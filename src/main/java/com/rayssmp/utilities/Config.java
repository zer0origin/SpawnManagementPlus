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
    private boolean settingsHaveBeenUpdated = false;

    public void load() throws IOException {
        if (!configFileLocation.exists()) {
            configFileLocation.createNewFile();
            try (InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("config.yml"), "Unable to write config file!");
                 OutputStream output = new FileOutputStream(configFileLocation)) {

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

        cfg.save(configFileLocation);
        settingsHaveBeenUpdated = false;
    }

    private ServerJoin loadServerJoinValues(FileConfiguration cfg) {
        boolean enabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.enabled", false);
        boolean onlyOnFirstTime = cfg.getBoolean("SpawnManagementPlus.on_server_join.only_on_first_time", false);
        String world = cfg.getString("SpawnManagementPlus.on_server_join.action.location.world", "");
        boolean useWorldDefault = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.location.use_world_default", false);
        double locationX = cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.x", 0);
        double locationY = cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.y", 0);
        double locationZ = cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.z", 0);
        float locationYaw = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.yaw", 0);
        float locationPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.location.pitch", 0);
        boolean messageEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.message.enabled", false);
        boolean messageFirstTimeOnly = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.message.only_on_first_time");
        List<String> messageContents = cfg.getStringList("SpawnManagementPlus.on_server_join.action.message.content");
        boolean soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.sound.enabled", false);
        String soundType = cfg.getString("SpawnManagementPlus.on_server_join.action.sound.type", "Sound.GLASS");
        float soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.volume", 0);
        float soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.pitch", 0);
        String firstJoinLocationCommandError = cfg.getString("SpawnManagementPlus.on_server_join.commands.joinlocation.insufficient_permission_error_message", "&cjoinlocation failed");
        String savedDataMessage = cfg.getString("SpawnManagementPlus.on_server_join.commands.joinlocation.saved_data_message", "&cLocation was saved successfully");
        String savedDataFailedMessage = cfg.getString("SpawnManagementPlus.on_server_join.commands.joinlocation.saved_data_failed_message", "&cLocation save failed!");
        List<String> exclude = cfg.getStringList("SpawnManagementPlus.on_server_join.exclude");


        return new ServerJoin(enabled, onlyOnFirstTime, world, soundEnabled, soundType, soundVolume,
                soundPitch, useWorldDefault, locationX, locationY, locationZ, locationYaw, locationPitch,
                messageEnabled, messageFirstTimeOnly, messageContents, firstJoinLocationCommandError, savedDataMessage, savedDataFailedMessage, exclude);
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
        cfg.set("SpawnManagementPlus.commands.joinlocation.insufficient_permission_error_message", serverJoin.firstJoinLocationCommandError);
        cfg.set("SpawnManagementPlus.commands.joinlocation.saved_data_message", serverJoin.savedDataMessage);
        cfg.set("SpawnManagementPlus.commands.joinlocation.saved_data_failed_message", serverJoin.savedDataFailedMessage);
        cfg.set("SpawnManagementPlus.on_server_join.exclude", serverJoin.exclude);
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

    public ServerJoin serverJoinSettings() {
        return serverJoinSettings;
    }

    public void setServerJoinSettings(ServerJoin serverJoinSettings) {
        this.serverJoinSettings = serverJoinSettings;
        settingsHaveBeenUpdated = true;
    }

    public WorldJoin worldJoinSettings() {
        return worldJoinSettings;
    }

    public void setWorldJoinSettings(WorldJoin worldJoinSettings) {
        this.worldJoinSettings = worldJoinSettings;
        settingsHaveBeenUpdated = true;
    }

    public record ServerJoin(boolean enabled, boolean onlyOnFirstTime, String world, boolean soundEnabled,
                             String soundType, float soundVolume,
                             float soundPitch, boolean useWorldDefault, double x, double y,
                             double z, float yaw, float pitch, boolean messageEnabled, boolean messageFirstTimeOnly, List<String> messageContents,
                             String firstJoinLocationCommandError, String savedDataMessage,
                             String savedDataFailedMessage, List<String> exclude) {
        public ServerJoin() {
            this(false, false, "", false, "", 0, 0, false, 0, 0, 0, 0, 0, false, false, null, "", "", "", null);
        }
    }

    public record WorldJoin(boolean enabled, List<String> exclude, boolean soundEnabled, String soundType,
                            float soundVolume, float soundPitch, float yaw, float pitch, boolean messageEnabled,
                            List<String> messageContents) {
        public WorldJoin() {
            this(false, null, false, "", 0, 0, 0, 0, false, null);
        }
    }
}
