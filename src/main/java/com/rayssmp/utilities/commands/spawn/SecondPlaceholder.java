package com.rayssmp.utilities.commands.spawn;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SecondPlaceholder extends PlaceholderExpansion {
    public SecondPlaceholder() {
    }

    @Override
    public @NotNull String getIdentifier() {
        return "smp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "zer0origin";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2.3";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if(player == null){
            return "";
        }

        if(params.equalsIgnoreCase("second")){
            if(!RunnableSpawnTask.intervalTask.containsKey(player.getUniqueId())){
                return "0";
            }

            var taskData = RunnableSpawnTask.intervalTask.get(player.getUniqueId());
            if (taskData.getTask().isCancelled()){
                return "";
            }

            return String.valueOf(taskData.getRemainingSeconds());
        }

        return "";
    }
}
