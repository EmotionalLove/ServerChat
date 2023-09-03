# ServerChat

This mod is a server-sided 'plugin' that runs off of Babric (Fabric for Beta Minecraft). This mod does not need to be installed on your players clients. This mod is a chat and command management system for basic survival servers that are trying to not to drift too far from the vanilla experience. This mod adds much desired features to build upon Minecraft's already existing chat system.

This mod also implements playerdata-by-UUID, to keep playerdata consistent across username changes. it will also convert existing legacy playerdata files to UUID based ones if a player without a UUID save joins, and a legacy file can be matched with their name.

For version 1.7.7_02

### Work-in-progress!!

## Features:
- \>tfw can greentext in minecraft
- Improved death messages
- Improved join/leave messages
- Improved private-messaging (/reply, /last, /ignore, /ignorelist, /togglechat)
- playerdat-by-UUID
- automatic playerdat conversion for existing playerdat-by-name files
- /tps command for monitoring server performance, accessible in-game.
- /kill command for player suicide.
- automatically generated /help command
- server-sided word-wrap to preserve chat colours on multi-lined messages


## Todo:
- party/group chat
- rate-limiting, chat spam blocking
- (additional) configurability
- server performance improvements, low tps mitigations (entity tick deferring, etc)

https://github.com/Turnip-Labs/babric-instance-repo/releases/tag/v1.7.7.0_01 run the server "babric/fabric" JAR and use `./gradlew build` in this repo to generate a JAR file, which you will place in the `mods` folder of your fabric/babric server directory.