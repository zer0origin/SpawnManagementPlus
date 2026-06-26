package com.rayssmp.utilities.config.command.spawn;

import java.util.List;

public record onInterval(String messageType, List<String> messages, boolean soundEnabled,
                         String soundType, float soundVolume, float soundPitch) {
    public onInterval() {
        this("",null,false,"",0,0);
    }
}
