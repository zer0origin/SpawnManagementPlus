package com.rayssmp.utilities.config.command.spawn;

import java.util.List;

public record SpawnConfig(boolean enabled, int seconds,
                          String insufficientPermissionErrorMessage, String youAreAlreadyTeleporting, String world,
                          double x, double y, double z, float yaw,
                          float pitch, onTeleport onTeleport, onMove onMove, onInterval onInterval

) {
    public SpawnConfig() {
        this(false, 0, "", "", "", 0, 0, 0, 0, 0, new onTeleport(), new onMove(), new onInterval());
    }

    public static SpawnConfig SpawnFactory(boolean enabled, int seconds,
                                           String insufficientPermissionErrorMessage, String youAreAlreadyTeleporting, String world, double x, double y, double z, float yaw,
                                           float pitch,
                                           String onTeleportMessageType, List<String> onTeleportMessages, boolean onTeleportSoundEnabled,
                                           String onTeleportSoundType, float onTeleportSoundVolume, float onTeleportSoundPitch,
                                           String onIntervalMessageType, List<String> onIntervalMessages, boolean onIntervalSoundEnabled,
                                           String onIntervalSoundType, float onIntervalSoundVolume, float onIntervalSoundPitch,
                                           boolean onMoveCancel, String onMoveMessageType, boolean onMoveSoundEnabled, String onMoveSoundType,
                                           float onMoveSoundVolume, float onMoveSoundPitch, List<String> onMoveMessages) {

        return new SpawnConfig(enabled, seconds, insufficientPermissionErrorMessage, youAreAlreadyTeleporting, world, x, y, z, yaw, pitch,
                new onTeleport(onTeleportMessageType, onTeleportMessages, onTeleportSoundEnabled, onTeleportSoundType, onTeleportSoundVolume, onTeleportSoundPitch),
                new onMove(onMoveCancel, onMoveMessageType, onMoveMessages, onMoveSoundEnabled, onMoveSoundType, onMoveSoundVolume, onMoveSoundPitch),
                new onInterval(onIntervalMessageType, onIntervalMessages, onIntervalSoundEnabled, onIntervalSoundType, onIntervalSoundVolume, onIntervalSoundPitch));
    }
}