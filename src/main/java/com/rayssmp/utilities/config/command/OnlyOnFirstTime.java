package com.rayssmp.utilities.config.command;

import com.rayssmp.utilities.config.Action;

public record OnlyOnFirstTime(boolean enabled, boolean useOnServerJoinDefault, Action action) {
    public OnlyOnFirstTime() {
        this(false, true, new Action());
    }
}
