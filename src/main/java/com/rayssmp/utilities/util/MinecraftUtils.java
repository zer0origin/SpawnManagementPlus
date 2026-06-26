package com.rayssmp.utilities.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class MinecraftUtils {
    public static void parseAndSendMessageContents(Player player, List<String> strings, String messageType) {
        strings.stream().map(s -> {
            try {
                return PlaceholderAPI.setPlaceholders(player, s);
            } catch (Throwable e) {
                return s;
            }
        }).forEach(s -> {
            player.spigot().sendMessage(ChatMessageType.valueOf(messageType),
                    TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
        });
    }
}
