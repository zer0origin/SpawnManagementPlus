package com.rayssmp.utilities.config;

import java.util.List;

public record WorldJoin(boolean enabled, List<String> exclude, boolean soundEnabled, String soundType,
                        float soundVolume, float soundPitch, float yaw, float pitch,
                        String messageType, List<String> messageContents) {
    public WorldJoin() {
        this(false, null, false, "", 0, 0, 0, 0, "", null);
    }
}
