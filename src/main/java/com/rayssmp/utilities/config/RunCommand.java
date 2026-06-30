package com.rayssmp.utilities.config;

public record RunCommand(boolean enabled, String commandToRun, String user) {
    public RunCommand() {
        this(false, "", "");
    }
}
