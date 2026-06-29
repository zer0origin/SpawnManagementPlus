package com.rayssmp.utilities.config;

import com.rayssmp.utilities.config.command.CommandConfig;
import com.rayssmp.utilities.config.command.Smp;
import com.rayssmp.utilities.config.command.spawn.SpawnConfig;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class Config {
    private final File configFileLocation = new File("plugins/SpawnManagementPlus", "config.yml");
    private FileConfiguration cfg = new YamlConfiguration();
    private ServerJoinConfig serverJoinConfigSettings = new ServerJoinConfig();
    private WorldJoinConfig worldJoinConfigSettings = new WorldJoinConfig();
    private CommandConfig commandConfig = new CommandConfig();
    private RespawnConfig respawnConfig = new RespawnConfig();
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
            serverJoinConfigSettings = loadServerJoinValues(cfg);
            worldJoinConfigSettings = loadWorldJoinValues(cfg);
            commandConfig = loadCommandSettingValues(cfg);
            respawnConfig = loadRespawnSettingsValues(cfg);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() throws IOException {
        if (!settingsHaveBeenUpdated) {
            return;
        }

        setServerJoinValues(cfg, this.serverJoinConfigSettings);
        setWorldJoinValues(cfg, this.worldJoinConfigSettings);
        setCommandValues(cfg, this.commandConfig);
        setRespawnValues(cfg, this.respawnConfig);

        cfg.save(configFileLocation);
        settingsHaveBeenUpdated = false;
    }

    private ServerJoinConfig loadServerJoinValues(FileConfiguration cfg) {
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
        var messageType = cfg.getString("SpawnManagementPlus.on_server_join.action.message_type", "CHAT");
        var soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.sound.enabled", false);
        var soundType = cfg.getString("SpawnManagementPlus.on_server_join.action.sound.type", "Sound.GLASS");
        var soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.volume", 0);
        var soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.pitch", 0);
        var exclude = cfg.getStringList("SpawnManagementPlus.on_server_join.exclude");

        return new ServerJoinConfig(enabled, onlyOnFirstTime, world, soundEnabled, soundType, soundVolume, soundPitch, useWorldDefault, locationX, locationY, locationZ, locationYaw, locationPitch, messageType, messageContents, exclude);
    }

    private void setServerJoinValues(FileConfiguration cfg, ServerJoinConfig serverJoinConfig) {
        cfg.set("SpawnManagementPlus.on_server_join.enabled", serverJoinConfig.enabled());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time", serverJoinConfig.onlyOnFirstTime());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.world", serverJoinConfig.world());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.use_world_default", serverJoinConfig.useWorldDefault());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.x", serverJoinConfig.x());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.y", serverJoinConfig.y());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.z", serverJoinConfig.z());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.yaw", serverJoinConfig.yaw());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.pitch", serverJoinConfig.pitch());
        cfg.set("SpawnManagementPlus.on_server_join.action.message", serverJoinConfig.messageContents());
        cfg.set("SpawnManagementPlus.on_server_join.action.message_type", serverJoinConfig.messageType());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.enabled", serverJoinConfig.soundEnabled());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.type", serverJoinConfig.soundType());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.volume", serverJoinConfig.soundVolume());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.pitch", serverJoinConfig.soundPitch());
        cfg.set("SpawnManagementPlus.on_server_join.exclude", serverJoinConfig.exclude());
    }

    private WorldJoinConfig loadWorldJoinValues(FileConfiguration cfg) {
        var enabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.enabled", false);
        var exclude = cfg.getStringList("SpawnManagementPlus.on_world_join.exclude");
        var soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.action.sound.enabled", false);
        var soundType = cfg.getString("SpawnManagementPlus.on_world_join.action.sound.type", "Sound.GLASS");
        var soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.volume", 0);
        var soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.pitch", 0);
        var locationYaw = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.yaw", 0);
        var locationPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.pitch", 0);
        var messageType = cfg.getString("SpawnManagementPlus.on_world_join.action.message_type", "CHAT");
        var messageContents = cfg.getStringList("SpawnManagementPlus.on_world_join.action.message");

        return new WorldJoinConfig(enabled, exclude, soundEnabled, soundType, soundVolume, soundPitch, locationYaw, locationPitch, messageType, messageContents);
    }

    private void setWorldJoinValues(FileConfiguration cfg, WorldJoinConfig worldJoinConfig) {
        cfg.set("SpawnManagementPlus.on_world_join.enabled", worldJoinConfig.enabled());
        cfg.set("SpawnManagementPlus.on_world_join.exclude", worldJoinConfig.exclude());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.enabled", worldJoinConfig.soundEnabled());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.type", worldJoinConfig.soundType());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.volume", worldJoinConfig.soundVolume());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.pitch", worldJoinConfig.soundPitch());
        cfg.set("SpawnManagementPlus.on_world_join.action.location.yaw", worldJoinConfig.yaw());
        cfg.set("SpawnManagementPlus.on_world_join.action.location.pitch", worldJoinConfig.pitch());
        cfg.set("SpawnManagementPlus.on_world_join.action.message_type", worldJoinConfig.messageType());
        cfg.set("SpawnManagementPlus.on_world_join.action.message", worldJoinConfig.messageContents());
    }

    private CommandConfig loadCommandSettingValues(FileConfiguration cfg) {
        boolean enabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.enabled", false);
        int seconds = cfg.getInt("SpawnManagementPlus.commands.spawn.cooldown_timer.seconds", -1);
        String onTeleportMessageType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.message_type", "CHAT");
        List<String> onTeleportMessages = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.message");
        String onIntervalMessageType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message_type", "CHAT");
        List<String> onIntervalMessages = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message");
        boolean onMoveCancel = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.cancel", false);
        String onMoveMessageType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message_type", "CHAT");
        List<String> onMoveMessages = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message");
        String spawnInsufficientPermissionErrorMessage = cfg.getString("SpawnManagementPlus.commands.spawn.insufficient_permission_error_message", "");
        String youAreAlreadyTeleporting = cfg.getString("SpawnManagementPlus.commands.spawn.you_are_already_teleporting", "");

        boolean useServerJoinLocation = cfg.getBoolean("SpawnManagementPlus.commands.spawn.use_on_server_join_location", true);
        String world = cfg.getString("SpawnManagementPlus.commands.spawn.location.world", "");
        double x = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.x", 0);
        double y = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.y", 0);
        double z = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.z", 0);
        float yaw = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.location.yaw", 0);
        float pitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.location.pitch", 0);

        boolean onTeleportSoundEnabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.enabled", false);
        String onTeleportSoundType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.type", "");
        float onTeleportSoundVolume = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.volume", 1);
        float onTeleportSoundPitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport.sound.pitch", 1);

        boolean onIntervalSoundEnabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.enabled", false);
        String onIntervalSoundType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.type", "");
        float onIntervalSoundVolume = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.volume", 1);
        float onIntervalSoundPitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.sound.pitch", 1);

        boolean onMoveSoundEnabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.enabled", false);
        String onMoveSoundType = cfg.getString("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.type", "");
        float onMoveSoundVolume = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.volume", 1);
        float onMoveSoundPitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.sound.pitch", 1);

        SpawnConfig spawnConfig;
        if (!useServerJoinLocation) {
            spawnConfig = SpawnConfig.SpawnFactory(enabled, seconds, spawnInsufficientPermissionErrorMessage, youAreAlreadyTeleporting, false, world, x, y, z, yaw, pitch,
                    onTeleportMessageType, onTeleportMessages, onTeleportSoundEnabled, onTeleportSoundType,
                    onTeleportSoundVolume, onTeleportSoundPitch, onIntervalMessageType, onIntervalMessages,
                    onIntervalSoundEnabled, onIntervalSoundType, onIntervalSoundVolume, onIntervalSoundPitch, onMoveCancel,
                    onMoveMessageType, onMoveSoundEnabled, onMoveSoundType, onMoveSoundVolume, onMoveSoundPitch, onMoveMessages);
        } else {
            spawnConfig = SpawnConfig.SpawnFactory(enabled, seconds, spawnInsufficientPermissionErrorMessage, youAreAlreadyTeleporting, true, serverJoinConfigSettings.world(), serverJoinConfigSettings.x(), serverJoinConfigSettings.y(), serverJoinConfigSettings.z(), serverJoinConfigSettings.yaw(), serverJoinConfigSettings.pitch(),
                    onTeleportMessageType, onTeleportMessages, onTeleportSoundEnabled, onTeleportSoundType,
                    onTeleportSoundVolume, onTeleportSoundPitch, onIntervalMessageType, onIntervalMessages,
                    onIntervalSoundEnabled, onIntervalSoundType, onIntervalSoundVolume, onIntervalSoundPitch, onMoveCancel,
                    onMoveMessageType, onMoveSoundEnabled, onMoveSoundType, onMoveSoundVolume, onMoveSoundPitch, onMoveMessages);
        }

        var smpInsufficientPermissionErrorMessage = cfg.getString("SpawnManagementPlus.commands.smp.insufficient_permission_error_message", "");
        var smpSavedDataMessage = cfg.getString("SpawnManagementPlus.commands.smp.saved_data_message", "");
        var smpSavedDataFailedMessage = cfg.getString("SpawnManagementPlus.commands.smp.saved_data_failed_message", "");
        Smp smp = new Smp(smpInsufficientPermissionErrorMessage, smpSavedDataMessage, smpSavedDataFailedMessage);

        return new CommandConfig(smp, spawnConfig);
    }

    private void setCommandValues(FileConfiguration cfg, CommandConfig commandConfig) {
        var spawnCommand = commandConfig.spawnConfig();
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
        cfg.set("SpawnManagementPlus.commands.spawn.you_are_already_teleporting", spawnCommand.youAreAlreadyTeleporting());
        cfg.set("SpawnManagementPlus.commands.spawn.location.world", spawnCommand.world());
        cfg.set("SpawnManagementPlus.commands.spawn.location.x", spawnCommand.x());
        cfg.set("SpawnManagementPlus.commands.spawn.location.y", spawnCommand.y());
        cfg.set("SpawnManagementPlus.commands.spawn.location.z", spawnCommand.z());
        cfg.set("SpawnManagementPlus.commands.spawn.location.yaw", spawnCommand.yaw());
        cfg.set("SpawnManagementPlus.commands.spawn.location.pitch", spawnCommand.pitch());
    }

    private RespawnConfig loadRespawnSettingsValues(FileConfiguration cfg) {
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
        var skipRespawnScreen = cfg.getBoolean("SpawnManagementPlus.on_respawn.skip_respawn_screen", false);
        var spreadItemsOnDeath = cfg.getBoolean("SpawnManagementPlus.on_respawn.spread_items_on_death", false);
        var forceRespawnButKeepDefaultMessage = cfg.getBoolean("SpawnManagementPlus.on_respawn.force_respawn_but_keep_default_message", false);

        return new RespawnConfig(enabled, skipRespawnScreen, forceRespawnButKeepDefaultMessage, spreadItemsOnDeath, preferBedLocation, preferAnchorLocation, world, x, y, z, yaw, pitch, messageType, messages);
    }

    private void setRespawnValues(FileConfiguration cfg, RespawnConfig respawnConfig) {
        cfg.set("SpawnManagementPlus.on_respawn.enabled", respawnConfig.enabled());
        cfg.set("SpawnManagementPlus.on_respawn.prefer_bed_location", respawnConfig.preferBedLocation());
        cfg.set("SpawnManagementPlus.on_respawn.prefer_anchor_location", respawnConfig.preferAnchorLocation());
        cfg.set("SpawnManagementPlus.on_respawn.location.world", respawnConfig.world());
        cfg.set("SpawnManagementPlus.on_respawn.location.x", respawnConfig.x());
        cfg.set("SpawnManagementPlus.on_respawn.location.y", respawnConfig.y());
        cfg.set("SpawnManagementPlus.on_respawn.location.z", respawnConfig.z());
        cfg.set("SpawnManagementPlus.on_respawn.location.yaw", respawnConfig.yaw());
        cfg.set("SpawnManagementPlus.on_respawn.location.pitch", respawnConfig.pitch());
        cfg.set("SpawnManagementPlus.on_respawn.location.message", respawnConfig.messageContent());
        cfg.set("SpawnManagementPlus.on_respawn.location.message_type", respawnConfig.messageType());
        cfg.set("SpawnManagementPlus.on_respawn.skip_respawn_screen", respawnConfig.skipRespawnScreen());
        cfg.set("SpawnManagementPlus.on_respawn.spread_items_on_death", respawnConfig.spreadItemsOnDeath());
        cfg.set("SpawnManagementPlus.on_respawn.force_respawn_but_keep_default_message", respawnConfig.forceRespawnButKeepDefaultMessage());
    }

    public ServerJoinConfig getServerJoinSettings() {
        return serverJoinConfigSettings;
    }

    public void setServerJoinSettings(ServerJoinConfig serverJoinConfigSettings) {
        this.serverJoinConfigSettings = serverJoinConfigSettings;
        settingsHaveBeenUpdated = true;
    }

    public WorldJoinConfig getWorldJoinSettings() {
        return worldJoinConfigSettings;
    }

    public void setWorldJoinSettings(WorldJoinConfig worldJoinConfigSettings) {
        this.worldJoinConfigSettings = worldJoinConfigSettings;
        settingsHaveBeenUpdated = true;
    }

    public CommandConfig getCommandSettings() {
        return commandConfig;
    }

    public void setCommandValues(CommandConfig commandConfig) {
        this.commandConfig = commandConfig;
        settingsHaveBeenUpdated = true;
    }

    public RespawnConfig getRespawnSettings() {
        return respawnConfig;
    }

    public void setRespawnSettings(RespawnConfig respawnConfig) {
        this.respawnConfig = respawnConfig;
    }
}
