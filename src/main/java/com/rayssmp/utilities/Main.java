package com.rayssmp.utilities;

import com.rayssmp.utilities.commands.SetFirstJoinLocation;
import com.rayssmp.utilities.events.PlayerOnJoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private final Config config = new Config();

    @Override
    public void onLoad() {
        config.createOrLoad();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new PlayerOnJoinHandler(config.firstJoinSettings()), this);
        this.getCommand("setfirstjoinlocation").setExecutor(new SetFirstJoinLocation(config));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
