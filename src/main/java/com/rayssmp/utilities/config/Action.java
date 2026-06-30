package com.rayssmp.utilities.config;

import com.rayssmp.utilities.config.command.Sound;

import java.util.List;

/**
 * @param worldLocation   The location to use for the action
 * @param messageType     what type of message should be used CHAT or ACTION_BAR
 * @param messageContents the contents of the message, its recommended that ACTION_BAR only uses 1 message.
 */
public record Action(WorldLocation worldLocation, Sound sound, RunCommand runCommand, String messageType, List<String> messageContents) {
    public Action() {
        this(new WorldLocation(), new Sound(), new RunCommand(), "", null);
    }
}
