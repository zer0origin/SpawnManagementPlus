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
    private FirstJoin firstJoinSettings = new FirstJoin();
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
            firstJoinSettings = loadFirstJoinValues(cfg);
            worldJoinSettings = loadWorldJoinValues(cfg);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() throws IOException {
        if (!settingsHaveBeenUpdated) {
            return;
        }

        setFirstJoinValues(cfg, this.firstJoinSettings);
        setWorldJoinValues(cfg, this.worldJoinSettings);

        cfg.save(configFileLocation);
        settingsHaveBeenUpdated = false;
    }

    private FirstJoin loadFirstJoinValues(FileConfiguration cfg) {
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
        List<String> messageContents = cfg.getStringList("SpawnManagementPlus.on_server_join.action.message.content");
        boolean soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.sound.enabled", false);
        String soundType = cfg.getString("SpawnManagementPlus.on_server_join.action.sound.type", "Sound.GLASS");
        float soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.volume", 0);
        float soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.action.sound.pitch", 0);
        String firstJoinLocationCommandError = cfg.getString("SpawnManagementPlus.on_server_join.commands.setfirstjoinlocation.insufficient_permission_error_message", "&csetfirstjoinlocation");
        String savedDataMessage = cfg.getString("SpawnManagementPlus.on_server_join.commands.setfirstjoinlocation.saved_data_message", "&cLocation was saved successfully");
        String savedDataFailedMessage = cfg.getString("SpawnManagementPlus.on_server_join.commands.setfirstjoinlocation.saved_data_failed_message", "&cLocation save failed!");

        return new FirstJoin(enabled, onlyOnFirstTime, world, soundEnabled, soundType, soundVolume,
                soundPitch, useWorldDefault, locationX, locationY, locationZ, locationYaw, locationPitch,
                messageEnabled, messageContents, firstJoinLocationCommandError, savedDataMessage, savedDataFailedMessage);
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

    private void setFirstJoinValues(FileConfiguration cfg, FirstJoin firstJoin) {
        cfg.set("SpawnManagementPlus.on_server_join.enabled", firstJoin.enabled);
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time", firstJoin.onlyOnFirstTime);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.world", firstJoin.world);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.use_world_default", firstJoin.useWorldDefault);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.x", firstJoin.x);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.y", firstJoin.y);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.z", firstJoin.z);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.yaw", firstJoin.yaw);
        cfg.set("SpawnManagementPlus.on_server_join.action.location.pitch", firstJoin.pitch);
        cfg.set("SpawnManagementPlus.on_server_join.action.message.enabled", firstJoin.messageEnabled);
        cfg.set("SpawnManagementPlus.on_server_join.action.message.content", firstJoin.messageContents);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.enabled", firstJoin.soundEnabled);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.type", firstJoin.soundType);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.volume", firstJoin.soundVolume);
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.pitch", firstJoin.soundPitch);
        cfg.set("SpawnManagementPlus.commands.setfirstjoinlocation.insufficient_permission_error_message", firstJoin.firstJoinLocationCommandError);
        cfg.set("SpawnManagementPlus.commands.setfirstjoinlocation.saved_data_message", firstJoin.savedDataMessage);
        cfg.set("SpawnManagementPlus.commands.setfirstjoinlocation.saved_data_failed_message", firstJoin.savedDataFailedMessage);
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

    public FirstJoin firstJoinSettings() {
        return firstJoinSettings;
    }

    public void setFirstJoinSettings(FirstJoin firstJoinSettings) {
        this.firstJoinSettings = firstJoinSettings;
        settingsHaveBeenUpdated = true;
    }

    public WorldJoin worldJoinSettings() {
        return worldJoinSettings;
    }

    public void setWorldJoinSettings(WorldJoin worldJoinSettings) {
        this.worldJoinSettings = worldJoinSettings;
        settingsHaveBeenUpdated = true;
    }

    public record FirstJoin(boolean enabled, boolean onlyOnFirstTime, String world, boolean soundEnabled,
                            String soundType, float soundVolume,
                            float soundPitch, boolean useWorldDefault, double x, double y,
                            double z, float yaw, float pitch, boolean messageEnabled, List<String> messageContents,
                            String firstJoinLocationCommandError, String savedDataMessage,
                            String savedDataFailedMessage) {
        public FirstJoin() {
            this(false, false, "", false, "", 0, 0, false, 0, 0, 0, 0, 0, false, null, "", "", "");
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
