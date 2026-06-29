package com.rayssmp.utilities.events;

import com.rayssmp.utilities.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
    private final GameConfigActionHandler actionHandler;

    public OnJoinOrOnWorldChangeHandler(JavaPlugin main, Config config, GameConfigActionHandler actionHandler) {
        this.main = main;
        this.config = config;
        this.actionHandler = actionHandler;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        var serverJoinSettings = config.getServerJoinSettings();
        var onlyOnFirstTime = config.getServerJoinSettings().onlyOnFirstTime();

        if (!serverJoinSettings.enabled()) {
            return;
        }

        boolean isWorldExcluded = serverJoinSettings.exclude().stream().anyMatch(s -> s.toLowerCase().equals(player.getWorld().getName()));
        if (isWorldExcluded) {
            return;
        }

        event.joinMessage(null);
        if (serverJoinSettings.onlyOnFirstTime().enabled()) {
            if (!player.hasPlayedBefore()) {
                actionHandler.handle(player, onlyOnFirstTime.action(), player::teleport);
                return;
            }
        }

        actionHandler.handle(player, serverJoinSettings.action(), player::teleport);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        var worldJoinSettings = config.getWorldJoinSettings();
        Player player = event.getPlayer();

        if (!worldJoinSettings.enabled()) {
            return;
        }

        boolean isWorldExcluded = worldJoinSettings.exclude().stream().anyMatch(s -> s.toLowerCase().equals(player.getWorld().getName()));
        if (isWorldExcluded) {
            return;
        }

        actionHandler.handle(player, worldJoinSettings.action(), player::teleport);
    }
}
