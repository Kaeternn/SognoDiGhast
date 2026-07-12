package me.kaeternn.sognodighast.entities;

import org.bukkit.World.Environment;

import org.jetbrains.annotations.NotNull;

public class SDGEnvironment {
    private Environment environment;
    private Integer min;
    private Integer max;

    public SDGEnvironment(@NotNull Environment environment, Integer min, Integer max) {
        this.environment = environment;
        this.min = min;
        this.max = max;
    }

    public Environment getEnvironment() { return environment; }
    public Integer getMin() { return min; }
    public Integer getMax() { return max; }
}
