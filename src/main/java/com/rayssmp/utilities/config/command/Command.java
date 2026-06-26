package com.rayssmp.utilities.config.command;


import com.rayssmp.utilities.config.command.spawn.Spawn;

public record Command(SetJoin setJoin, SetSpawn setSpawn, Spawn spawn) {
    public Command() {
        this(new SetJoin(), new SetSpawn(), new Spawn());
    }
}