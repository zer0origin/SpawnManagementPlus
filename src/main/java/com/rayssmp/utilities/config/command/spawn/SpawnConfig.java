package com.rayssmp.utilities.config.command.spawn;

import com.rayssmp.utilities.config.WorldLocation;

import java.util.List;

public record SpawnConfig(boolean enabled, int seconds,
                          String insufficientPermissionErrorMessage, String youAreAlreadyTeleporting, WorldLocation worldLocation, onTeleport onTeleport, onMove onMove, onInterval onInterval,
                          boolean useOnServerJoinLocation

) {
    public SpawnConfig() {
        this(false, 0, "", "", new WorldLocation(), new onTeleport(), new onMove(), new onInterval(), false);
    }

    public static SpawnConfig SpawnFactory(boolean enabled, int seconds,
                                           String insufficientPermissionErrorMessage, String youAreAlreadyTeleporting, boolean useOnServerJoinLocation, WorldLocation worldLocation,
                                           String onTeleportMessageType, List<String> onTeleportMessages, boolean onTeleportSoundEnabled,
                                           String onTeleportSoundType, float onTeleportSoundVolume, float onTeleportSoundPitch,
                                           String onIntervalMessageType, List<String> onIntervalMessages, boolean onIntervalSoundEnabled,
                                           String onIntervalSoundType, float onIntervalSoundVolume, float onIntervalSoundPitch,
                                           boolean onMoveCancel, String onMoveMessageType, boolean onMoveSoundEnabled, String onMoveSoundType,
                                           float onMoveSoundVolume, float onMoveSoundPitch, List<String> onMoveMessages) {

        return new SpawnConfig(enabled, seconds, insufficientPermissionErrorMessage, youAreAlreadyTeleporting, worldLocation,
                new onTeleport(onTeleportMessageType, onTeleportMessages, onTeleportSoundEnabled, onTeleportSoundType, onTeleportSoundVolume, onTeleportSoundPitch),
                new onMove(onMoveCancel, onMoveMessageType, onMoveMessages, onMoveSoundEnabled, onMoveSoundType, onMoveSoundVolume, onMoveSoundPitch),
                new onInterval(onIntervalMessageType, onIntervalMessages, onIntervalSoundEnabled, onIntervalSoundType, onIntervalSoundVolume, onIntervalSoundPitch), useOnServerJoinLocation);
    }
}