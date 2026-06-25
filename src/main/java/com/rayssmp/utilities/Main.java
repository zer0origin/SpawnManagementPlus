package com.rayssmp.utilities;

import com.rayssmp.utilities.commands.Reload;
import com.rayssmp.utilities.commands.SetJoinLocation;
import com.rayssmp.utilities.commands.SetSpawn;
import com.rayssmp.utilities.commands.Spawn;
import com.rayssmp.utilities.events.PlayerOnJoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
    private final Config config = new Config();

    @Override
    public void onLoad() {
        File dataFolder = this.getDataFolder();

        if (!dataFolder.exists()) {
            this.getLogger().info("Creating plugin folder...");

            if (!dataFolder.mkdirs()) {
                this.getLogger().severe("Failed to create plugin folder!");
            }

            this.getLogger().info("Plugin folder successfully created.");
        }

        try {
            config.load();
        } catch (IOException e) {
            this.getLogger().severe("Failed to create config file!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "Starting...");
        Bukkit.getPluginManager().registerEvents(new PlayerOnJoinHandler(this, config), this);

        this.getCommand("setjoinlocation").setExecutor(new SetJoinLocation(config));
        this.getCommand("reloadspawnmanagementplus").setExecutor(new Reload(config));
        this.getCommand("setspawn").setExecutor(new SetSpawn(config));
        this.getLogger().log(Level.INFO, "Enabled!");

        var spawn = new Spawn(this, config);
        Bukkit.getPluginManager().registerEvents(spawn, this);
        this.getCommand("spawn").setExecutor(spawn);
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "Shutting Down...");
        this.getLogger().log(Level.INFO, "Updating config...");

        try {
            config.update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.getLogger().log(Level.INFO, "Shut Down!");
    }
}
