package com.rayssmp.utilities;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class Config {
    private final File configFileLocation = new File("plugins/SpawnManagementPlus", "config.yml");
    private final FileConfiguration cfg = new YamlConfiguration();
    private FirstJoin firstJoinSettings = new FirstJoin();
    private WorldJoin worldJoinSettings = new WorldJoin();
    private boolean settingsHaveBeenUpdated = false;

    public void createOrLoad() {
        if (!configFileLocation.exists()) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            try (InputStream is = Objects.requireNonNull(classloader.getResourceAsStream("config.yml"), "Unable to write config file!");
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
        boolean enabled = cfg.getBoolean("joins.on_server_join.enabled", false);
        boolean onlyOnFirstTime = cfg.getBoolean("joins.on_server_join.only_on_first_time", false);
        String world = cfg.getString("joins.on_server_join.action.location.world", "");
        boolean useWorldDefault = cfg.getBoolean("joins.on_server_join.action.location.use_world_default", false);
        double locationX = cfg.getDouble("joins.on_server_join.action.location.x", 0);
        double locationY = cfg.getDouble("joins.on_server_join.action.location.y", 0);
        double locationZ = cfg.getDouble("joins.on_server_join.action.location.z", 0);
        float locationYaw = (float) cfg.getDouble("joins.on_server_join.action.location.yaw", 0);
        float locationPitch = (float) cfg.getDouble("joins.on_server_join.action.location.pitch", 0);
        boolean messageEnabled = cfg.getBoolean("joins.on_server_join.action.message.enabled", false);
        List<String> messageContents = cfg.getStringList("joins.on_server_join.action.message.content");
        boolean soundEnabled = cfg.getBoolean("joins.on_server_join.action.sound.enabled", false);
        String soundType = cfg.getString("joins.on_server_join.action.sound.type", "Sound.GLASS");
        float soundVolume = (float) cfg.getDouble("joins.on_server_join.action.sound.volume", 0);
        float soundPitch = (float) cfg.getDouble("joins.on_server_join.action.sound.pitch", 0);
        String firstJoinLocationCommandError = cfg.getString("joins.on_server_join.commands.setfirstjoinlocation.insufficient_permission_error_message", "&csetfirstjoinlocation");
        String savedDataMessage = cfg.getString("joins.on_server_join.commands.setfirstjoinlocation.saved_data_message", "&cLocation was saved successfully");
        String savedDataFailedMessage = cfg.getString("joins.on_server_join.commands.setfirstjoinlocation.saved_data_failed_message", "&cLocation save failed!");

        return new FirstJoin(enabled, onlyOnFirstTime, world, soundEnabled, soundType, soundVolume,
                soundPitch, useWorldDefault, locationX, locationY, locationZ, locationYaw, locationPitch,
                messageEnabled, messageContents, firstJoinLocationCommandError, savedDataMessage, savedDataFailedMessage);
    }

    private WorldJoin loadWorldJoinValues(FileConfiguration cfg) {
        boolean enabled = cfg.getBoolean("joins.on_world_join.enabled", false);
        List<String> exclude = cfg.getStringList("joins.on_world_join.exclude");
        boolean soundEnabled = cfg.getBoolean("joins.on_world_join.action.sound.enabled", false);
        String soundType = cfg.getString("joins.on_world_join.action.sound.type", "Sound.GLASS");
        float soundVolume = (float) cfg.getDouble("joins.on_world_join.action.sound.volume", 0);
        float soundPitch = (float) cfg.getDouble("joins.on_world_join.action.sound.pitch", 0);
        float locationYaw = (float) cfg.getDouble("joins.on_world_join.action.location.yaw", 0);
        float locationPitch = (float) cfg.getDouble("joins.on_world_join.action.location.pitch", 0);
        boolean messageEnabled = cfg.getBoolean("joins.on_world_join.action.message.enabled", false);
        List<String> messageContents = cfg.getStringList("joins.on_world_join.action.message.content");

        return new WorldJoin(enabled, exclude, soundEnabled, soundType, soundVolume, soundPitch, locationYaw,
                locationPitch, messageEnabled, messageContents);
    }

    private void setFirstJoinValues(FileConfiguration cfg, FirstJoin firstJoin) {
        cfg.set("joins.on_server_join.enabled", firstJoin.enabled);
        cfg.set("joins.on_server_join.only_on_first_time", firstJoin.onlyOnFirstTime);
        cfg.set("joins.on_server_join.action.location.world", firstJoin.world);
        cfg.set("joins.on_server_join.action.location.use_world_default", firstJoin.useWorldDefault);
        cfg.set("joins.on_server_join.action.location.x", firstJoin.x);
        cfg.set("joins.on_server_join.action.location.y", firstJoin.y);
        cfg.set("joins.on_server_join.action.location.z", firstJoin.z);
        cfg.set("joins.on_server_join.action.location.yaw", firstJoin.yaw);
        cfg.set("joins.on_server_join.action.location.pitch", firstJoin.pitch);
        cfg.set("joins.on_server_join.action.message.enabled", firstJoin.messageEnabled);
        cfg.set("joins.on_server_join.action.message.content", firstJoin.messageContents);
        cfg.set("joins.on_server_join.action.sound.enabled", firstJoin.soundEnabled);
        cfg.set("joins.on_server_join.action.sound.type", firstJoin.soundType);
        cfg.set("joins.on_server_join.action.sound.volume", firstJoin.soundVolume);
        cfg.set("joins.on_server_join.action.sound.pitch", firstJoin.pitch);
        cfg.set("joins.commands.setfirstjoinlocation.insufficient_permission_error_message", firstJoin.firstJoinLocationCommandError);
        cfg.set("joins.commands.setfirstjoinlocation.saved_data_message", firstJoin.savedDataMessage);
        cfg.set("joins.commands.setfirstjoinlocation.saved_data_failed_message", firstJoin.savedDataFailedMessage);
    }

    private void setWorldJoinValues(FileConfiguration cfg, WorldJoin worldJoin) {
        cfg.set("joins.on_world_join.enabled", worldJoin.enabled);
        cfg.set("joins.on_world_join.exclude", worldJoin.exclude);
        cfg.set("joins.on_world_join.action.sound.enabled", worldJoin.soundEnabled);
        cfg.set("joins.on_world_join.action.sound.type", worldJoin.soundType);
        cfg.set("joins.on_world_join.action.sound.volume", worldJoin.soundVolume);
        cfg.set("joins.on_world_join.action.sound.pitch", worldJoin.soundPitch);
        cfg.set("joins.on_world_join.action.location.yaw", worldJoin.yaw);
        cfg.set("joins.on_world_join.action.location.pitch", worldJoin.pitch);
        cfg.set("joins.on_world_join.action.message.enabled", worldJoin.messageEnabled);
        cfg.set("joins.on_world_join.action.message.content", worldJoin.messageContents);
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
