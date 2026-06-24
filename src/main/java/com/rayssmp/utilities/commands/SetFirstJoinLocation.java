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

public class SetFirstJoinLocation implements CommandExecutor {
    private final Config config;

    public SetFirstJoinLocation(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.setfirstjoinlocation") && !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.firstJoinSettings().firstJoinLocationCommandError()));
            return true;
        }

        Location locationToSet = player.getLocation();
        Config.FirstJoin original = config.firstJoinSettings();
        config.setFirstJoinSettings(new Config.FirstJoin(original.enabled(), original.onlyOnFirstTime(), player.getWorld().getKey().value(),
                original.soundEnabled(), original.soundType(), original.soundVolume(), original.soundPitch(),
                original.useWorldDefault(), locationToSet.x(), locationToSet.y(), locationToSet.z(), player.getYaw(),
                player.getPitch(), original.messageEnabled(), original.messageContents(),
                original.firstJoinLocationCommandError(), original.savedDataMessage(), original.savedDataFailedMessage()));

        try {
            config.update();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.firstJoinSettings().savedDataMessage()));
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.firstJoinSettings().savedDataFailedMessage()));
            throw new RuntimeException(e);
        }

        return true;
    }
}
