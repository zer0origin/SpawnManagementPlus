package com.rayssmp.utilities.config.command;

/**
 * @param enabled if the action should contain sound
 * @param type    the type of sound
 * @param volume  how loud the sound is
 * @param pitch   what pitch the sound should play at
 */
public record Sound(boolean enabled, String type, float volume, float pitch) {
    public Sound() {
        this(false, "", 0, 0);
    }
}
