package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.config.ServerJoinConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ServerJoinSpawnLocation implements PlayerCommand {
    private final Config config;

    /**
     * Set the Server Join location in the config.
     * @param config
     */
    public ServerJoinSpawnLocation(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull String @NotNull [] args) {
        if (!(player.hasPermission("SpawnManagementPlus.smp.set.join") || !player.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setJoin().insufficientPermissionErrorMessage()));
            return true;
        }

        Location locationToSet = player.getLocation();
        ServerJoinConfig original = config.getServerJoinSettings();
        config.setServerJoinSettings(new ServerJoinConfig(original.enabled(), original.onlyOnFirstTime(), player.getWorld().getName(),
                original.soundEnabled(), original.soundType(), original.soundVolume(), original.soundPitch(),
                original.useWorldDefault(), locationToSet.x(), locationToSet.y(), locationToSet.z(), player.getYaw(),
                player.getPitch(), original.messageType(), original.messageContents(), original.exclude()));

        try {
            config.update();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setJoin().savedDataMessage()));
        } catch (IOException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().setJoin().savedDataFailedMessage()));
            throw new RuntimeException(e);
        }

        return true;
    }
}
