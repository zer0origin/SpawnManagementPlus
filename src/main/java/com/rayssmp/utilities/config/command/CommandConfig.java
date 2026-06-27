package com.rayssmp.utilities.config.command;


import com.rayssmp.utilities.config.command.spawn.SpawnConfig;

public record CommandConfig(SetJoin setJoin, SetSpawn setSpawn, SpawnConfig spawnConfig, Smp smp) {
    public CommandConfig() {
        this(new SetJoin(), new SetSpawn(), new SpawnConfig(), new Smp());
    }
}