package com.rayssmp.utilities.events;

import com.rayssmp.utilities.config.Action;
import com.rayssmp.utilities.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.function.Consumer;

public class GameConfigActionHandler {
    private JavaPlugin main;

    public GameConfigActionHandler(JavaPlugin main) {
        this.main = main;
    }

    public void handleWithActionWorld(Player player, Action action, Consumer<Location> teleportHandle) {
        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(action.worldLocation().name()), "Failed to find world!");
        Location location = new Location(spawnWorld, action.worldLocation().x(), action.worldLocation().y(), action.worldLocation().z(), action.worldLocation().yaw(), action.worldLocation().pitch());

        handle(player, action, teleportHandle, location);
    }

    public void handleWithWorld(Player player, Action action, World world, Consumer<Location> teleportHandle) {
        Location location = world.getSpawnLocation();

        handle(player, action, teleportHandle, location);
    }

    private void handle(Player player, Action action, Consumer<Location> teleportHandle, Location location) {
        MinecraftUtils.parseAndSendMessageContents(player, action.messageType(), action.messageContents());

        if (action.sound().enabled()) {
            player.playSound(player, action.sound().type(), action.sound().volume(), action.sound().pitch());
        }

        if (action.runCommand().enabled()) {
            var cmdStr = MinecraftUtils.tryPlaceholderParseOrReturn(player, action.runCommand().commandToRun());
            if (action.runCommand().user().equalsIgnoreCase("player")) {
                player.performCommand(cmdStr);
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdStr);
            }
        }

        if (action.worldLocation().enabled()) {
            teleportHandle.accept(location);
        }
    }
}
