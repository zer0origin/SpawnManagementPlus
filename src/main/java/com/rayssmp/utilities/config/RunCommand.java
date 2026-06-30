package com.rayssmp.utilities.config;

public record RunCommand(String commandToRun, String user) {
    public RunCommand() {
        this("", "");
    }
}
