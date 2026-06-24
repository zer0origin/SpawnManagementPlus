package com.rayssmp.utilities;

import com.rayssmp.utilities.commands.SetFirstJoinLocation;
import com.rayssmp.utilities.events.PlayerOnJoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
    private final Config config = new Config();

    @Override
    public void onLoad() {
        config.createOrLoad();
    }

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "Starting...");
        Bukkit.getPluginManager().registerEvents(new PlayerOnJoinHandler(config.firstJoinSettings(), config.worldJoinSettings()), this);
        this.getCommand("setfirstjoinlocation").setExecutor(new SetFirstJoinLocation(config));
        this.getLogger().log(Level.INFO, "Enabled!");
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
