# ServerChat

This mod is a server-sided 'plugin' that runs off of Babric (Fabric for Beta Minecraft). This mod does not need to be installed on your players clients. This mod is a chat and command management system for basic survival servers that are trying to not drift too far from the vanilla experience. This mod adds much desired features to build upon Minecraft's already existing chat system.

This mod also implements playerdata-by-UUID, to keep playerdata consistent across username changes. it will also convert existing legacy playerdata files to UUID based ones if a player without a UUID save joins, and a legacy file can be matched with their name.

This mod contains **anti-features**, and may not be suitable for your needs. Configuration is currently limited, but intended to be expanded upon in the future. Please read the entire feature lists to ensure this mod is right for you.

For version 1.7.7_02. See `CONFIG.YML` for explanations behind the configuration options.

### Work-in-progress!!

## Command features:
- Disables BTA and vanilla commands (security feature)
- Disables operator commands (security feature)
- Disables BTA nicknames and colornames
- Adds /reply, /last, /ignore, /ignorelist, /togglechat
- Adds /tps (allow players to view server performance)
- Adds /kill (allow players to commit suicide)

## Other features:
- \>tfw can greentext in minecraft
- Revised death messages, more descriptive
- Simplified join/leave messages, removed player kick announcement
- Server kick-reason obfuscation
- kick-reason on server /stop
- playerdat-by-UUID + automatic legacy playerdat conversion
- server-sided word-wrap to preserve chat colours on multi-lined messages
- Enforce maximum stack sizes and remove illegal blocks and items from player inventories, and prevent their use and/or placement (configurable)
- Randomised spawnpoint (configurable)
- Restrict netherroof access (configurable)

## Exploit patches:
- Validate the contents of Packet134ItemData (Never trust the client!)


## Todo:
- party/group chat
- rate-limiting, chat spam blocking
- (additional) configurability
- server performance improvements, low tps mitigations (entity tick deferring, etc)

https://github.com/Turnip-Labs/babric-instance-repo/releases/tag/v1.7.7.0_02 run the server "babric/fabric" JAR and use `./gradlew build` in this repo to generate a JAR file, which you will place in the `mods` folder of your fabric/babric server directory.