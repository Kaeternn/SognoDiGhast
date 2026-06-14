package me.kaeternn.sognodighast;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import me.kaeternn.sognodighast.commands.*;
import me.kaeternn.sognodighast.entities.*;
import me.kaeternn.sognodighast.listeners.*;

public class SognoDiGhast extends JavaPlugin {
    public static SognoDiGhast plugin;
    public NamespacedKey key;
    public NamespacedKey legacyKey;
    public boolean debug;
    public boolean onlyWhenRidden;
    public boolean onlyHappyGhast;
    public AttributeModifier modifier;
    public AttributeModifier legacyModifier;
    private List<SDGEnvironment> environments = new ArrayList<>();
    private List<SDGWorld> worlds = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        key = new NamespacedKey(plugin, "FlyingSpeedModifier");
        legacyKey = new NamespacedKey(plugin, "HappyGhastSpeedModifier");

        loadConfig();
        getServer().getPluginManager().registerEvents(new SDGListener(plugin), plugin);
        registerCommand("sdg", new SDGCommand(plugin));
    }

    public void loadConfig() {
        reloadConfig();

        debug = getConfig().getBoolean("debug");
        if (debug) getLogger().info("Debug mode enabled.");
        
        onlyWhenRidden = getConfig().getBoolean("only_when_ridden");
        if (debug) getLogger().info("Modifier only applied on ridden ghasts.");

        onlyHappyGhast = getConfig().getBoolean("only_happy_ghast");
        if (debug) getLogger().info("Modifier only applied on happy ghasts.");

        modifier = new AttributeModifier(key, getConfig().getDouble("speed_multiplier") - 1.0, Operation.MULTIPLY_SCALAR_1);
        legacyModifier = new AttributeModifier(legacyKey, getConfig().getDouble("speed_multiplier") - 1.0, Operation.MULTIPLY_SCALAR_1);
        if (debug) getLogger().info("Flying speed multiplier set to " + getConfig().getDouble("speed_multiplier") + ".");

        ConfigurationSection dimensionsSection = getConfig().getConfigurationSection("dimensions");
        if (dimensionsSection == null) {
            getLogger().severe("No dimensions defined in config, it will be regenerated.");
        } else {
            for (String key : dimensionsSection.getKeys(false)) {
                Environment dimension;
                
                switch (key) {
                    case "overworld":
                        dimension = Environment.NORMAL;
                        break;
                    case "nether":
                        dimension = Environment.NETHER;
                        break;
                    case "end":
                        dimension = Environment.THE_END;
                        break;
                    default:
                        getLogger().severe("Dimension " + key + " not found don't add new dimension, use world configuration instead.");
                        continue;
                }

                ConfigurationSection dimensionSection = dimensionsSection.getConfigurationSection(key);

                Integer[] values = getLimitValues(dimensionSection);
                
                SDGEnvironment newDimension = new SDGEnvironment(dimension, values[0], values[1]);

                environments.add(newDimension);
                if (debug) getLogger().info("Dimension " + key + " added with min height " + dimensionSection.getInt("min") + " and max height " + dimensionSection.getInt("max") + ".");
            }
        }

        ConfigurationSection worldsSection = getConfig().getConfigurationSection("worlds");
        if (worldsSection == null) {
            if (debug) getLogger().info("No worlds defined in config.");
        } else {
            for (String key : worldsSection.getKeys(false)) {
                World world = Bukkit.getWorld(key);
                if (world == null) {
                    getLogger().severe("World " + key + " not found, please verify that you wrote the correct name.");
                    continue;
                }

                ConfigurationSection worldSection = worldsSection.getConfigurationSection(key);

                Integer[] values = getLimitValues(worldSection);

                SDGWorld newWorld = new SDGWorld(world, values[0], values[1]);

                worlds.add(newWorld);
                if (debug) getLogger().info("World " + key + " added with min height " + worldSection.getInt("min") + " and max height " + worldSection.getInt("max") + ".");
            }
        }

        saveConfig();
    }

    private Integer[] getLimitValues(ConfigurationSection section) {
        Integer min = null;
        if (section.getString("min").matches("-?\\d+")) min = section.getInt("min");
        else if (section.getString("min") != "infinity") {
            section.set("min", "infinity");
            getLogger().severe("Invalid value for min height, it was changed to \"infinity\"");
        }

        Integer max = null;
        if (section.getString("max").matches("-?\\d+")) max = section.getInt("max");
        else if (section.getString("max") != "infinity") {
            section.set("max", "infinity");
            getLogger().severe("Invalid value for max height, it was changed to \"infinity\"");
        }

        return new Integer[] { min, max };
    }

    public SDGEnvironment toSDGEnvironment(Environment toConvertEnvironment) {
        for (SDGEnvironment environment : environments)
            if (environment.getEnvironment().equals(toConvertEnvironment)) return environment;
        
        return null;
    }

    public SDGWorld toSDGWorld(World toConvertWorld) {
        for (SDGWorld world : worlds)
            if (world.getWorld().equals(toConvertWorld)) return world;
        
        return null;
    }
}
