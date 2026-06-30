package com.rayssmp.utilities.config;

import com.rayssmp.utilities.config.command.CommandConfig;
import com.rayssmp.utilities.config.command.OnlyOnFirstTime;
import com.rayssmp.utilities.config.command.Smp;
import com.rayssmp.utilities.config.command.Sound;
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
        var locationEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.location.enabled", false);
        var world = cfg.getString("SpawnManagementPlus.on_server_join.action.location.world", "");
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

        var run = cfg.getString("SpawnManagementPlus.on_server_join.action.run_command.run", "");
        var user = cfg.getString("SpawnManagementPlus.on_server_join.action.run_command.user", "");
        var enabledRun = cfg.getBoolean("SpawnManagementPlus.on_server_join.action.run_command.enabled", false);
        var serverJoinCommand = new RunCommand(enabledRun, run, user);

        var serverJoinLocation = new WorldLocation(locationEnabled, world, locationX, locationY, locationZ, locationYaw, locationPitch);
        var serverJoinSound = new Sound(soundEnabled, soundType, soundVolume, soundPitch);
        var serverJoinAction = new Action(serverJoinLocation, serverJoinSound, serverJoinCommand, messageType, messageContents);

        //only on first time config
        var firstJoinEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.only_on_first_time.enabled", false);
        var firstJoinUseOnServerJoinDefault = cfg.getBoolean("SpawnManagementPlus.on_server_join.only_on_first_time.use_on_server_join_default", false);
        if (firstJoinUseOnServerJoinDefault){
            return new ServerJoinConfig(enabled, exclude, serverJoinAction, new OnlyOnFirstTime(firstJoinEnabled, true, serverJoinAction));
        }

        var firstJoinSoundEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.enabled", false);
        var firstJoinSoundType = cfg.getString("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.type", "");
        var firstJoinSoundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.volume", 0);
        var firstJoinSoundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.pitch", 0);
        var firstJoinLocationEnabled = cfg.getBoolean("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.enabled", false);
        var firstJoinWorld = cfg.getString("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.world", "");
        var firstJoinX = cfg.getDouble("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.x", 0);
        var firstJoinY = cfg.getDouble("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.y", 0);
        var firstJoinZ = cfg.getDouble("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.z", 0);
        var firstJoinYaw = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.yaw", 0);
        var firstJoinPitch = (float) cfg.getDouble("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.pitch", 0);
        var firstJoinMessageType = cfg.getString("SpawnManagementPlus.on_server_join.only_on_first_time.action.message_type", "");
        var firstJoinMessageContents = cfg.getStringList("SpawnManagementPlus.on_server_join.only_on_first_time.action.message");

        var enabled1 = cfg.getBoolean("SpawnManagementPlus.on_server_join.only_on_first_time.action.run_command.enabled", false);
        var run1 = cfg.getString("SpawnManagementPlus.on_server_join.only_on_first_time.action.run_command.run", "");
        var user1 = cfg.getString("SpawnManagementPlus.on_server_join.only_on_first_time.action.user", "");
        var firstTimeCommand = new RunCommand(enabled1, run1, user1);

        WorldLocation firstTimeWorldLocation = new WorldLocation(firstJoinLocationEnabled, firstJoinWorld, firstJoinX, firstJoinY, firstJoinZ, firstJoinYaw, firstJoinPitch);
        Sound firstTimeSound = new Sound(firstJoinSoundEnabled, firstJoinSoundType, firstJoinSoundVolume, firstJoinSoundPitch);
        Action firstTimeAction = new Action(firstTimeWorldLocation, firstTimeSound, firstTimeCommand, firstJoinMessageType, firstJoinMessageContents);
        OnlyOnFirstTime firstTimeConfig = new OnlyOnFirstTime(firstJoinEnabled, false, firstTimeAction);

        return new ServerJoinConfig(enabled, exclude, serverJoinAction, firstTimeConfig);
    }

    private void setServerJoinValues(FileConfiguration cfg, ServerJoinConfig serverJoinConfig) {
        cfg.set("SpawnManagementPlus.on_server_join.enabled", serverJoinConfig.enabled());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.enabled", serverJoinConfig.action().worldLocation().enabled());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.world", serverJoinConfig.action().worldLocation().name());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.x", serverJoinConfig.action().worldLocation().x());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.y", serverJoinConfig.action().worldLocation().y());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.z", serverJoinConfig.action().worldLocation().z());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.yaw", serverJoinConfig.action().worldLocation().yaw());
        cfg.set("SpawnManagementPlus.on_server_join.action.location.pitch", serverJoinConfig.action().worldLocation().pitch());
        cfg.set("SpawnManagementPlus.on_server_join.action.message", serverJoinConfig.action().messageContents());
        cfg.set("SpawnManagementPlus.on_server_join.action.message_type", serverJoinConfig.action().messageType());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.enabled", serverJoinConfig.action().sound().enabled());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.type", serverJoinConfig.action().sound().type());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.volume", serverJoinConfig.action().sound().volume());
        cfg.set("SpawnManagementPlus.on_server_join.action.sound.pitch", serverJoinConfig.action().sound().pitch());
        cfg.set("SpawnManagementPlus.on_server_join.exclude", serverJoinConfig.exclude());
        cfg.set("SpawnManagementPlus.on_server_join.action.run_command.run", serverJoinConfig.action().runCommand().commandToRun());
        cfg.set("SpawnManagementPlus.on_server_join.action.run_command.user", serverJoinConfig.action().runCommand().user());
        cfg.set("SpawnManagementPlus.on_server_join.action.run_command.enabled", serverJoinConfig.action().runCommand().enabled());

        //only on first time config
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.enabled", serverJoinConfig.onlyOnFirstTime().enabled());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.use_on_server_join_default", serverJoinConfig.onlyOnFirstTime().useOnServerJoinDefault());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.enabled", serverJoinConfig.onlyOnFirstTime().action().sound().enabled());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.type", serverJoinConfig.onlyOnFirstTime().action().sound().type());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.volume", serverJoinConfig.onlyOnFirstTime().action().sound().volume());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.sound.pitch", serverJoinConfig.onlyOnFirstTime().action().sound().pitch());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.enabled", serverJoinConfig.onlyOnFirstTime().action().worldLocation().enabled());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.world", serverJoinConfig.onlyOnFirstTime().action().worldLocation().name());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.x", serverJoinConfig.onlyOnFirstTime().action().worldLocation().x());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.y", serverJoinConfig.onlyOnFirstTime().action().worldLocation().y());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.z", serverJoinConfig.onlyOnFirstTime().action().worldLocation().z());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.yaw", serverJoinConfig.onlyOnFirstTime().action().worldLocation().yaw());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.location.pitch", serverJoinConfig.onlyOnFirstTime().action().worldLocation().pitch());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.message_type", serverJoinConfig.onlyOnFirstTime().action().messageType());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.message", serverJoinConfig.onlyOnFirstTime().action().messageContents());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.run_command.run", serverJoinConfig.onlyOnFirstTime().action().runCommand().commandToRun());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.run_command.user", serverJoinConfig.onlyOnFirstTime().action().runCommand().user());
        cfg.set("SpawnManagementPlus.on_server_join.only_on_first_time.action.run_command.enabled", serverJoinConfig.onlyOnFirstTime().action().runCommand().enabled());
    }

    private WorldJoinConfig loadWorldJoinValues(FileConfiguration cfg) {
        var enabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.enabled", false);
        var exclude = cfg.getStringList("SpawnManagementPlus.on_world_join.exclude");
        var soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.action.sound.enabled", false);
        var soundType = cfg.getString("SpawnManagementPlus.on_world_join.action.sound.type", "Sound.GLASS");
        var soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.volume", 0);
        var soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.sound.pitch", 0);
        var locationEnabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.action.location.enabled", false);
        var locationYaw = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.yaw", 0);
        var locationPitch = (float) cfg.getDouble("SpawnManagementPlus.on_world_join.action.location.pitch", 0);
        var messageType = cfg.getString("SpawnManagementPlus.on_world_join.action.message_type", "CHAT");
        var messageContents = cfg.getStringList("SpawnManagementPlus.on_world_join.action.message");

        var run = cfg.getString("SpawnManagementPlus.on_world_join.action.run_command.run", "");
        var user = cfg.getString("SpawnManagementPlus.on_world_join.action.run_command.user", "");
        var runEnabled = cfg.getBoolean("SpawnManagementPlus.on_world_join.action.run_command.enabled", false);
        var command = new RunCommand(runEnabled, run, user);

        WorldLocation worldLocation = new WorldLocation(locationEnabled,null, 0, 0, 0, locationYaw, locationPitch); // World,x,y,z are ignored since WorldJoin will use the world's default values.
        Sound sound = new Sound(soundEnabled, soundType, soundVolume, soundPitch);

        return new WorldJoinConfig(enabled, exclude, new Action(worldLocation, sound, command, messageType, messageContents));
    }

    private void setWorldJoinValues(FileConfiguration cfg, WorldJoinConfig worldJoinConfig) {
        cfg.set("SpawnManagementPlus.on_world_join.enabled", worldJoinConfig.enabled());
        cfg.set("SpawnManagementPlus.on_world_join.exclude", worldJoinConfig.exclude());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.enabled", worldJoinConfig.action().sound().enabled());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.type", worldJoinConfig.action().sound().type());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.volume", worldJoinConfig.action().sound().volume());
        cfg.set("SpawnManagementPlus.on_world_join.action.sound.pitch", worldJoinConfig.action().sound().pitch());
        cfg.set("SpawnManagementPlus.on_world_join.action.location.enabled", worldJoinConfig.action().worldLocation().enabled());
        cfg.set("SpawnManagementPlus.on_world_join.action.location.yaw", worldJoinConfig.action().worldLocation().yaw());
        cfg.set("SpawnManagementPlus.on_world_join.action.location.pitch", worldJoinConfig.action().worldLocation().pitch());
        cfg.set("SpawnManagementPlus.on_world_join.action.message_type", worldJoinConfig.action().messageType());
        cfg.set("SpawnManagementPlus.on_world_join.action.message", worldJoinConfig.action().messageContents());
        cfg.set("SpawnManagementPlus.on_world_join.action.run_command.enabled", worldJoinConfig.action().runCommand().enabled());
        cfg.set("SpawnManagementPlus.on_world_join.action.run_command.run", worldJoinConfig.action().runCommand().commandToRun());
        cfg.set("SpawnManagementPlus.on_world_join.action.run_command.user", worldJoinConfig.action().runCommand().user());
    }

    private RespawnConfig loadRespawnSettingsValues(FileConfiguration cfg) {
        var enabled = cfg.getBoolean("SpawnManagementPlus.on_respawn.enabled", false);
        var preferBedLocation = cfg.getBoolean("SpawnManagementPlus.on_respawn.prefer_bed_location", false);
        var preferAnchorLocation = cfg.getBoolean("SpawnManagementPlus.on_respawn.prefer_anchor_location", false);
        var skipRespawnScreen = cfg.getBoolean("SpawnManagementPlus.on_respawn.skip_respawn_screen", false);
        var spreadItemsOnDeath = cfg.getBoolean("SpawnManagementPlus.on_respawn.spread_items_on_death", false);
        var forceRespawnButKeepDefaultMessage = cfg.getBoolean("SpawnManagementPlus.on_respawn.use_game_death", false);

        var useWorldDefaultSpawnLocation = cfg.getBoolean("SpawnManagementPlus.on_respawn.use_world_default_spawn_location", false);
        var soundEnabled = cfg.getBoolean("SpawnManagementPlus.on_respawn.action.sound.enabled", false);
        var soundType = cfg.getString("SpawnManagementPlus.on_respawn.action.sound.type", "");
        var soundVolume = (float) cfg.getDouble("SpawnManagementPlus.on_respawn.action.sound.volume", 0);
        var soundPitch = (float) cfg.getDouble("SpawnManagementPlus.on_respawn.action.sound.pitch", 0);
        var locationEnabled = cfg.getBoolean("SpawnManagementPlus.on_respawn.action.location.enabled", false);
        var world = cfg.getString("SpawnManagementPlus.on_respawn.action.location.world", "");
        var x = cfg.getDouble("SpawnManagementPlus.on_respawn.action.location.x", 0);
        var y = cfg.getDouble("SpawnManagementPlus.on_respawn.action.location.y", 0);
        var z = cfg.getDouble("SpawnManagementPlus.on_respawn.action.location.z", 0);
        var yaw = (float) cfg.getDouble("SpawnManagementPlus.on_respawn.action.location.yaw", 0);
        var pitch = (float) cfg.getDouble("SpawnManagementPlus.on_respawn.action.location.pitch", 0);
        var messageType = cfg.getString("SpawnManagementPlus.on_respawn.action.message_type", "");
        var messageContents = cfg.getStringList("SpawnManagementPlus.on_respawn.action.message");

        var run = cfg.getString("SpawnManagementPlus.on_respawn.action.run_command.run", "");
        var user = cfg.getString("SpawnManagementPlus.on_respawn.action.run_command.user", "");
        var runEnabled = cfg.getBoolean("SpawnManagementPlus.on_respawn.action.run_command.enabled", false);
        var command = new RunCommand(runEnabled, run, user);

        var worldLocation = new WorldLocation(locationEnabled,world, x, y, z, yaw, pitch);
        var sound = new Sound(soundEnabled, soundType, soundVolume, soundPitch);
        var action = new Action(worldLocation, sound, command, messageType, messageContents);

        return new RespawnConfig(enabled, skipRespawnScreen, forceRespawnButKeepDefaultMessage, spreadItemsOnDeath, preferBedLocation, preferAnchorLocation, useWorldDefaultSpawnLocation, action);
    }

    private void setRespawnValues(FileConfiguration cfg, RespawnConfig respawnConfig) {
        cfg.set("SpawnManagementPlus.on_respawn.enabled", respawnConfig.enabled());
        cfg.set("SpawnManagementPlus.on_respawn.prefer_bed_location", respawnConfig.preferBedLocation());
        cfg.set("SpawnManagementPlus.on_respawn.prefer_anchor_location", respawnConfig.preferAnchorLocation());
        cfg.set("SpawnManagementPlus.on_respawn.skip_respawn_screen", respawnConfig.skipRespawnScreen());
        cfg.set("SpawnManagementPlus.on_respawn.spread_items_on_death", respawnConfig.spreadItemsOnDeath());
        cfg.set("SpawnManagementPlus.on_respawn.use_game_death", respawnConfig.useGameDeath());
        cfg.set("SpawnManagementPlus.on_respawn.action.sound.enabled", respawnConfig.action().sound().enabled());
        cfg.set("SpawnManagementPlus.on_respawn.action.sound.type", respawnConfig.action().sound().type());
        cfg.set("SpawnManagementPlus.on_respawn.action.sound.volume", respawnConfig.action().sound().volume());
        cfg.set("SpawnManagementPlus.on_respawn.action.sound.pitch", respawnConfig.action().sound().pitch());
        cfg.set("SpawnManagementPlus.on_respawn.action.location.enabled", respawnConfig.action().worldLocation().enabled());
        cfg.set("SpawnManagementPlus.on_respawn.action.location.world", respawnConfig.action().worldLocation().name());
        cfg.set("SpawnManagementPlus.on_respawn.action.location.x", respawnConfig.action().worldLocation().x());
        cfg.set("SpawnManagementPlus.on_respawn.action.location.y", respawnConfig.action().worldLocation().y());
        cfg.set("SpawnManagementPlus.on_respawn.action.location.z", respawnConfig.action().worldLocation().z());
        cfg.set("SpawnManagementPlus.on_respawn.action.location.yaw", respawnConfig.action().worldLocation().yaw());
        cfg.set("SpawnManagementPlus.on_respawn.action.location.pitch", respawnConfig.action().worldLocation().pitch());
        cfg.set("SpawnManagementPlus.on_respawn.action.message_type", respawnConfig.action().messageType());
        cfg.set("SpawnManagementPlus.on_respawn.action.message", respawnConfig.action().messageContents());
        cfg.set("SpawnManagementPlus.on_respawn.use_world_default_spawn_location", respawnConfig.useWorldDefaultSpawnLocation());
        cfg.set("SpawnManagementPlus.on_respawn.action.run_command.run", respawnConfig.action().runCommand().commandToRun());
        cfg.set("SpawnManagementPlus.on_respawn.action.run_command.user", respawnConfig.action().runCommand().user());
        cfg.set("SpawnManagementPlus.on_respawn.action.run_command.enabled", respawnConfig.action().runCommand().enabled());
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
            spawnConfig = SpawnConfig.SpawnFactory(enabled, seconds, spawnInsufficientPermissionErrorMessage, youAreAlreadyTeleporting, false, new WorldLocation(true, world, x, y, z, yaw, pitch),
                    onTeleportMessageType, onTeleportMessages, onTeleportSoundEnabled, onTeleportSoundType,
                    onTeleportSoundVolume, onTeleportSoundPitch, onIntervalMessageType, onIntervalMessages,
                    onIntervalSoundEnabled, onIntervalSoundType, onIntervalSoundVolume, onIntervalSoundPitch, onMoveCancel,
                    onMoveMessageType, onMoveSoundEnabled, onMoveSoundType, onMoveSoundVolume, onMoveSoundPitch, onMoveMessages);
        } else {
            spawnConfig = SpawnConfig.SpawnFactory(enabled, seconds, spawnInsufficientPermissionErrorMessage, youAreAlreadyTeleporting, true, new WorldLocation(true, serverJoinConfigSettings.action().worldLocation().name(), serverJoinConfigSettings.action().worldLocation().x(), serverJoinConfigSettings.action().worldLocation().y(), serverJoinConfigSettings.action().worldLocation().z(), serverJoinConfigSettings.action().worldLocation().yaw(), serverJoinConfigSettings.action().worldLocation().pitch()),
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
        cfg.set("SpawnManagementPlus.commands.spawn.location.world", spawnCommand.worldLocation().name());
        cfg.set("SpawnManagementPlus.commands.spawn.location.x", spawnCommand.worldLocation().x());
        cfg.set("SpawnManagementPlus.commands.spawn.location.y", spawnCommand.worldLocation().y());
        cfg.set("SpawnManagementPlus.commands.spawn.location.z", spawnCommand.worldLocation().z());
        cfg.set("SpawnManagementPlus.commands.spawn.location.yaw", spawnCommand.worldLocation().yaw());
        cfg.set("SpawnManagementPlus.commands.spawn.location.pitch", spawnCommand.worldLocation().pitch());
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
