package me.kaeternn.sognodighast;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.plugin.java.JavaPlugin;

import me.kaeternn.sognodighast.commands.SDGCommand;
import me.kaeternn.sognodighast.listeners.SDGListener;

public class SognoDiGhast extends JavaPlugin {
    public static SognoDiGhast plugin;
    public NamespacedKey key;
    public boolean debug;
    public AttributeModifier modifier;
    public int minHeight;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        key = new NamespacedKey(plugin, "HappyGhastSpeedModifier");

        loadConfig();
        getServer().getPluginManager().registerEvents(new SDGListener(plugin), plugin);
        registerCommand("sognodighast", new SDGCommand(plugin));
    }

    public void loadConfig() {
        reloadConfig(); 
        debug = getConfig().getBoolean("debug");
        if (debug) getLogger().info("Debug mode enabled.");
        modifier = new AttributeModifier(key, getConfig().getDouble("speed_multiplier") - 1.0, Operation.MULTIPLY_SCALAR_1);
        if (debug) getLogger().info("Flying speed multiplier set to " + getConfig().getDouble("speed_multiplier") + ".");
        minHeight = getConfig().getInt("min_height");
        if (debug) getLogger().info("Minimum height set to " + minHeight + ".");
    }
}
