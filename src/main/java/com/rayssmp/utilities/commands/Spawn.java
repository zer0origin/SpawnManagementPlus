package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Spawn implements CommandExecutor, Listener {
    private final Config config;
    private final JavaPlugin main;
    private final Map<UUID, BukkitTask> intervalTask = new HashMap<>();

    public Spawn(JavaPlugin main, Config config) {
        this.config = config;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!config.getCommandSettings().spawn().enabled()) {
            return true;
        }

        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.spawn") || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().spawn().insufficientPermissionErrorMessage()));
            return true;
        }

        var spawnSettings = config.getCommandSettings().spawn();
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(spawnSettings.world()), "Failed to find world!");
        Location location = new Location(spawnWorld, spawnSettings.x(), spawnSettings.y(), spawnSettings.z(),
                spawnSettings.yaw(), spawnSettings.pitch());

        if (spawnSettings.seconds() < 0) {
            Bukkit.getScheduler().runTask(this.main, () -> {
                player.teleport(location);
            });
        }

        AtomicInteger second = new AtomicInteger(spawnSettings.seconds() + 1);
        var task = Bukkit.getScheduler().runTaskTimer(this.main, () -> {
            if (second.decrementAndGet() > 0) {
                var prefix = second.get() == 1 ? "econd" : "econds";
                var parsedStrings = spawnSettings.onInterval().messages().stream()
                        .map(s -> s.replaceAll("%smp_teleport_seconds%", second.get() + " s" + prefix))
                        .map(s -> s.replaceAll("%smp_teleport_seconds_capital%", second.get() + " S" + prefix))
                        .toList();

                MinecraftUtils.parseAndSendMessageContents(player, parsedStrings, spawnSettings.onInterval().messageType());

                if (spawnSettings.onInterval().soundEnabled()) {
                    player.playSound(player.getLocation(), spawnSettings.onInterval().soundType(), spawnSettings.onInterval().soundVolume(), spawnSettings.onInterval().soundPitch());
                }

                return;
            }

            MinecraftUtils.parseAndSendMessageContents(player, spawnSettings.onTeleport().messages(), spawnSettings.onTeleport().messageType());
            intervalTask.get(player.getUniqueId()).cancel();
            player.teleport(location);

            if (spawnSettings.onTeleport().soundEnabled()) {
                player.playSound(player.getLocation(), spawnSettings.onTeleport().soundType(), spawnSettings.onTeleport().soundVolume(), spawnSettings.onTeleport().soundPitch());
            }

        }, 0, 20);

        intervalTask.put(player.getUniqueId(), task);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var spawnSettings = config.getCommandSettings().spawn();
        if (!intervalTask.containsKey(player.getUniqueId()) || !spawnSettings.onMove().enabled()) {
            return;
        }

        var from = event.getFrom();
        var to = event.getTo();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            MinecraftUtils.parseAndSendMessageContents(player, spawnSettings.onMove().messages(), spawnSettings.onMove().messageType());

            if (spawnSettings.onMove().soundEnabled()) {
                player.playSound(player.getLocation(), spawnSettings.onMove().soundType(), spawnSettings.onMove().soundVolume(), spawnSettings.onMove().soundPitch());
            }

            intervalTask.remove(player.getUniqueId()).cancel();
        }
    }
}
