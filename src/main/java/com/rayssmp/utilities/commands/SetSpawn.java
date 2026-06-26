package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SetSpawn implements CommandExecutor {
    private final Config config;

    public SetSpawn(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.setspawn") || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setSpawnPermissionError()));
            return true;
        }

        var commandSettings = config.getCommandSettings();
        var location = player.getLocation();

        config.setCommandValues(new Config.CommandSettings(commandSettings.enabled(), commandSettings.setJoinLocationSaved(),
                commandSettings.setJoinLocationSavedFailed(), location.getWorld().getName(), location.x(), location.y(),
                location.z(), location.getYaw(), location.getPitch(), commandSettings.spawnPermissionError(),
                commandSettings.setJoinLocationPermissionError(), commandSettings.setSpawnPermissionError(),
                commandSettings.setSpawnSaved(), commandSettings.setSpawnFailed(), commandSettings.cooldownTimerSeconds(),
                commandSettings.coolDownTimerCancelOnMoveMessage(), commandSettings.cooldownTimerCancelOnMove(),
                commandSettings.intervalEnabled(), commandSettings.intervalMessage(), commandSettings.onTeleport()));

        try {
            config.update();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setSpawnSaved()));
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setSpawnFailed()));
            throw new RuntimeException(e);
        }

        return true;
    }
}
