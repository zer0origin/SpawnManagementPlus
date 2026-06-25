package com.rayssmp.utilities.events;

import com.rayssmp.utilities.Config;
import me.clip.placeholderapi.PlaceholderAPI;
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

import java.util.List;
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
        var firstJoinSettings = config.serverJoinSettings();

        if (firstJoinSettings.enabled()) {
            event.joinMessage(null);
            if (firstJoinSettings.onlyOnFirstTime()) {
                if (!player.hasPlayedBefore()) {
                    handleOnServerJoin(player);
                    return;
                }

                return;
            }

            handleOnServerJoin(player);
        }
    }

    private void handleOnServerJoin(Player player) {
        var serverJoinSettings = config.serverJoinSettings();
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(serverJoinSettings.world()), "Failed to find first time join world!");

        if (serverJoinSettings.messageEnabled()) {
            parseAndSendMessageContents(player, serverJoinSettings.messageContents());
        }

        if (serverJoinSettings.useWorldDefault()) {
            Location defaultSpawnLocation = spawnWorld.getSpawnLocation();
            player.teleport(defaultSpawnLocation);
            return;
        }

        Location customSpawnLocation = new Location(spawnWorld, serverJoinSettings.x(), serverJoinSettings.y(), serverJoinSettings.z(), serverJoinSettings.yaw(), serverJoinSettings.pitch());
        Bukkit.getScheduler().runTask(this.main, () -> {
            player.teleport(customSpawnLocation);

            if (serverJoinSettings.soundEnabled()) {
                player.playSound(player.getLocation(), serverJoinSettings.soundType(), serverJoinSettings.soundVolume(), serverJoinSettings.soundPitch());
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        var worldJoinSettings = config.worldJoinSettings();
        Player player = event.getPlayer();

        if (!worldJoinSettings.enabled()) {
            return;
        }

        boolean isWorldExcluded = worldJoinSettings.exclude().stream().anyMatch(s -> s.toLowerCase().equals(player.getWorld().getKey().value()));
        if (isWorldExcluded) {
            return;
        }

        if (worldJoinSettings.messageEnabled()) {
            parseAndSendMessageContents(player, worldJoinSettings.messageContents());
        }

        Location spawnLocation = player.getWorld().getSpawnLocation();
        spawnLocation.setYaw(worldJoinSettings.yaw());
        spawnLocation.setPitch(worldJoinSettings.pitch());

        Bukkit.getScheduler().runTask(this.main, () -> {
            player.teleport(spawnLocation);

            if (worldJoinSettings.soundEnabled()) {
                player.playSound(player.getLocation(), worldJoinSettings.soundType(), worldJoinSettings.soundVolume(), worldJoinSettings.soundPitch());
            }
        });
    }

    private void parseAndSendMessageContents(Player player, List<String> strings) {
        strings.stream().map(s -> {
            try {
                return PlaceholderAPI.setPlaceholders(player, s);
            } catch (Throwable e) {
                return s;
            }
        }).forEach(s -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        });
    }
}
