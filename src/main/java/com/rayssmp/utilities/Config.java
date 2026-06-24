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
    private FirstJoin firstJoinSettings = new FirstJoin(false, "", false, "Sound.GLASS", 0, 0, false, 0, 0, 0, 0f, 0f, false, null, "", "", "");
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

                cfg.load(configFileLocation);

                boolean enabled = cfg.getBoolean("joins.first_join.enabled", false);
                String world = cfg.getString("joins.first_join.action.world", "");
                boolean useWorldDefault = cfg.getBoolean("joins.first_join.action.location.use_world_default", false);
                double locationX = cfg.getDouble("joins.first_join.action.location.x", 0);
                double locationY = cfg.getDouble("joins.first_join.action.location.y", 0);
                double locationZ = cfg.getDouble("joins.first_join.action.location.z", 0);
                float locationYaw = (float) cfg.getDouble("joins.first_join.action.location.yaw", 0);
                float locationPitch = (float) cfg.getDouble("joins.first_join.action.location.pitch", 0);
                boolean messageEnabled = cfg.getBoolean("joins.first_join.action.message.enabled", false);
                List<String> messageContents = cfg.getStringList("joins.first_join.action.message.content");
                boolean soundEnabled = cfg.getBoolean("joins.first_join.action.sound.enabled", false);
                String soundType = cfg.getString("joins.first_join.action.sound.type", "Sound.GLASS");
                float soundVolume = (float) cfg.getDouble("joins.first_join.action.sound.volume", 0);
                float soundPitch = (float) cfg.getDouble("joins.first_join.action.sound.pitch", 0);
                String firstJoinLocationCommandError = cfg.getString("joins.commands.setfirstjoinlocation.insufficient_permission_error_message", "&csetfirstjoinlocation");
                String savedDataMessage = cfg.getString("joins.commands.setfirstjoinlocation.saved_data_message", "&cLocation was saved successfully");
                String savedDataFailedMessage = cfg.getString("joins.commands.setfirstjoinlocation.saved_data_failed_message", "&cLocation save failed!");

                firstJoinSettings = new FirstJoin(enabled, world, soundEnabled, soundType, soundVolume,
                        soundPitch, useWorldDefault, locationX, locationY, locationZ, locationYaw, locationPitch,
                        messageEnabled, messageContents, firstJoinLocationCommandError, savedDataMessage, savedDataFailedMessage);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() throws IOException {
        if (!settingsHaveBeenUpdated){
            return;
        }

        FirstJoin dataToWrite = this.firstJoinSettings;

        cfg.set("joins.first_join.enabled", dataToWrite.enabled);
        cfg.get("joins.first_join.action.world", dataToWrite.world);
        cfg.get("joins.first_join.action.location.use_world_default", dataToWrite.useWorldDefault);
        cfg.get("joins.first_join.action.location.x", dataToWrite.x);
        cfg.get("joins.first_join.action.location.y", dataToWrite.y);
        cfg.get("joins.first_join.action.location.z", dataToWrite.z);
        cfg.get("joins.first_join.action.location.yaw", dataToWrite.yaw);
        cfg.get("joins.first_join.action.location.pitch", dataToWrite.pitch);
        cfg.get("joins.first_join.action.message.enabled", dataToWrite.messageEnabled);
        cfg.get("joins.first_join.action.message.content", dataToWrite.messageContents);
        cfg.get("joins.first_join.action.sound.enabled", dataToWrite.soundEnabled);
        cfg.get("joins.first_join.action.sound.type", dataToWrite.soundType);
        cfg.get("joins.first_join.action.sound.volume", dataToWrite.soundVolume);
        cfg.get("joins.first_join.action.sound.pitch", dataToWrite.pitch);
        cfg.get("joins.commands.setfirstjoinlocation.insufficient_permission_error_message", dataToWrite.firstJoinLocationCommandError);
        cfg.get("joins.commands.setfirstjoinlocation.saved_data_message", dataToWrite.savedDataMessage);
        cfg.get("joins.commands.setfirstjoinlocation.saved_data_failed_message", dataToWrite.savedDataFailedMessage);

        cfg.save(configFileLocation);
        settingsHaveBeenUpdated = false;
    }

    public FirstJoin firstJoinSettings() {
        return firstJoinSettings;
    }

    public void setFirstJoinSettings(FirstJoin firstJoinSettings) {
        this.firstJoinSettings = firstJoinSettings;
        settingsHaveBeenUpdated = true;
    }

    public record FirstJoin(boolean enabled, String world, boolean soundEnabled, String soundType, float soundVolume,
                            float soundPitch, boolean useWorldDefault, double x, double y,
                            double z, float yaw, float pitch, boolean messageEnabled, List<String> messageContents,
                            String firstJoinLocationCommandError, String savedDataMessage, String savedDataFailedMessage) {
    }
}
