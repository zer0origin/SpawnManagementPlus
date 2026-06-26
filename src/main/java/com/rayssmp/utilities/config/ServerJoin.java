package com.rayssmp.utilities.config;

import java.util.List;

public record ServerJoin(boolean enabled, boolean onlyOnFirstTime, String world, boolean soundEnabled,
                         String soundType, float soundVolume, float soundPitch, boolean useWorldDefault, double x,
                         double y, double z, float yaw, float pitch, String messageType,
                         List<String> messageContents, List<String> exclude) {
    public ServerJoin() {
        this(false, false, "", false, "", 0, 0, false, 0, 0, 0, 0, 0, "", null, null);
    }
}
