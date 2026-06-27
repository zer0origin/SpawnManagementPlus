package com.rayssmp.utilities.config.command;


import com.rayssmp.utilities.config.command.spawn.SpawnConfig;

public record CommandConfig(SetJoin setJoin, SetSpawn setSpawn, Smp smp, SpawnConfig spawnConfig) {
    public CommandConfig() {
        this(new SetJoin(), new SetSpawn(), new Smp(), new SpawnConfig());
    }
}