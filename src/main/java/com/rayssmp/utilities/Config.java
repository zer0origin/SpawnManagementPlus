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
    private FirstJoin firstJoin = new FirstJoin(false, "", false, 0, 0, 0, false, null);

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
                boolean messageEnabled = cfg.getBoolean("joins.first_join.action.message.enabled", false);
                List<String> messageContents = cfg.getStringList("joins.first_join.action.message.content");
                firstJoin = new FirstJoin(enabled, world, useWorldDefault, locationX, locationY, locationZ, messageEnabled, messageContents);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public FirstJoin firstJoinSettings() {
        return firstJoin;
    }

    public record FirstJoin(boolean enabled, String world, boolean useWorldDefault, double x, double y,
                            double z, boolean messageEnabled, List<String> messageContents) {
    }
}
