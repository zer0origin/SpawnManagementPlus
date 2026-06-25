package com.rayssmp.utilities.commands;

import com.rayssmp.utilities.Config;
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
        if (!config.getCommandSettings().enabled()) {
            return true;
        }

        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.spawn") || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().spawnPermissionError()));
            return true;
        }

        var commandSettings = config.getCommandSettings();
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(commandSettings.world()), "Failed to find world!");
        Location location = new Location(spawnWorld, commandSettings.x(), commandSettings.y(), commandSettings.z(),
                commandSettings.yaw(), commandSettings.pitch());

        if (config.getCommandSettings().cooldownTimerSeconds() < 0) {
            Bukkit.getScheduler().runTask(this.main, () -> {
                player.teleport(location);
            });
        }

        AtomicInteger second = new AtomicInteger(config.getCommandSettings().cooldownTimerSeconds() + 1);
        var task = Bukkit.getScheduler().runTaskTimer(this.main, () -> {
            if (second.decrementAndGet() > 0) {
                //TODO: Send in between message. OPTIONAL SETTING REQUIRED, OPTIONAL PLACEHOLDER REQUIRED, VIA BOTH PLACEHOLDER API AND NON-PLACEHOLDER API.

                if (config.getCommandSettings().intervalEnabled()) {
                    var prefix = second.get() == 1 ? "econd" : "econds";
                    var parsedStrings = commandSettings.intervalMessage().stream()
                            .map(s -> s.replaceAll("%smp_teleport_seconds%", second.get() + " s" + prefix))
                            .map(s -> s.replaceAll("%smp_teleport_seconds_capital%", second.get() + " S" + prefix))
                            .toList();

                    MinecraftUtils.parseAndSendMessageContents(player, parsedStrings);
                    return;
                }
                return;
            }

            MinecraftUtils.parseAndSendMessageContents(player, commandSettings.onTeleport());
            player.teleport(location);
            intervalTask.get(player.getUniqueId()).cancel();
        }, 0, 20);

        intervalTask.put(player.getUniqueId(), task);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!intervalTask.containsKey(event.getPlayer().getUniqueId()) && !config.getCommandSettings().cooldownTimerCancelOnMove()) {
            return;
        }

        var from = event.getFrom();
        var to = event.getTo();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            MinecraftUtils.parseAndSendMessageContents(event.getPlayer(), config.getCommandSettings().coolDownTimerCancelOnMoveMessage());
            intervalTask.get(event.getPlayer().getUniqueId()).cancel();
        }
    }
}
