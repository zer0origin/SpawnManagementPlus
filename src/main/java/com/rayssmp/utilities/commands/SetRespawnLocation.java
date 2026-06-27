package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.config.RespawnConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SetRespawnLocation implements PlayerCommand {
    private final Config config;

    public SetRespawnLocation(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull String @NotNull [] args) {
        if (!(player.hasPermission("SpawnManagementPlus.smp.set.respawm") || !player.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().smp().insufficientPermissionErrorMessage()));
            return true;
        }

        Location locationToSet = player.getLocation();
        var original = config.getRespawnSettings();
        config.setRespawnSettings(new RespawnConfig(original.enabled(), original.skipRespawnScreen(),
                original.forceRespawnButKeepDefaultMessage(), original.spreadItemsOnDeath(), original.preferBedLocation(),
                original.preferAnchorLocation(), locationToSet.getWorld().getName(), locationToSet.getX(), locationToSet.getY(),
                locationToSet.getZ(), locationToSet.getYaw(), locationToSet.getPitch(), original.messageType(), original.messageContent()));

        try {
            config.update();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().smp().savedDataMessage()));
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().smp().savedDataFailedMessage()));
            throw new RuntimeException(e);
        }

        return true;
    }
}
