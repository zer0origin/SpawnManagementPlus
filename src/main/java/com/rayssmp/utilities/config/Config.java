package com.rayssmp.utilities.config;

import com.rayssmp.utilities.config.command.Command;
import com.rayssmp.utilities.config.command.SetJoin;
import com.rayssmp.utilities.config.command.SetSpawn;
import com.rayssmp.utilities.config.command.spawn.Spawn;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class Config {
    private final File configFileLocation = new File("plugins/SpawnManagementPlus", "config.yml");
    private FileConfiguration cfg = new YamlConfiguration();
    private ServerJoin serverJoinSettings = new ServerJoin();
    private WorldJoin worldJoinSettings = new WorldJoin();
    private Command command = new Command();
    private Respawn respawn = new Respawn();
    private boolean settingsHaveBeenUpdated = false;

    public void load() throws IOException {
        if (!configFileLocation.exists()) {
            configFileLocation.createNewFile();
            try (InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("config.yml"), "Unable to write config file!"); OutputStream output = new FileOutputStream(configFileLocation)) {

                long transferred = is.transferTo(output);
                if (transferred <= 0) {
                    throw new IllegalStateException("Failed to write config file!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            cfg = new YamlConfiguration();
            cfg.load(configFileLocation);
            serverJoinSettings = loadServerJoinValues(cfg);
            worldJoinSettings = loadWorldJoinValues(cfg);
            command = loadCommandSettingValues(cfg);
            respawn = loadRespawnSettingsValues(cfg);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() throws IOException {
        if (!settingsHaveBeenUpdated) {
            return;
        }

        setServerJoinValues(cfg, this.serverJoinSettings);
        setWorldJoinValues(cfg, this.worldJoinSettings);
        setCommandValues(cfg, this.command);
        setRespawnValues(cfg, this.respawn);

        cfg.save(configFileLocation);
        settingsHaveBeenUpdated = false;
    }

    private ServerJoin loadServerJoinValues(FileConfiguration cfg) {
        var enabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.enabled", false);
        var onlyOnFirstTime = cfg.getBoolean("SpawnManagementPlus.on_server_join.only_on_first_time", false);
        var world = cfg.getString("SpawnManagementPlus.on_server_join.action.location.world", "");
        var useWorldDefault = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.location.use_world_default", false);
        var locationX = cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.x", 0);
        var locationY = cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.y", 0);
        var locationZ = cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.z", 0);
        var locationYaw = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.yaw", 0);
        var locationPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.pitch", 0);
        var messageContents = cfg.getStringList("SpawnManagementPlus.on_server_join.action.message");
        var messageType = cfg.getString("SpawnManagementPlus.on_server_join.action.message_type");
        var soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.sound.enabled", false);
        var soundType = cfg.getString("SpawnManagementPlus.on_server_join.action.sound.type", "Sound.GLASS");
        var soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.volume", 0);
        var soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.pitch", 0);
        var exclude = cfg.getStringList("SpawnManagementPlus.on_server_join.exclude");

        return new ServerJoin(enabled, onlyOnFirstTime, world, soundEnabled, soundType, soundVolume, soundPitch, useWorldDefault, locationX, locationY, locationZ, locationYaw, locationPitch, messageType, messageContents, exclude);
    }

    private void setServerJoinValues(FileConfiguration cfg, ServerJoin serverJoin) {
        cfg.set("SpawnManagementPlus.on_server_join.enabled", serverJoin.enabled());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time", serverJoin.onlyOnFirstTime());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.world", serverJoin.world());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.use_world_default", serverJoin.useWorldDefault());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.x", serverJoin.x());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.y", serverJoin.y());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.z", serverJoin.z());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.yaw", serverJoin.yaw());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.pitch", serverJoin.pitch());
        cfg.set("SpawnManagementPlus.on_server_join.action.message", serverJoin.messageContents());
        cfg.set("SpawnManagementPlus.on_server_join.action.message_type", serverJoin.messageType());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.enabled", serverJoin.soundEnabled());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.type", serverJoin.soundType());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.volume", serverJoin.soundVolume());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.pitch", serverJoin.soundPitch());
        cfg.set("SpawnManagementPlus.on_server_join.exclude", serverJoin.exclude());
    }

    private WorldJoin loadWorldJoinValues(FileConfiguration cfg) {
        var enabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.enabled", false);
        var exclude = cfg.getStringList("SpawnManagementPlus.on_world_join.exclude");
        var soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.action.sound.enabled", false);
        var soundType = cfg.getString("SpawnManagementPlus.on_world_join.action.sound.type", "Sound.GLASS");
        var soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.volume", 0);
        var soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.pitch", 0);
        var locationYaw = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.yaw", 0);
        var locationPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.pitch", 0);
        var messageType = cfg.getString("SpawnManagementPlus.on_world_join.action.message_type");
        var messageContents = cfg.getStringList("SpawnManagementPlus.on_world_join.action.message");

        return new WorldJoin(enabled, exclude, soundEnabled, soundType, soundVolume, soundPitch, locationYaw, locationPitch, messageType, messageContents);
    }

    private void setWorldJoinValues(FileConfiguration cfg, WorldJoin worldJoin) {
        cfg.set("SpawnManagementPlus.on_world_join.enabled", worldJoin.enabled());
        cfg.set("SpawnManagementPlus.on_world_join.exclude", worldJoin.exclude());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.enabled", worldJoin.soundEnabled());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.type", worldJoin.soundType());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.volume", worldJoin.soundVolume());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.pitch", worldJoin.soundPitch());
        cfg.set("SpawnManagementPlus.on_world_join.action.location.yaw", worldJoin.yaw());
        cfg.set("SpawnManagementPlus.on_world_join.action.location.pitch", worldJoin.pitch());
        cfg.set("SpawnManagementPlus.on_world_join.action.message_type", worldJoin.messageType());
        cfg.set("SpawnManagementPlus.on_world_join.action.message", worldJoin.messageContents());
    }

    private Command loadCommandSettingValues(FileConfiguration cfg) {
        var insufficientPermissionErrorMessage = cfg.getString("SpawnManagementPlus.commands.setjoinlocation.insufficient_permission_error_message");
        var savedDataMessage = cfg.getString("SpawnManagementPlus.commands.setjoinlocation.saved_data_message");
        var savedDataFailedMessage = cfg.getString("SpawnManagementPlus.commands.setjoinlocation.savedDataFailedMessage");
        SetJoin setJoin = new SetJoin(insufficientPermissionErrorMessage, savedDataMessage, savedDataFailedMessage);

        var setSpawnInsufficientPermissionErrorMessage = cfg.getString("SpawnManagementPlus.commands.setSpawn.insufficient_permission_error_message");
        var setSpawnSavedDataMessage = cfg.getString("SpawnManagementPlus.commands.setSpawn.saved_data_message");
        var setSpawnSavedDataFailedMessage = cfg.getString("SpawnManagementPlus.commands.setSpawn.saved_data_failed_message");
        SetSpawn setSpawn = new SetSpawn(setSpawnInsufficientPermissionErrorMessage, setSpawnSavedDataMessage, setSpawnSavedDataFailedMessage);

        boolean enabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.enabled");
        int seconds = cfg.getInt("SpawnManagementPlus.commands.spawn.cooldown_timer.seconds");
        String onTeleportMessageType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.message_type");
        List<String> onTeleportMessages = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.message");
        String onIntervalMessageType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message_type");
        List<String> onIntervalMessages = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message");
        boolean onMoveCancel = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.cancel");
        String onMoveMessageType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message_type");
        List<String> onMoveMessages = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message");
        String spawnInsufficientPermissionErrorMessage = cfg.getString("SpawnManagementPlus.commands.spawn.insufficient_permission_error_message");
        String world = cfg.getString("SpawnManagementPlus.commands.spawn.location.world");
        double x = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.x");
        double y = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.y");
        double z = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.z");
        float yaw = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.location.yaw");
        float pitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.location.pitch");

        boolean onTeleportSoundEnabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.enabled");
        String onTeleportSoundType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.type");
        float onTeleportSoundVolume = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.volume", 1);
        float onTeleportSoundPitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.pitch", 1);

        boolean onIntervalSoundEnabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.enabled");
        String onIntervalSoundType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.type");
        float onIntervalSoundVolume = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.volume", 1);
        float onIntervalSoundPitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.pitch", 1);

        boolean onMoveSoundEnabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.enabled");
        String onMoveSoundType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.type");
        float onMoveSoundVolume = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.volume", 1);
        float onMoveSoundPitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.pitch", 1);
        Spawn spawn = Spawn.SpawnFactory(enabled, seconds, spawnInsufficientPermissionErrorMessage, world, x, y, z, yaw, pitch,
                onTeleportMessageType, onTeleportMessages, onTeleportSoundEnabled, onTeleportSoundType,
                onTeleportSoundVolume, onTeleportSoundPitch, onIntervalMessageType, onIntervalMessages,
                onIntervalSoundEnabled, onIntervalSoundType, onIntervalSoundVolume, onIntervalSoundPitch, onMoveCancel,
                onMoveMessageType, onMoveSoundEnabled, onMoveSoundType, onMoveSoundVolume, onMoveSoundPitch, onMoveMessages);

        return new Command(setJoin, setSpawn, spawn);
    }

    private void setCommandValues(FileConfiguration cfg, Command command) {
        var setJoinCommand = command.setJoin();
        cfg.set("SpawnManagementPlus.commands.setjoinlocation.insufficient_permission_error_message", setJoinCommand.insufficientPermissionErrorMessage());
        cfg.set("SpawnManagementPlus.commands.setjoinlocation.saved_data_message", setJoinCommand.savedDataFailedMessage());
        cfg.set("SpawnManagementPlus.commands.setjoinlocation.savedDataFailedMessage", setJoinCommand.savedDataFailedMessage());

        var setSpawnCommand = command.setSpawn();
        cfg.set("SpawnManagementPlus.commands.setSpawn.insufficient_permission_error_message", setSpawnCommand.insufficientPermissionErrorMessage());
        cfg.set("SpawnManagementPlus.commands.setSpawn.saved_data_message", setSpawnCommand.savedDataMessage());
        cfg.set("SpawnManagementPlus.commands.setSpawn.saved_data_failed_message", setSpawnCommand.savedDataFailedMessage());

        var spawnCommand = command.spawn();
        cfg.set("SpawnManagementPlus.commands.spawn.enabled", spawnCommand.enabled());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.seconds", spawnCommand.seconds());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.message_type", spawnCommand.onTeleport().messageType());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.message", spawnCommand.onTeleport().messages());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message_type", spawnCommand.onInterval().messageType());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message", spawnCommand.onInterval().messages());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.cancel", spawnCommand.onMove().enabled());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message_type", spawnCommand.onMove().messageType());
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message", spawnCommand.onMove().messages());
        cfg.set("SpawnManagementPlus.commands.spawn.insufficient_permission_error_message", spawnCommand.insufficientPermissionErrorMessage());
        cfg.set("SpawnManagementPlus.commands.spawn.location.world", spawnCommand.world());
        cfg.set("SpawnManagementPlus.commands.spawn.location.x", spawnCommand.x());
        cfg.set("SpawnManagementPlus.commands.spawn.location.y", spawnCommand.y());
        cfg.set("SpawnManagementPlus.commands.spawn.location.z", spawnCommand.z());
        cfg.set("SpawnManagementPlus.commands.spawn.location.yaw", spawnCommand.yaw());
        cfg.set("SpawnManagementPlus.commands.spawn.location.pitch", spawnCommand.pitch());
    }

    private Respawn loadRespawnSettingsValues(FileConfiguration cfg) {
        var enabled = cfg.getBoolean("SpawnManagementPlus.on_respawn.enabled", false);
        var preferBedLocation = cfg.getBoolean("SpawnManagementPlus.on_respawn.prefer_bed_location", false);
        var preferAnchorLocation = cfg.getBoolean("SpawnManagementPlus.on_respawn.prefer_anchor_location", false);
        var world = cfg.getString("SpawnManagementPlus.on_respawn.location.world", "");
        var x = cfg.getDouble("SpawnManagementPlus.on_respawn.location.x", 0);
        var y = cfg.getDouble("SpawnManagementPlus.on_respawn.location.y", 0);
        var z = cfg.getDouble("SpawnManagementPlus.on_respawn.location.z", 0);
        var yaw = (float) cfg.getDouble("SpawnManagementPlus.on_respawn.location.yaw", 0);
        var pitch = (float) cfg.getDouble("SpawnManagementPlus.on_respawn.location.pitch", 0);
        var messageType = cfg.getString("SpawnManagementPlus.on_respawn.location.message_type", "");
        var messages = cfg.getStringList("SpawnManagementPlus.on_respawn.location.message");
        var skipRespawnScreen = cfg.getBoolean("SpawnManagementPlus.on_respawn.skip_respawn_screen");
        var spreadItemsOnDeath = cfg.getBoolean("SpawnManagementPlus.on_respawn.spread_items_on_death");

        return new Respawn(enabled, skipRespawnScreen, spreadItemsOnDeath, preferBedLocation, preferAnchorLocation, world, x, y, z, yaw, pitch, messageType, messages);
    }

    private void setRespawnValues(FileConfiguration cfg, Respawn respawn) {
        cfg.set("SpawnManagementPlus.on_respawn.enabled", respawn.enabled());
        cfg.set("SpawnManagementPlus.on_respawn.prefer_bed_location", respawn.preferBedLocation());
        cfg.set("SpawnManagementPlus.on_respawn.prefer_anchor_location", respawn.preferAnchorLocation());
        cfg.set("SpawnManagementPlus.on_respawn.location.world", respawn.world());
        cfg.set("SpawnManagementPlus.on_respawn.location.x", respawn.x());
        cfg.set("SpawnManagementPlus.on_respawn.location.y", respawn.y());
        cfg.set("SpawnManagementPlus.on_respawn.location.z", respawn.z());
        cfg.set("SpawnManagementPlus.on_respawn.location.yaw", respawn.yaw());
        cfg.set("SpawnManagementPlus.on_respawn.location.pitch", respawn.pitch());
        cfg.set("SpawnManagementPlus.on_respawn.location.message", respawn.messageContent());
        cfg.set("SpawnManagementPlus.on_respawn.location.message_type", respawn.messageType());
        cfg.set("SpawnManagementPlus.on_respawn.skip_respawn_screen", respawn.skipRespawnScreen());
        cfg.set("SpawnManagementPlus.on_respawn.spread_items_on_death", respawn.spreadItemsOnDeath());
    }

    public ServerJoin getServerJoinSettings() {
        return serverJoinSettings;
    }

    public void setServerJoinSettings(ServerJoin serverJoinSettings) {
        this.serverJoinSettings = serverJoinSettings;
        settingsHaveBeenUpdated = true;
    }

    public WorldJoin getWorldJoinSettings() {
        return worldJoinSettings;
    }

    public void setWorldJoinSettings(WorldJoin worldJoinSettings) {
        this.worldJoinSettings = worldJoinSettings;
        settingsHaveBeenUpdated = true;
    }

    public Command getCommandSettings() {
        return command;
    }

    public void setCommandValues(Command command) {
        this.command = command;
        settingsHaveBeenUpdated = true;
    }

    public Respawn getRespawnSettings() {
        return respawn;
    }

    public void setRespawnSettings(Respawn respawn) {
        this.respawn = respawn;
    }
}
