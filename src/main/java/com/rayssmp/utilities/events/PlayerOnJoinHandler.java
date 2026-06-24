package com.rayssmp.utilities.events;

import com.rayssmp.utilities.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *handle movement, countdowns, and ensuring players end up at the correct location.
 *  spawnrule.txt (Respawn logic)
 *  spawncommand.txt (Spawn with warmup)
 *  rtpcountdown.txt (RTP with warmup)
 *  joinspawn.txt (Join logic)
 *  firstspawnjoin.txt (New player logic)
 */
public class PlayerOnJoinHandler implements Listener {
    private final Config.FirstJoin firstJoin;

    public PlayerOnJoinHandler(Config.FirstJoin firstJoin) {
        this.firstJoin = firstJoin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore() && firstJoin.enabled()){
            World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(firstJoin.world()), "Failed to find first time join world!");

            if (firstJoin.useWorldDefault()){
                Location defaultSpawnLocation = spawnWorld.getSpawnLocation();
                player.teleport(defaultSpawnLocation);
                return;
            }

            Location customSpawnLocation = new Location(spawnWorld, firstJoin.x(), firstJoin.y(), firstJoin.z(), firstJoin.yaw(), firstJoin.pitch());
            player.teleport(customSpawnLocation);
            return;
        }

        //TODO: joinspawn.txt (Join logic)
    }

}
