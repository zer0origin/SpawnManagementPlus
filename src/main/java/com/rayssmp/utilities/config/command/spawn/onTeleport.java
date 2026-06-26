package com.rayssmp.utilities.config.command.spawn;

import java.util.List;

public record onTeleport(String messageType, List<String> messages, boolean soundEnabled,
                         String soundType, float soundVolume, float soundPitch) {
    public onTeleport() {
        this("",null,false,"",0,0);
    }
}
