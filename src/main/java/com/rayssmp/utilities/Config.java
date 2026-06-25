package com.rayssmp.utilities;

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
    private CommandSettings commandSettings = new CommandSettings();
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
            commandSettings = loadCommandSettingValues(cfg);
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
        setCommandSettings(cfg, this.commandSettings);

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
        var messageEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.message.enabled", false);
        var messageFirstTimeOnly = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.message.only_on_first_time");
        var messageContents = cfg.getStringList("SpawnManagementPlus.on_server_join.action.message.content");
        var soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.sound.enabled", false);
        var soundType = cfg.getString("SpawnManagementPlus.on_server_join.action.sound.type", "Sound.GLASS");
        var soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.volume", 0);
        var soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.pitch", 0);
        var exclude = cfg.getStringList("SpawnManagementPlus.on_server_join.exclude");

        return new ServerJoin(enabled, onlyOnFirstTime, world, soundEnabled, soundType, soundVolume, soundPitch,
                useWorldDefault, locationX, locationY, locationZ, locationYaw, locationPitch, messageEnabled,
                messageFirstTimeOnly, messageContents, exclude);
    }

    private void setServerJoinValues(FileConfiguration cfg, ServerJoin serverJoin) {
        cfg.set("SpawnManagementPlus.on_server_join.enabled", serverJoin.enabled);
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time", serverJoin.onlyOnFirstTime);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.world", serverJoin.world);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.use_world_default", serverJoin.useWorldDefault);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.x", serverJoin.x);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.y", serverJoin.y);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.z", serverJoin.z);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.yaw", serverJoin.yaw);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.pitch", serverJoin.pitch);
        cfg.set("SpawnManagementPlus.on_server_join.action.message.enabled", serverJoin.messageEnabled);
        cfg.set("SpawnManagementPlus.on_server_join.action.message.only_on_first_time", serverJoin.messageFirstTimeOnly);
        cfg.set("SpawnManagementPlus.on_server_join.action.message.content", serverJoin.messageContents);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.enabled", serverJoin.soundEnabled);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.type", serverJoin.soundType);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.volume", serverJoin.soundVolume);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.pitch", serverJoin.soundPitch);
        cfg.set("SpawnManagementPlus.on_server_join.exclude", serverJoin.exclude);
    }

    private WorldJoin loadWorldJoinValues(FileConfiguration cfg) {
        boolean enabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.enabled", false);
        List<String> exclude = cfg.getStringList("SpawnManagementPlus.on_world_join.exclude");
        boolean soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.action.sound.enabled", false);
        String soundType = cfg.getString("SpawnManagementPlus.on_world_join.action.sound.type", "Sound.GLASS");
        float soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.volume", 0);
        float soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.pitch", 0);
        float locationYaw = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.yaw", 0);
        float locationPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.pitch", 0);
        boolean messageEnabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.action.message.enabled", false);
        List<String> messageContents = cfg.getStringList("SpawnManagementPlus.on_world_join.action.message.content");

        return new WorldJoin(enabled, exclude, soundEnabled, soundType, soundVolume, soundPitch, locationYaw,
                locationPitch, messageEnabled, messageContents);
    }

    private void setWorldJoinValues(FileConfiguration cfg, WorldJoin worldJoin) {
        cfg.set("SpawnManagementPlus.on_world_join.enabled", worldJoin.enabled);
        cfg.set("SpawnManagementPlus.on_world_join.exclude", worldJoin.exclude);
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.enabled", worldJoin.soundEnabled);
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.type", worldJoin.soundType);
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.volume", worldJoin.soundVolume);
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.pitch", worldJoin.soundPitch);
        cfg.set("SpawnManagementPlus.on_world_join.action.location.yaw", worldJoin.yaw);
        cfg.set("SpawnManagementPlus.on_world_join.action.location.pitch", worldJoin.pitch);
        cfg.set("SpawnManagementPlus.on_world_join.action.message.enabled", worldJoin.messageEnabled);
        cfg.set("SpawnManagementPlus.on_world_join.action.message.content", worldJoin.messageContents);
    }

    private CommandSettings loadCommandSettingValues(FileConfiguration cfg) {
        var firstJoinLocationCommandError = cfg.getString("SpawnManagementPlus.on_server_join.commands.setjoinlocation.insufficient_permission_error_message", "&cjoinlocation failed");
        var spawnPermissionError = cfg.getString("SpawnManagementPlus.on_server_join.commands.setjoinlocation.insufficient_permission_error_message", "&cYou don't have permission to run this command.");
        var savedDataMessage = cfg.getString("SpawnManagementPlus.commands.spawn.insufficient_permission_error_message", "&cLocation was saved successfully");
        var savedDataFailedMessage = cfg.getString("SpawnManagementPlus.on_server_join.commands.setjoinlocation.saved_data_failed_message", "&cLocation save failed!");
        var world = cfg.getString("SpawnManagementPlus.commands.spawn.location.world", "world");
        var x = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.x", 0);
        var y = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.y", 0);
        var z = cfg.getDouble("SpawnManagementPlus.commands.spawn.location.z", 0);
        var yaw = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.location.yaw", 0);
        var pitch = (float) cfg.getDouble("SpawnManagementPlus.commands.spawn.location.pitch", 0);
        var enabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.enabled", false);
        var setSpawnPermissionError = cfg.getString("SpawnManagementPlus.commands.setSpawn.insufficient_permission_error_message", "&cYou don't have permission to run this command.");
        var setSpawnSaved = cfg.getString("SpawnManagementPlus.commands.setSpawn.saved_data_message", "&cLocation was saved successfully");
        var setSpawnFailed = cfg.getString("SpawnManagementPlus.commands.setSpawn.saved_data_failed_message", "&cLocation save failed!");
        var cooldownTimerSeconds = cfg.getInt("SpawnManagementPlus.commands.spawn.cooldown_timer.seconds", -1);
        var cooldownTimerCancelOnMove = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.cancel_on_move", false);
        var coolDownTimerCancelOnMoveMessage = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message");
        var intervalEnabled = cfg.getBoolean("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.enabled", false);
        var intervalMessage = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message");
        var onTeleport = cfg.getStringList("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport");

        return new CommandSettings(enabled, savedDataMessage, savedDataFailedMessage, world, x, y, z, yaw, pitch,
                spawnPermissionError, firstJoinLocationCommandError, setSpawnPermissionError, setSpawnSaved,
                setSpawnFailed, cooldownTimerSeconds, coolDownTimerCancelOnMoveMessage, cooldownTimerCancelOnMove,
                intervalEnabled, intervalMessage, onTeleport);
    }

    private void setCommandSettings(FileConfiguration cfg, CommandSettings commandSettings) {
        cfg.set("SpawnManagementPlus.commands.setjoinlocation.insufficient_permission_error_message", commandSettings.setJoinLocationPermissionError);
        cfg.set("SpawnManagementPlus.commands.spawn.insufficient_permission_error_message", commandSettings.spawnPermissionError);
        cfg.set("SpawnManagementPlus.commands.setjoinlocation.saved_data_message", commandSettings.setJoinLocationSaved);
        cfg.set("SpawnManagementPlus.commands.setSpawn.insufficient_permission_error_message", commandSettings.setSpawnPermissionError);
        cfg.set("SpawnManagementPlus.commands.setSpawn.saved_data_message", commandSettings.setSpawnSaved);
        cfg.set("SpawnManagementPlus.commands.setSpawn.saved_data_failed_message", commandSettings.setSpawnFailed);
        cfg.set("SpawnManagementPlus.commands.setjoinlocation.saved_data_failed_message", commandSettings.setJoinLocationSavedFailed);
        cfg.set("SpawnManagementPlus.commands.spawn.location.world", commandSettings.world);
        cfg.set("SpawnManagementPlus.commands.spawn.location.x", commandSettings.x);
        cfg.set("SpawnManagementPlus.commands.spawn.location.y", commandSettings.y);
        cfg.set("SpawnManagementPlus.commands.spawn.location.z", commandSettings.z);
        cfg.set("SpawnManagementPlus.commands.spawn.location.yaw", commandSettings.yaw);
        cfg.set("SpawnManagementPlus.commands.spawn.location.pitch", commandSettings.pitch);
        cfg.set("SpawnManagementPlus.commands.spawn.enabled", commandSettings.enabled);
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.seconds", commandSettings.cooldownTimerSeconds);
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.cancel", commandSettings.cooldownTimerCancelOnMove);
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_move.message", commandSettings.coolDownTimerCancelOnMoveMessage);
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.enabled", commandSettings.intervalEnabled);
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_interval.message", commandSettings.intervalMessage);
        cfg.set("SpawnManagementPlus.commands.spawn.cooldown_timer.on_teleport", commandSettings.onTeleport);
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

    public CommandSettings getCommandSettings() {
        return commandSettings;
    }

    public void setCommandSettings(CommandSettings commandSettings) {
        this.commandSettings = commandSettings;
        settingsHaveBeenUpdated = true;
    }

    public record ServerJoin(boolean enabled, boolean onlyOnFirstTime, String world, boolean soundEnabled,
                             String soundType, float soundVolume, float soundPitch, boolean useWorldDefault, double x,
                             double y, double z, float yaw, float pitch, boolean messageEnabled,
                             boolean messageFirstTimeOnly, List<String> messageContents, List<String> exclude) {
        public ServerJoin() {
            this(false, false, "", false, "", 0, 0, false, 0, 0, 0, 0, 0, false, false, null, null);
        }
    }

    public record WorldJoin(boolean enabled, List<String> exclude, boolean soundEnabled, String soundType,
                            float soundVolume, float soundPitch, float yaw, float pitch, boolean messageEnabled,
                            List<String> messageContents) {
        public WorldJoin() {
            this(false, null, false, "", 0, 0, 0, 0, false, null);
        }
    }

    public record CommandSettings(boolean enabled, String setJoinLocationSaved, String setJoinLocationSavedFailed,
                                  String world,
                                  double x, double y, double z, float yaw, float pitch, String spawnPermissionError,
                                  String setJoinLocationPermissionError,
                                  String setSpawnPermissionError, String setSpawnSaved, String setSpawnFailed,
                                  int cooldownTimerSeconds, List<String> coolDownTimerCancelOnMoveMessage,
                                  boolean cooldownTimerCancelOnMove, boolean intervalEnabled,
                                  List<String> intervalMessage, List<String> onTeleport) {
        public CommandSettings() {
            this(false, "", "", "", 0, 0, 0, 0, 0, "", "", "", "", "", 0, null, false, false, null, null);
        }
    }
}
