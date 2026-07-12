package me.kaeternn.sognodighast.entities;

import org.bukkit.World;

import org.jetbrains.annotations.NotNull;

public class SDGWorld {
    private World world;
    private Integer min;
    private Integer max;

    public SDGWorld(@NotNull World world, Integer min, Integer max) {
        this.world = world;
        this.min = min;
        this.max = max;
    }

    public World getWorld() { return world; }
    public Integer getMin() { return min; }
    public Integer getMax() { return max; }
}
