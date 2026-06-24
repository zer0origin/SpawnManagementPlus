package com.rayssmp.utilities.events;

import com.rayssmp.utilities.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
    private final Config.FirstJoin firstJoinSettings;

    public PlayerOnJoinHandler(Config.FirstJoin firstJoinSettings) {
        this.firstJoinSettings = firstJoinSettings;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore() && firstJoinSettings.enabled()) {
            World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(firstJoinSettings.world()), "Failed to find first time join world!");

            if (firstJoinSettings.soundEnabled()) {
                player.playSound(player.getLocation(), firstJoinSettings.soundType().toLowerCase(), firstJoinSettings.soundVolume(), firstJoinSettings.pitch());
            }

            if (firstJoinSettings.messageEnabled()){
                for (String s : firstJoinSettings.messageContents()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                }
            }

            if (firstJoinSettings.useWorldDefault()) {
                Location defaultSpawnLocation = spawnWorld.getSpawnLocation();
                player.teleport(defaultSpawnLocation);
                return;
            }

            Location customSpawnLocation = new Location(spawnWorld, firstJoinSettings.x(), firstJoinSettings.y(), firstJoinSettings.z(), firstJoinSettings.yaw(), firstJoinSettings.pitch());
            player.teleport(customSpawnLocation);
            return;
        }

        //TODO: joinspawn.txt (Join logic)
    }
}
