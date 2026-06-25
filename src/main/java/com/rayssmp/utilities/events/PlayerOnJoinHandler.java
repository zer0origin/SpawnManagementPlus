package com.rayssmp.utilities.events;

import com.rayssmp.utilities.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * handle movement, countdowns, and ensuring players end up at the correct location.
 * spawnrule.txt (Respawn logic)
 * spawncommand.txt (Spawn with warmup)
 * rtpcountdown.txt (RTP with warmup)
 * joinspawn.txt (Join logic)
 * firstspawnjoin.txt (New player logic)
 */
public class PlayerOnJoinHandler implements Listener {
    private final Config config;
    private final JavaPlugin main;

    public PlayerOnJoinHandler(JavaPlugin main, Config config) {
        this.main = main;
        this.config = config;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        var firstJoinSettings = config.firstJoinSettings();

        if (firstJoinSettings.enabled()) {
            if (firstJoinSettings.onlyOnFirstTime()) {
                if (!player.hasPlayedBefore()) {
                    handleOnServerJoin(player);
                    return;
                }
            }

            handleOnServerJoin(player);
        }
    }

    private void handleOnServerJoin(Player player) {
        var firstJoinSettings = config.firstJoinSettings();
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(firstJoinSettings.world()), "Failed to find first time join world!");

        if (firstJoinSettings.soundEnabled()) {
            player.playSound(player.getLocation(), firstJoinSettings.soundType(), firstJoinSettings.soundVolume(), firstJoinSettings.soundPitch());
        }

        if (firstJoinSettings.messageEnabled()) {
            for (String s : firstJoinSettings.messageContents()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }

        if (firstJoinSettings.useWorldDefault()) {
            Location defaultSpawnLocation = spawnWorld.getSpawnLocation();
            player.teleport(defaultSpawnLocation);
            return;
        }

        Location customSpawnLocation = new Location(spawnWorld, firstJoinSettings.x(), firstJoinSettings.y(), firstJoinSettings.z(), firstJoinSettings.yaw(), firstJoinSettings.pitch());
        Bukkit.getScheduler().runTask(this.main, () -> {
            player.teleport(customSpawnLocation);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        var worldJoin = config.worldJoinSettings();
        Player player = event.getPlayer();

        if (!worldJoin.enabled()) {
            return;
        }

        boolean isWorldExcluded = worldJoin.exclude().stream().anyMatch(s -> s.toLowerCase().equals(player.getWorld().getKey().value()));
        if (isWorldExcluded) {
            return;
        }

        if (worldJoin.soundEnabled()) {
            player.playSound(player.getLocation(), worldJoin.soundType().toLowerCase(), worldJoin.soundVolume(), worldJoin.pitch());
        }

        if (worldJoin.messageEnabled()) {
            for (String s : worldJoin.messageContents()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }

        Location spawnLocation = player.getWorld().getSpawnLocation();
        spawnLocation.setYaw(worldJoin.yaw());
        spawnLocation.setPitch(worldJoin.pitch());

        player.teleport(spawnLocation);
    }
}
