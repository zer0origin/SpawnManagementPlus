# SpawnManagementPlus

Minecraft plugin for handling server/world join events.
## Features

- **Server Join Events**: Plays sounds, teleports and sends messages when players join the server.
- **First Join Only**: Option for performing actions only once when players join the server for the first time.
- **World Join Events**: Plays sounds, teleports and sends messages when players move between worlds.
- **Exclusion List**: Option for disabling world join events in some worlds, e.g. The Nether and The End.
- **Customisation**: Total control over sound types, volume, pitch, coordinates, messages.

## Commands

- **/setfirstjoinlocation**: Sets player location as a spawn point for server join event.

## Configuration

Config file consists of two main sections:

### on_server_join
This section contains settings about behavior of plugin when players connect to the server.
- **enabled**: Enables/disables this functionality.
- **only_on_first_time**: Actions are performed only when player connects to the server for the first time.
- **action**: Define sound, location (x, y, z, world) and welcome message.

### on_world_join
This section contains settings about behavior of plugin when players change worlds.
- **enabled**: Enables/disables this functionality.
- **exclude**: List of worlds where this will not trigger.
- **action**: Define the sound, orientation (yaw, pitch), and messages.

## Requirements
Minecraft Version: '26.1.2'\
For multi-world locations, you need a world loader plugin.