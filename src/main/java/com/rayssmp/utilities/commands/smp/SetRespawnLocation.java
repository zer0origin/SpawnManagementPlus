package com.rayssmp.utilities.commands.smp;

import com.rayssmp.utilities.commands.PlayerCommand;
import com.rayssmp.utilities.config.Action;
import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.config.RespawnConfig;
import com.rayssmp.utilities.config.WorldLocation;
import com.rayssmp.utilities.config.command.Sound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

        WorldLocation worldLocation = new WorldLocation(locationToSet.getWorld().getName(), locationToSet.getX(), locationToSet.getY(), locationToSet.getZ(), locationToSet.getYaw(), locationToSet.getPitch());
        Action action = new Action(worldLocation, original.action().sound(), original.action().messageType(), original.action().messageContents());
        config.setRespawnSettings(new RespawnConfig(original.enabled(), original.skipRespawnScreen(),
                original.forceRespawnButKeepDefaultMessage(), original.spreadItemsOnDeath(), original.preferBedLocation(),
                original.preferAnchorLocation(), action));

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
