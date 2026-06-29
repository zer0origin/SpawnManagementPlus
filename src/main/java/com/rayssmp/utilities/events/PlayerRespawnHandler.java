package com.rayssmp.utilities.events;

import com.rayssmp.utilities.config.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class PlayerRespawnHandler implements Listener {
    private final Config config;
    private final JavaPlugin plugin;
    private final GameConfigActionHandler actionHandler;

    public PlayerRespawnHandler(JavaPlugin plugin, Config config, GameConfigActionHandler actionHandler) {
        this.config = config;
        this.plugin = plugin;
        this.actionHandler = actionHandler;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        var respawnSettings = config.getRespawnSettings();
        if (!respawnSettings.enabled()) {
            return;
        }

        if (respawnSettings.preferBedLocation() && event.isBedSpawn()) {
            return;
        }

        if (respawnSettings.preferAnchorLocation() && event.isAnchorSpawn()) {
            return;
        }

        //TODO: Use actionHandler with/without use_world_default_location

        if (respawnSettings.useWorldDefaultSpawnLocation()) {
            actionHandler.handleWithWorld(event.getPlayer(), respawnSettings.action(), event.getPlayer().getWorld(), event::setRespawnLocation);
            return;
        }

        actionHandler.handle(event.getPlayer(), respawnSettings.action(), event::setRespawnLocation);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        var respawnSettings = config.getRespawnSettings();
        var player = event.getEntity();

        if (!respawnSettings.skipRespawnScreen()) {
            return;
        }

        if (respawnSettings.useGameDeath()) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    player.spigot().respawn();
                }
            }, 1);

            return;
        }

        event.setCancelled(true);
        dropInventory(event.getPlayer());
        player.spigot().respawn();
    }

    private void dropInventory(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) {
                continue;
            }

            dropItem(player.getLocation(), itemStack, config.getRespawnSettings().spreadItemsOnDeath());
            player.getInventory().removeItem(itemStack);
        }

        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack == null) {
                continue;
            }

            dropItem(player.getLocation(), itemStack, config.getRespawnSettings().spreadItemsOnDeath());
            player.getInventory().removeItem(itemStack);
        }
    }

    private void dropItem(Location location, ItemStack itemStack, boolean naturally) {
        if (naturally) {
            location.getWorld().dropItemNaturally(location, itemStack);
            return;
        }

        location.getWorld().dropItem(location, itemStack).setVelocity(new Vector(0, -1, 0));
    }
}
