package com.rayssmp.utilities.events;

import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Objects;

public class PlayerRespawnEvent implements Listener {
    private final Config config;
    private final JavaPlugin plugin;

    public PlayerRespawnEvent(JavaPlugin plugin, Config config) {
        this.config = config;
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(org.bukkit.event.player.PlayerRespawnEvent event) {
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

        World world = Objects.requireNonNull(Bukkit.getWorld(respawnSettings.world()), "Failed to find world!");
        Location location = new Location(world, respawnSettings.x(), respawnSettings.y(), respawnSettings.z(), respawnSettings.yaw(), respawnSettings.pitch());
        event.setRespawnLocation(location);

        MinecraftUtils.parseAndSendMessageContents(event.getPlayer(), respawnSettings.messageContent(), respawnSettings.messageType());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        var respawnSettings = config.getRespawnSettings();
        var player = event.getEntity();

        if (!respawnSettings.skipRespawnScreen()) {
            return;
        }

        if(respawnSettings.forceRespawnButKeepDefaultMessage()){
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

        location.getWorld().dropItem(location, itemStack).setVelocity(new Vector(0,-1,0));
    }
}
