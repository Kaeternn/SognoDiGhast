package me.kaeternn.sognodighast.listeners;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.papermc.paper.event.entity.EntityMoveEvent;

import me.kaeternn.sognodighast.SognoDiGhast;
import me.kaeternn.sognodighast.entities.SDGEnvironment;
import me.kaeternn.sognodighast.entities.SDGWorld;

public class SDGListener implements Listener {
    private SognoDiGhast plugin;
    private boolean onlyWhenRidden;
    private boolean onlyHappyGhast;
    
    public SDGListener(SognoDiGhast plugin) {
        this.plugin = plugin;
        this.onlyWhenRidden = plugin.onlyWhenRidden;
        this.onlyHappyGhast = plugin.onlyHappyGhast;

        if (plugin.debug) plugin.getLogger().info("Listener registered.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGhastMove(EntityMoveEvent event) {
        if (event.isCancelled()) return; // IF event cancelled by another plugin
        boolean forceRemove = false;

        switch (event.getEntityType()) {
            case GHAST:
                if (onlyHappyGhast) forceRemove = true; // IF modifier only applied on Happy Ghast
            case HAPPY_GHAST:
                if (onlyWhenRidden) forceRemove = true; //IF modifier only applied on ridden Happy Ghast
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
                if (onlyHappyGhast) forceRemove = true; // IF modifier only applied on Happy Ghast
            case HAPPY_GHAST:
                break;
            default:
                return;
        }

        GhastHandler((LivingEntity) event.getPlayer().getVehicle(), forceRemove);
    }

    public void GhastHandler(LivingEntity ghast, boolean forceRemove) {
        AttributeInstance flyingSpeedAttribute = ghast.getAttribute(Attribute.FLYING_SPEED);
        Integer min = -2; // -2 mean undified, -1 mean no limit
        Integer max = -2;

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
        if (!forceRemove && ((min != -1 || ghastLocation.getBlockY() >= min) // IF forceRemove false OR min isn't limited OR ghast above min limit
                && (max != -1 || ghastLocation.getBlockY() <= max))) { // AND max isn't limited OR ghast below max limit
            if (flyingSpeedAttribute.getModifier(plugin.key) == null) { // IF ghast don't have the modifier
                flyingSpeedAttribute.addModifier(plugin.modifier);

                if (plugin.debug) plugin.getLogger().info("Modifier applied to Ghast " + ghast.getUniqueId() + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
            } else { // IF ghast have the modifier, check if it need to be modified
                if (flyingSpeedAttribute.getModifier(plugin.key).getAmount() != plugin.modifier.getAmount()) {
                    flyingSpeedAttribute.removeModifier(plugin.key);
                    flyingSpeedAttribute.addModifier(plugin.modifier);

                    if (plugin.debug) plugin.getLogger().info("Modifier modified for Ghast " + ghast.getUniqueId() + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
                }
            }
        } else {
            if (flyingSpeedAttribute.getModifier(plugin.key) != null) {
                flyingSpeedAttribute.removeModifier(plugin.key);

                if (plugin.debug) plugin.getLogger().info("Modifier removed from Ghast " + ghast.getUniqueId() + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
            }
        }
    }
}