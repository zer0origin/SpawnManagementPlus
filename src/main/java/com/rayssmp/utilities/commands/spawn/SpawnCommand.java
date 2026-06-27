package com.rayssmp.utilities.commands.spawn;

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

public class SpawnCommand implements CommandExecutor, Listener {
    private final Config config;
    private final JavaPlugin main;
    private final Map<UUID, BukkitTask> intervalTask = new HashMap<>();

    public SpawnCommand(JavaPlugin main, Config config) {
        this.config = config;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!config.getCommandSettings().spawnConfig().enabled()) {
            return true;
        }


        if (!(sender instanceof Player player)) {
            System.out.println("You cannot execute this command!");
            return true;
        }

        if (RunnableSpawnTask.intervalTask.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().spawnConfig().youAreAlreadyTeleporting()));
            return true;
        }

        if (!(sender.hasPermission("SpawnManagementPlus.spawn") || !sender.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getCommandSettings().spawnConfig().insufficientPermissionErrorMessage()));
            return true;
        }

        var spawnSettings = config.getCommandSettings().spawnConfig();
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(spawnSettings.world()), "Failed to find world!");
        Location location = new Location(spawnWorld, spawnSettings.x(), spawnSettings.y(), spawnSettings.z(),
                spawnSettings.yaw(), spawnSettings.pitch());

        if (spawnSettings.seconds() < 0) {
            Bukkit.getScheduler().runTask(this.main, () -> {
                player.teleport(location);
            });
        }

        var task = Bukkit.getScheduler().runTaskTimer(this.main, new RunnableSpawnTask(this, config, player, location), 0, 20L);
        RunnableSpawnTask.intervalTask.put(player.getUniqueId(), new RunnableSpawnTask.TaskData(task, spawnSettings.seconds()));
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var spawnSettings = config.getCommandSettings().spawnConfig();
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
