package me.kaeternn.sognodighast.entities;

import org.jetbrains.annotations.NotNull;

public class SDGConfig {
    private boolean debug;
    private boolean onlyWhenRidden;
    private boolean onlyHappyGhast;
    private Double speedMultiplier;

    public SDGConfig(@NotNull boolean debug, @NotNull boolean onlyWhenRidden, @NotNull boolean onlyHappyGhast, @NotNull Double speedMultiplier) {
        this.debug = debug;
        this.onlyWhenRidden = onlyWhenRidden;
        this.onlyHappyGhast = onlyHappyGhast;
        this.speedMultiplier = speedMultiplier;
    }

    public boolean isDebugEnabled() { return debug; }
    public boolean isOnlyWhenRidden() { return onlyWhenRidden; }
    public boolean isOnlyHappyGhast() { return onlyHappyGhast; }
    public Double getSpeedMultiplier() { return speedMultiplier; }
}
