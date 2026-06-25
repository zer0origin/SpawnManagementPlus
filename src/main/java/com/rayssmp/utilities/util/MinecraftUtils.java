package com.rayssmp.utilities.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class MinecraftUtils {
    public static void parseAndSendMessageContents(Player player, List<String> strings) {
        strings.stream().map(s -> {
            try {
                return PlaceholderAPI.setPlaceholders(player, s);
            } catch (Throwable e) {
                return s;
            }
        }).forEach(s -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        });
    }
}
