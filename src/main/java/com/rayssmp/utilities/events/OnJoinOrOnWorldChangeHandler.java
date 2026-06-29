package com.rayssmp.utilities.events;

import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.util.MinecraftUtils;
import org.bukkit.Bukkit;
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
public class OnJoinOrOnWorldChangeHandler implements Listener {
    private final Config config;
    private final JavaPlugin main;

    public OnJoinOrOnWorldChangeHandler(JavaPlugin main, Config config) {
        this.main = main;
        this.config = config;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        var serverJoinSettings = config.getServerJoinSettings();

        boolean isWorldExcluded = serverJoinSettings.exclude().stream().anyMatch(s -> s.toLowerCase().equals(player.getWorld().getKey().value()));
        if (isWorldExcluded) {
            return;
        }

        if (serverJoinSettings.enabled()) {
            event.joinMessage(null);
            if (serverJoinSettings.onlyOnFirstTime().enabled()) {
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
        var serverJoinSettings = config.getServerJoinSettings();
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(serverJoinSettings.action().worldLocation().name()), "Failed to find world!");

        MinecraftUtils.parseAndSendMessageContents(player, serverJoinSettings.action().messageType(), serverJoinSettings.action().messageContents());

        Location customSpawnLocation = new Location(spawnWorld, serverJoinSettings.action().worldLocation().x(), serverJoinSettings.action().worldLocation().y(), serverJoinSettings.action().worldLocation().z(), serverJoinSettings.action().worldLocation().yaw(), serverJoinSettings.action().worldLocation().pitch());
        Bukkit.getScheduler().runTask(this.main, () -> {
            player.teleport(customSpawnLocation);

            if (serverJoinSettings.action().sound().enabled()) {
                player.playSound(player.getLocation(), serverJoinSettings.action().sound().type(), serverJoinSettings.action().sound().volume(), serverJoinSettings.action().sound().pitch());
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        var worldJoinSettings = config.getWorldJoinSettings();
        Player player = event.getPlayer();

        if (!worldJoinSettings.enabled()) {
            return;
        }

        boolean isWorldExcluded = worldJoinSettings.exclude().stream().anyMatch(s -> s.toLowerCase().equals(player.getWorld().getKey().value()));
        if (isWorldExcluded) {
            return;
        }

        MinecraftUtils.parseAndSendMessageContents(player, worldJoinSettings.action().messageType(), worldJoinSettings.action().messageContents());

        Location spawnLocation = player.getWorld().getSpawnLocation();
        spawnLocation.setYaw(worldJoinSettings.action().worldLocation().yaw());
        spawnLocation.setPitch(worldJoinSettings.action().worldLocation().pitch());

        Bukkit.getScheduler().runTask(this.main, () -> {
            player.teleport(spawnLocation);

            if (worldJoinSettings.action().sound().enabled()) {
                player.playSound(player.getLocation(), worldJoinSettings.action().sound().type(), worldJoinSettings.action().sound().volume(), worldJoinSettings.action().sound().pitch());
            }
        });
    }
}
