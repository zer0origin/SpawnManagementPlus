package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Reload implements PlayerCommand {
    private final Config config;

    public Reload(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull String @NotNull [] args) {
        if (!(player.hasPermission("SpawnManagementPlus.reload") || !player.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Wrong permissions")); //TODO: message
            return true;
        }

        try {
            config.load();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Reloaded")); //TODO: message
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Failed to reload")); //TODO: message
            throw new RuntimeException(e);
        }

        return true;
    }
}
