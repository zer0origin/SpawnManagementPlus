package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Reload implements CommandExecutor {
    private final Config config;

    public Reload(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.reload") || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Wrong permissions"));
            return true;
        }

        try {
            config.load();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Reloaded"));
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Failed to reload"));
            throw new RuntimeException(e);
        }

        return true;
    }
}
