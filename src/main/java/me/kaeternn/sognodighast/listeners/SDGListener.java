package me.kaeternn.sognodighast.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import org.jetbrains.annotations.NotNull;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.event.player.PlayerCustomClickEvent;

import me.kaeternn.sognodighast.SognoDiGhast;
import me.kaeternn.sognodighast.entities.SDGConfig;
import me.kaeternn.sognodighast.entities.SDGEnvironment;
import me.kaeternn.sognodighast.entities.SDGWorld;

import net.kyori.adventure.key.Key;

public class SDGListener implements Listener {
    private SognoDiGhast plugin;
    
    public SDGListener(@NotNull SognoDiGhast plugin) {
        this.plugin = plugin;

        if (plugin.cachedConfig.isDebugEnabled()) plugin.getLogger().info("Listener registered.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGhastMove(EntityMoveEvent event) {
        if (event.isCancelled()) return; // IF event cancelled by another plugin
        boolean forceRemove = false;

        switch (event.getEntityType()) {
            case GHAST:
                if (plugin.cachedConfig.isOnlyHappyGhast()) forceRemove = true; // IF modifier only applied on Happy Ghast
            case HAPPY_GHAST:
                if (plugin.cachedConfig.isOnlyWhenRidden()) forceRemove = true; //IF modifier only applied on ridden Happy Ghast
                break;
            default:
                return;
        }

        GhastHandler(event.getEntity(), forceRemove);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled() // IF event cancelled by another plugin OR player not in a vehicle
            || (!event.getPlayer().isInsideVehicle())) return; 
        boolean forceRemove = false;

        switch (event.getPlayer().getVehicle().getType()) {
            case GHAST:
                if (plugin.cachedConfig.isOnlyHappyGhast()) forceRemove = true; // IF modifier only applied on Happy Ghast
            case HAPPY_GHAST:
                break;
            default:
                return;
        }

        GhastHandler((LivingEntity) event.getPlayer().getVehicle(), forceRemove);
    }

    @EventHandler
    public void onPlayerDialogQuit(PlayerCustomClickEvent event) {
        DialogResponseView view = event.getDialogResponseView();
        if (view == null) return;

        Player player = Bukkit.getPlayer(view.getText("player"));
        if (player == null) return;

        if (event.getIdentifier().equals(Key.key("general_dialog_confirmation"))) {
            boolean debug = view.getBoolean("debug");
            boolean onlyHappyGhast = view.getBoolean("only_happy_ghast");
            boolean onlyWhenRidden = view.getBoolean("only_when_ridden");
            Double speedModifier = Double.valueOf(view.getText("speed_multiplier"));

            SDGConfig newConfig = new SDGConfig(debug, onlyWhenRidden, onlyHappyGhast, speedModifier);

            plugin.setConfig(newConfig);
        } else if (event.getIdentifier().equals(Key.key("environment_dialog_confirmation"))) {
            Environment environmentType;

            switch (view.getText("environment")) {
                case "overworld":
                    environmentType = Environment.NORMAL;
                    break;
                case "nether":
                    environmentType = Environment.NETHER;
                    break;
                case "end":
                    environmentType = Environment.THE_END;
                    break;
                default:
                    player.sendMessage("§4Selected dimension was not recognized, please report this.");
                    return;
            }

            Integer min = null;
            if (view.getText("min").matches("-?\\d+")) min = Integer.valueOf(view.getText("min"));
            else if (!view.getText("min").equals("infinity")) {
                player.sendMessage("§4Invalid minimum height value.");
                return;
            }

            Integer max = null;
            if (view.getText("max").matches("-?\\d+")) min = Integer.valueOf(view.getText("max"));
            else if (!view.getText("max").equals("infinity")) {
                player.sendMessage("§4Invalid maximum height value.");
                return;
            }

            SDGEnvironment environment = new SDGEnvironment(environmentType, min, max);

            plugin.setEnvironment(environment);
        } else if (event.getIdentifier().equals(Key.key("world_dialog_confirmation"))) {
            SDGWorld world;

            if (Bukkit.getWorld(view.getText("world")) != null) {
                Integer min = null;
                if (view.getText("min").matches("-?\\d+")) min = Integer.valueOf(view.getText("min"));
                else if (!view.getText("min").equals("infinity")) {
                    player.sendMessage("§4Invalid minimum height value.");
                    return;
                }

                Integer max = null;
                if (view.getText("max").matches("-?\\d+")) min = Integer.valueOf(view.getText("max"));
                else if (!view.getText("max").equals("infinity")) {
                    player.sendMessage("§4Invalid maximum height value.");
                    return;
                }

                world = new SDGWorld(
                    Bukkit.getWorld(view.getText("world")), 
                    min, 
                    max
                );
            } else {
                player.sendMessage("§4Invalid world name.");
                return;
            }

            plugin.setWorld(world);
        }
    }

    public void GhastHandler(@NotNull LivingEntity ghast, @NotNull boolean forceRemove) {
        AttributeInstance flyingSpeedAttribute = ghast.getAttribute(Attribute.FLYING_SPEED);
        Integer min = null;
        Integer max = null;

        Location ghastLocation = ghast.getLocation();
        SDGWorld ghastWorld = plugin.toSDGWorld(ghastLocation.getWorld());
        if (ghastWorld != null) {
            min = ghastWorld.getMin();
            max = ghastWorld.getMax();
        } else {
            SDGEnvironment ghastEnvironment = plugin.toSDGEnvironment(ghastLocation.getWorld().getEnvironment());
            if (ghastEnvironment != null) {
                min = ghastEnvironment.getMin();
                max = ghastEnvironment.getMax();
            } else forceRemove = true;
        }

        if (flyingSpeedAttribute.getModifier(plugin.legacyKey) != null) flyingSpeedAttribute.removeModifier(plugin.legacyKey);

        boolean isInMinLimit = false;
        boolean isInMaxLimit = false;
        if (!forceRemove){ // IF forceRemove disabled
            if (min == null) isInMinLimit = true; // IF min limit disabled or respected
            else if (ghastLocation.getBlockY() >= min) isInMinLimit = true;

            if (max == null) isInMaxLimit = true; // IF max limit disabled or respected
            else if (ghastLocation.getBlockY() <= max) isInMaxLimit = true;
        }

        if (isInMinLimit && isInMaxLimit) {
            if (flyingSpeedAttribute.getModifier(plugin.key) == null) { // IF ghast don't have the modifier
                flyingSpeedAttribute.addModifier(plugin.modifier);

                if (plugin.cachedConfig.isDebugEnabled()) plugin.getLogger().info("Modifier applied to Ghast " + ghast.getUniqueId() + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
            } else { // IF ghast have the modifier, check if it need to be modified
                if (flyingSpeedAttribute.getModifier(plugin.key).getAmount() != plugin.modifier.getAmount()) {
                    flyingSpeedAttribute.removeModifier(plugin.key);
                    flyingSpeedAttribute.addModifier(plugin.modifier);

                    if (plugin.cachedConfig.isDebugEnabled()) plugin.getLogger().info("Modifier modified for Ghast " + ghast.getUniqueId() + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
                }
            }
        } else {
            if (flyingSpeedAttribute.getModifier(plugin.key) != null) {
                flyingSpeedAttribute.removeModifier(plugin.key);

                if (plugin.cachedConfig.isDebugEnabled()) plugin.getLogger().info("Modifier removed from Ghast " + ghast.getUniqueId() + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
            }
        }
    }
}