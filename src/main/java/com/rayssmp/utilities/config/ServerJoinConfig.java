package com.rayssmp.utilities.config;

import com.rayssmp.utilities.config.command.OnlyOnFirstTime;

import java.util.List;

public record ServerJoinConfig(boolean enabled, List<String> exclude, Action action, OnlyOnFirstTime onlyOnFirstTime) {
    public ServerJoinConfig() {
        this(false, null, new Action(), new OnlyOnFirstTime());
    }
}
