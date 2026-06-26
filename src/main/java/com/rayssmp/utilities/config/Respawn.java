package com.rayssmp.utilities.config;

import java.util.List;

public record Respawn(boolean enabled, boolean skipRespawnScreen, boolean spreadItemsOnDeath,
                      boolean preferBedLocation, boolean preferAnchorLocation, String world, double x,
                      double y,
                      double z, float yaw, float pitch, String messageType, List<String> messageContent) {
    public Respawn() {
        this(false, false, false, false, false, "", 0, 0, 0, 0, 0, "", null);
    }
}
