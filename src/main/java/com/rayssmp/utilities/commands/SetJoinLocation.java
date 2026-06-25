package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.Config;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SetJoinLocation implements CommandExecutor {
    private final Config config;

    public SetJoinLocation(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.joinlocation") || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.serverJoinSettings().firstJoinLocationCommandError()));
            return true;
        }

        Location locationToSet = player.getLocation();
        Config.ServerJoin original = config.serverJoinSettings();
        config.setServerJoinSettings(new Config.ServerJoin(original.enabled(), original.onlyOnFirstTime(), player.getWorld().getName(),
                original.soundEnabled(), original.soundType(), original.soundVolume(), original.soundPitch(),
                original.useWorldDefault(), locationToSet.x(), locationToSet.y(), locationToSet.z(), player.getYaw(),
                player.getPitch(), original.messageEnabled(), original.messageFirstTimeOnly(), original.messageContents(),
                original.firstJoinLocationCommandError(), original.savedDataMessage(), original.savedDataFailedMessage()));

        try {
            config.update();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.serverJoinSettings().savedDataMessage()));
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.serverJoinSettings().savedDataFailedMessage()));
            throw new RuntimeException(e);
        }

        return true;
    }
}
