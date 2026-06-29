package com.rayssmp.utilities.config.command;


import com.rayssmp.utilities.config.command.spawn.SpawnConfig;

public record CommandConfig(Smp smp, SpawnConfig spawnConfig) {
    public CommandConfig() {
        this(new Smp(), new SpawnConfig());
    }
}