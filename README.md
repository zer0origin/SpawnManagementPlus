# SpawnManagementPlus

**SpawnManagementPlus** is a powerful, lightweight utility plugin for Minecraft servers designed to give administrators granular control over player spawning, teleportation, and entry experiences. Whether you want to create a custom join sequence, manage multi-world transitions, or customize the respawn process, this plugin provides a centralized configuration to handle it all.

---

## Key Features

*   **Custom Join Handling:** Force players to a specific location when they log in (optionally restricted to first-time joins only).
*   **World Transition Control:** Automatically teleport players to a world's specific spawn point when they switch worlds.
*   **Advanced Respawn Logic:**
    *   Optionally bypass the "You Died" screen for immediate respawn.
    *   Configurable item behavior (spreading items) during auto-respawn.
    *   Control whether to honor or override Bed and Anchor spawns.
*   **Dynamic `/spawn` Command:**
    *   Configurable teleportation warm-up (timer).
    *   Movement detection (cancel teleport if the player moves).
    *   Custom countdown messages and sounds.
*   **Rich Media Support:**
    *   Support for **PlaceholderAPI** in all messages.
    *   Toggle between **CHAT** and **ACTION_BAR** notifications.
    *   Per-event sound effects with configurable pitch and volume.

---

## Commands & Permissions

| Command            | Description                                            | Permission |
|:-------------------|:-------------------------------------------------------| :--- |
| `/spawn`           | Teleports the player to the designated spawn location. | `SpawnManagementPlus.spawn` |
| `/setspawn`        | Sets the location for the `/spawn` command.            | `SpawnManagementPlus.setspawn` |
| `/setjoinlocation` | Sets the location for the `on_server_join` feature.    | `SpawnManagementPlus.setjoinlocation` |
| `/reloadsmp`       | Reload the plugins config.   | `SpawnManagementPlus.reloadspawnmanagementplus` |

---

## Configuration Deep-Dive

The `config.yml` is structured into logical modules. Each module can be independently enabled or disabled.

### 1. Global Join Logic (`on_server_join`)
This section manages the experience when a player connects to the server.
*   **`only_on_first_time`**: Set to `true` to make this a "Starter Spawn" only for new players.
*   **`exclude`**: Define worlds where this logic should not run (e.g., don't teleport players if they log out in the End).
*   **`action`**: Define the sound, coordinates, and greeting message.

### 2. Multi-World Logic (`on_world_join`)
Triggers when a player moves from one world to another (e.g., using a portal or teleport).
*   Useful for "Hub" worlds where you want players to always start at a specific gate regardless of where they left that world.

### 3. Respawn Management (`on_respawn`)
Take control of the death cycle.
*   **`skip_respawn_screen`**: Instantly respawns the player, removing the need to click the death screen button.
*   **`spread_items_on_death`**: If skipping the respawn screen, this ensures items drop naturally rather than glitching.
*   **`prefer_bed_location` / `prefer_anchor_location`**: If set to `false`, the plugin will ignore beds/anchors and force the player to the coordinates defined in this config.

### 4. The Spawn Command (`commands.spawn`)
A fully-featured teleportation system.
*   **`cooldown_timer.seconds`**: The "Warm-up" period. Set to `-1` for instant teleport.
*   **`on_move.cancel`**: If the player moves their feet during the countdown, the teleport is aborted.
*   **`on_interval`**: This sends a message every second of the countdown. Use the `%smp_teleport_seconds%` placeholder here.

---

## Placeholders (OPTIONAL)

The plugin natively supports **PlaceholderAPI**. You can use any external placeholders.

---

## Important Notes

1.  **World Loaders:** If you are using a multi-world setup (Multiverse-Core, MyWorlds, etc.), ensure those worlds are loaded before SpawnManagementPlus attempts to teleport players to them.
2.  **Sound Names:** Sound types must follow the official Bukkit/Spigot names (e.g., `minecraft:entity.enderman.teleport` or `block.note_block.pling`).
3.  **Empty Messages:** To disable any specific message while keeping the feature enabled, use `[]` in the configuration.

---

## Source Code Overview

The repo provides a docker file for easy plugin testing. the docker file builds a Minecraft server using the plugin jar, and files specified in `/miencraft_data`. Remote debugger is available on port 5005.