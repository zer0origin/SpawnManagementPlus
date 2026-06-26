package com.rayssmp.utilities.config.command;

public record SetJoin(String insufficientPermissionErrorMessage, String savedDataMessage,
                      String savedDataFailedMessage) {
    public SetJoin() {
        this("", "", "");
    }
}
