package com.rayssmp.utilities.commands.smp;

import com.rayssmp.utilities.commands.PlayerCommand;
import com.rayssmp.utilities.config.Action;
import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.config.ServerJoinConfig;
import com.rayssmp.utilities.config.WorldLocation;
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
        if (!(player.hasPermission("SpawnManagementPlus.smp.set.server_join") || !player.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().smp().insufficientPermissionErrorMessage()));
            return true;
        }

        Location locationToSet = player.getLocation();
        var worldLocationConfig = new WorldLocation(locationToSet.getWorld().getName(), locationToSet.x(), locationToSet.y(),locationToSet.z(), player.getYaw(), player.getPitch());
        ServerJoinConfig original = config.getServerJoinSettings();

        config.setServerJoinSettings(new ServerJoinConfig(
                original.enabled(),
                original.exclude(),
                new Action(worldLocationConfig, original.action().sound(), original.action().runCommand(), original.action().messageType(), original.action().messageContents()),
                original.onlyOnFirstTime()
        ));

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
