package com.rayssmp.utilities.commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerCommand {
    public boolean onCommand(@NotNull Player player, @NotNull String @NotNull [] args);
}
