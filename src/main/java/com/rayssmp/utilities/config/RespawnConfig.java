package com.rayssmp.utilities.config;

public record RespawnConfig(boolean enabled, boolean skipRespawnScreen, boolean useFakeDeath, boolean spreadItemsOnDeath,
                            boolean preferBedLocation, boolean preferAnchorLocation, boolean useWorldDefaultSpawnLocation, Action action) {
    public RespawnConfig() {
        this(false, false, false, false, false, false, false, new Action());
    }
}
