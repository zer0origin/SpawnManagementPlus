package com.rayssmp.utilities.config;

public record RespawnConfig(boolean enabled, boolean skipRespawnScreen, boolean forceRespawnButKeepDefaultMessage, boolean spreadItemsOnDeath,
                            boolean preferBedLocation, boolean preferAnchorLocation, Action action) {
    public RespawnConfig() {
        this(false, false, false, false, false, false, new Action());
    }
}
