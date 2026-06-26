package com.rayssmp.utilities.config.command.spawn;

import java.util.List;

public record Spawn(boolean enabled, int seconds,
                    String insufficientPermissionErrorMessage, String world, double x, double y, double z, float yaw,
                    float pitch, onTeleport onTeleport, onMove onMove, onInterval onInterval

) {
    public Spawn() {
        this(false, 0, "", "", 0, 0, 0, 0, 0, new onTeleport(), new onMove(), new onInterval());
    }

    public static Spawn SpawnFactory(boolean enabled, int seconds,
                                     String insufficientPermissionErrorMessage, String world, double x, double y, double z, float yaw,
                                     float pitch,
                                     String onTeleportMessageType, List<String> onTeleportMessages, boolean onTeleportSoundEnabled,
                                     String onTeleportSoundType, float onTeleportSoundVolume, float onTeleportSoundPitch,
                                     String onIntervalMessageType, List<String> onIntervalMessages, boolean onIntervalSoundEnabled,
                                     String onIntervalSoundType, float onIntervalSoundVolume, float onIntervalSoundPitch,
                                     boolean onMoveCancel, String onMoveMessageType, boolean onMoveSoundEnabled, String onMoveSoundType,
                                     float onMoveSoundVolume, float onMoveSoundPitch, List<String> onMoveMessages) {

        return new Spawn(enabled,seconds,insufficientPermissionErrorMessage,world,x,y,z,yaw,pitch,
                new onTeleport(onTeleportMessageType, onTeleportMessages, onTeleportSoundEnabled, onTeleportSoundType, onTeleportSoundVolume, onTeleportSoundPitch),
                new onMove(onMoveCancel, onMoveMessageType, onMoveMessages, onMoveSoundEnabled, onMoveSoundType, onMoveSoundVolume, onMoveSoundPitch),
                new onInterval(onIntervalMessageType, onIntervalMessages, onIntervalSoundEnabled, onIntervalSoundType, onIntervalSoundVolume, onIntervalSoundPitch));
    }
}