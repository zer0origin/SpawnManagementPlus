package com.rayssmp.utilities.config.command;

public record SetSpawn(String insufficientPermissionErrorMessage, String savedDataMessage, String savedDataFailedMessage) {
    public SetSpawn() {
        this("", "", "");
    }
}
