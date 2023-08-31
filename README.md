# ServerChat

This mod is a server-sided 'plugin' that runs off of Babric (Fabric for Beta Minecraft). This mod does not need to be installed on your players clients. This mod is a chat and command management system for basic survival servers that are trying to not to drift too far from the vanilla experience. This mod adds much desired features to build upon Minecraft's already existing chat system.

This mod also implements playerdata-by-UUID, to keep playerdata consistent across username changes. it will also convert existing legacy playerdata files to UUID based ones if a player without a UUID save joins, and a legacy file can be matched with their name.

This branch contains experimental changes that may break your worlds. Unfortunately there's not a lot that can be done with Mixins to fix the awful Server logic in Beta Minecraft. Unless I get on the BTA development team, funny hacks will have to suffice.

### Work-in-progress!!

## Features:
- \>tfw can greentext in minecraft
- Improved death messages
- Improved join/leave messages
- Improved private-messaging
- playerdat-by-UUID
- automatic playerdat conversion for existing playerdat-by-name files
- /tps command for monitoring server performance, accessible in-game.
- /kill command for player suicide.

## Todo:
- addChatMessage() sanitiser to properly word-wrap on the server side (if it's possible, not sure yet)
- /reply, /last, /ignore, /ignorelist
- party/group chat
- /help
- rate-limiting, chat spam blocking
- configurability

https://github.com/Turnip-Labs/babric-instance-repo/releases/tag/v1.7.7.0_01 run the server "babric/fabric" JAR and use `./gradlew build` in this repo to generate a JAR file, which you will place in the `mods` folder of your fabric/babric server directory.