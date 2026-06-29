package com.rayssmp.utilities.commands.spawn;

import com.rayssmp.utilities.config.Config;
import com.rayssmp.utilities.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RunnableSpawnTask implements Runnable {
    private final Config config;
    protected static final Map<UUID, TaskData> intervalTask = new HashMap<>();
    private final Player player;
    private final Location location;

    public RunnableSpawnTask(SpawnCommand spawn, Config config, Player player, Location location) {
        this.config = config;
        this.player = player;
        this.location = location;
    }

    @Override
    public void run() {
        var spawnSettings = config.getCommandSettings().spawnConfig();
        var taskData = intervalTask.get(player.getUniqueId());

        if (taskData == null) {
            return;
        }
        taskData.setRemainingSeconds(taskData.getRemainingSeconds() - 1);

        if (taskData.getRemainingSeconds() > 0) {
            int finalSeconds = taskData.getRemainingSeconds();

            var parsedStrings = spawnSettings.onInterval().messages();
            if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                parsedStrings = parsedStrings.stream()
                        .map(s -> s.replaceAll("%smp_second%", String.valueOf(finalSeconds)))
                        .toList();
            }

            MinecraftUtils.parseAndSendMessageContents(player, spawnSettings.onInterval().messageType(), parsedStrings);

            if (spawnSettings.onInterval().soundEnabled()) {
                player.playSound(player.getLocation(), spawnSettings.onInterval().soundType(), spawnSettings.onInterval().soundVolume(), spawnSettings.onInterval().soundPitch());
            }

            return;
        }

        MinecraftUtils.parseAndSendMessageContents(player, spawnSettings.onTeleport().messageType(), spawnSettings.onTeleport().messages());
        intervalTask.remove(player.getUniqueId()).getTask().cancel();
        player.teleport(location);

        if (spawnSettings.onTeleport().soundEnabled()) {
            player.playSound(player.getLocation(), spawnSettings.onTeleport().soundType(), spawnSettings.onTeleport().soundVolume(), spawnSettings.onTeleport().soundPitch());
        }

    }

    public static class TaskData {
        private BukkitTask task;
        private int remainingSeconds;

        public TaskData() {
        }

        public TaskData(int remainingSeconds) {
            this.remainingSeconds = remainingSeconds;
        }

        public TaskData(BukkitTask task, int remainingSeconds) {
            this.task = task;
            this.remainingSeconds = remainingSeconds;
        }

        public BukkitTask getTask() {
            return task;
        }

        void setTask(BukkitTask task) {
            this.task = task;
        }

        public int getRemainingSeconds() {
            return remainingSeconds;
        }

        void setRemainingSeconds(int remainingSeconds) {
            this.remainingSeconds = remainingSeconds;
        }
    }
}
