package me.kaeternn.sognodighast.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.papermc.paper.event.entity.EntityMoveEvent;

import me.kaeternn.sognodighast.SognoDiGhast;

public class SDGListener implements Listener {
    private SognoDiGhast plugin;
    
    public SDGListener(SognoDiGhast plugin) {
        this.plugin = plugin;
        if (plugin.debug)
            plugin.getLogger().info("Listener registered.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHappyGhastMove(EntityMoveEvent event) {
        if (event.isCancelled()) return;
        if (!event.getEntityType().equals(EntityType.HAPPY_GHAST)) return;

        happyGhastHandler(event.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if ((!event.getPlayer().isInsideVehicle()) ||
            (!event.getPlayer().getVehicle().getType().equals(EntityType.HAPPY_GHAST))) return;

        happyGhastHandler((LivingEntity) event.getPlayer().getVehicle());
    }

    public void happyGhastHandler(LivingEntity happyGhast) {
        AttributeInstance flyingSpeedAttribute = happyGhast.getAttribute(Attribute.FLYING_SPEED);

        if (happyGhast.getLocation().getBlockY() >= plugin.minHeight) {
            if (flyingSpeedAttribute.getModifier(plugin.key) == null) {
                flyingSpeedAttribute.addModifier(plugin.modifier);

                if (plugin.debug)
                    plugin.getLogger().info("Modifier applied to Happy Ghast " + happyGhast.getUniqueId()
                        + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
            } else {
                if (flyingSpeedAttribute.getModifier(plugin.key).getAmount() != plugin.modifier.getAmount()) {
                    flyingSpeedAttribute.removeModifier(plugin.key);
                    flyingSpeedAttribute.addModifier(plugin.modifier);

                    if (plugin.debug)
                        plugin.getLogger().info("Modifier modified for Happy Ghast " + happyGhast.getUniqueId()
                            + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
                }
            }
        } else {
            if (flyingSpeedAttribute.getModifier(plugin.key) != null) {
                flyingSpeedAttribute.removeModifier(plugin.key);

                if (plugin.debug)
                    plugin.getLogger().info("Modifier removed from Happy Ghast " + happyGhast.getUniqueId()
                        + ", his flying speed is now " + flyingSpeedAttribute.getValue() + ".");
            }
        }
    }
}