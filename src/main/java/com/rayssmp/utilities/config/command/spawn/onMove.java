package com.rayssmp.utilities.config.command.spawn;

import java.util.List;

public record onMove(boolean enabled, String messageType, List<String> messages, boolean soundEnabled, String soundType,
                     float soundVolume, float soundPitch) {
    public onMove() {
        this(false, "",null,false,"",0,0);
    }
}
