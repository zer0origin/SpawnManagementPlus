package com.rayssmp.utilities;

import com.rayssmp.utilities.commands.smp.Smp;
import com.rayssmp.utilities.commands.spawn.SecondPlaceholder;
import com.rayssmp.utilities.commands.spawn.SpawnCommand;
import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.events.OnJoinOrOnWorldChangeHandler;
import com.rayssmp.utilities.events.PlayerRespawnEvent;
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
        Bukkit.getPluginManager().registerEvents(new OnJoinOrOnWorldChangeHandler(this, config), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnEvent(this, config), this);

        var smp = new Smp(config);
        this.getCommand("smp").setExecutor(smp);
        this.getCommand("smp").setTabCompleter(smp);
        this.getLogger().log(Level.INFO, "Enabled!");

        var spawn = new SpawnCommand(this, config);
        Bukkit.getPluginManager().registerEvents(spawn, this);
        this.getCommand("spawn").setExecutor(spawn);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new SecondPlaceholder().register();
        }
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

/**
 * Author: Zer0origin
 */