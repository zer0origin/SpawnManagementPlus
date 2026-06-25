package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Spawn implements CommandExecutor {
    private final Config config;
    private final JavaPlugin main;

    public Spawn(JavaPlugin main, Config config) {
        this.config = config;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!config.getCommandSettings().enabled()) {
            return true;
        }

        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        var commandSettings = config.getCommandSettings();
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(commandSettings.world()), "Failed to find world!");
        Location location = new Location(spawnWorld, commandSettings.x(), commandSettings.y(), commandSettings.z(),
                commandSettings.yaw(), commandSettings.pitch());

        Bukkit.getScheduler().runTask(this.main, () -> {
            player.teleport(location);
        });

        return true;
    }
}
