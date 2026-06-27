package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.config.command.CommandConfig;
import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.config.command.spawn.SpawnConfig;
import org.bukkit.ChatColor;
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.smp.set.spawn") || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setSpawn().insufficientPermissionErrorMessage()));
            return true;
        }

        var commandSettings = config.getCommandSettings();
        var spawnSettings = config.getCommandSettings().spawnConfig();
        var location = player.getLocation();

        config.setCommandValues(new CommandConfig(commandSettings.setJoin(),
                commandSettings.setSpawn(),
                commandSettings.smp(), SpawnConfig.SpawnFactory(spawnSettings.enabled(), spawnSettings.seconds(),
                        spawnSettings.insufficientPermissionErrorMessage(), spawnSettings.youAreAlreadyTeleporting(),
                        location.getWorld().getName(), location.x(), location.y(), location.z(), location.getYaw(), location.getPitch(),
                        spawnSettings.onTeleport().messageType(), spawnSettings.onTeleport().messages(), spawnSettings.onTeleport().soundEnabled(), spawnSettings.onTeleport().soundType(), spawnSettings.onTeleport().soundVolume(), spawnSettings.onTeleport().soundPitch(),
                        spawnSettings.onInterval().messageType(), spawnSettings.onInterval().messages(), spawnSettings.onInterval().soundEnabled(), spawnSettings.onInterval().soundType(), spawnSettings.onInterval().soundVolume(), spawnSettings.onInterval().soundPitch(),
                        spawnSettings.onMove().enabled(), spawnSettings.onMove().messageType(), spawnSettings.onMove().soundEnabled(), spawnSettings.onMove().soundType(), spawnSettings.onMove().soundVolume(), spawnSettings.onMove().soundPitch(), spawnSettings.onMove().messages())
        ));

        try {
            config.update();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setSpawn().savedDataMessage()));
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setSpawn().savedDataFailedMessage()));
            throw new RuntimeException(e);
        }

        return true;
    }
}
