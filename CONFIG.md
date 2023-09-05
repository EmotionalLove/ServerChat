Below is a default `chatconfig.yml` file.
Each config option will be commented with it's function.

```yml
allow-joinleave-msgs: true # Disable or enable the player join/leave announcements.
allow-greentext: true # Allow players to prefix their messages with ">" to colour the message green. Allows for quotes and 4chan-style greentext stories.
clump-radius-x: 3 # Set the item drop clumping radius on the X-axis. Set to 0 to disable. Ridiculously large values may cause issues.
clump-radius-y: 2 # Set the item drop clumping radius on the Y-axis. Set to 0 to disable. Ridiculously large values may cause issues.
clump-radius-z: 3 # Set the item drop clumping radius on the Z-axis. Set to 0 to disable. Ridiculously large values may cause issues.
obscure-kick-reason: true # Set any and all kick-reasons to "You have been disconnected from the server"
defer-entity-updates: false # (Unimplemented) when the server TPS is continuously low, defer updating entities in priority of type and distance from players.
```