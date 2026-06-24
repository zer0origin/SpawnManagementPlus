package com.rayssmp.utilities.events;

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
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()){
            //TODO: firstspawnjoin.txt
            return;
        }

        //TODO: joinspawn.txt (Join logic)
    }

}
