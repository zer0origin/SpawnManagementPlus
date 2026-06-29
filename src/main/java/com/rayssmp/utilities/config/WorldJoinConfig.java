package com.rayssmp.utilities.config;

import java.util.List;

/**
 * The world join config POJO representation
 * @param enabled Is this setting enabled?
 * @param exclude List of world names that should be excluded from this config.
 * @param action Actions to perform, note: world, x,y,z should not be used.
 */
public record WorldJoinConfig(boolean enabled, List<String> exclude, Action action) {
    public WorldJoinConfig() {
        this(false, null, new Action());
    }
}
