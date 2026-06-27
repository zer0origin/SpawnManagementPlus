package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Smp implements CommandExecutor, TabCompleter {
    private final Config config;

    public Smp(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission(command.getPermission()) || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Wrong permissions"));
            return true;
        }

        if (args.length == 0) {
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            new Reload(config).onCommand(player, args);
        }

        if (args[0].equalsIgnoreCase("location")) {
            if (args.length == 1) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments"));
                return true;
            }

            switch (args[1].toLowerCase()) {
                case "spawn":
                    new SetSpawn(config).onCommand(player, args);
                    break;
                case "join":
                    new SetJoinLocation(config).onCommand(player, args);
                    break;
                case "respawn":
                    new SetRespawnLocation(config).onCommand(player, args);
                    break;
                default:
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments"));
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length <= 1 || args[0].isEmpty()) {
            return List.of("reload", "location");
        }

        if (args[0].equalsIgnoreCase("location")) {
            if (args.length == 2 || args[1].isEmpty()) {
                return List.of("spawn", "join", "respawn");
            }
        }

        return List.of();
    }
}
