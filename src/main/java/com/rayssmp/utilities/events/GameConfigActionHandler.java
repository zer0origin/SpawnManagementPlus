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

    public void handle(Player player, Action action, Consumer<Location> teleportHandle) {
        MinecraftUtils.parseAndSendMessageContents(player, action.messageType(), action.messageContents());

        World spawnWorld = Objects.requireNonNull(Bukkit.getWorld(action.worldLocation().name()), "Failed to find world!");
        Location customSpawnLocation = new Location(spawnWorld, action.worldLocation().x(), action.worldLocation().y(), action.worldLocation().z(), action.worldLocation().yaw(), action.worldLocation().pitch());
        player.teleport(customSpawnLocation);
        teleportHandle.accept(customSpawnLocation);

        if (action.sound().enabled()) {
            player.playSound(customSpawnLocation, action.sound().type(), action.sound().volume(), action.sound().pitch());
        }
    }

    public void handleWithWorld(Player player, Action action, World world, Consumer<Location> teleportHandle) {
        MinecraftUtils.parseAndSendMessageContents(player, action.messageType(), action.messageContents());

        Location customSpawnLocation = world.getSpawnLocation();
        teleportHandle.accept(customSpawnLocation);

        if (action.sound().enabled()) {
            player.playSound(customSpawnLocation, action.sound().type(), action.sound().volume(), action.sound().pitch());
        }
    }
}
