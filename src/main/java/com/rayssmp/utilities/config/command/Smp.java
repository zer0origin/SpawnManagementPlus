package com.rayssmp.utilities.config.command;

public record Smp(String insufficientPermissionErrorMessage, String savedDataMessage, String savedDataFailedMessage) {
    public Smp() {
        this("","","");
    }
}
