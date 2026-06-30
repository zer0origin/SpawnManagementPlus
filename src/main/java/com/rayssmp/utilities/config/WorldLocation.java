package com.rayssmp.utilities.config;

public record WorldLocation(boolean enabled, String name, double x, double y, double z, float yaw, float pitch) {
    public WorldLocation() {
        this(false, "", 0, 0, 0, 0, 0);
    }
}
