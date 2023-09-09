Below is a default `chatconfig.yml` file. It will be automatically generated on first run.
Each config option will be commented with it's function.

```yml
sleep-percent: 50 # Overrides the sometimes-not-working server.properties value
spawn-radius-x: 300
spawn-radius-z: 300
illegal-items: # A list of block and item IDs that should be prevented from using and removed from player inventories
               # This feature will also enforce the max stack size for all items, including those not on this list
  - 260 # Bedrock
  - 270 # Water Flowing Block
  - 272 # Lava Flowing Block
  - 271 # Water Still Block
  - 273 # Lava Still Block
  - 522 # Piston Head
  - 830 # Nether Portal Block
  - 831 # Paradise Portal Block
  - 610 # Bed Block
  - 690 # Wheat Crops Block
# It may take an additional server restart to activate this feature after installing this mod for the first time, or upgrading an existing installation
# You may set `illegal-items` to an empty array to disable this feature.
allow-joinleave-msgs: true # Disable or enable the player join/leave announcements.
allow-greentext: true # Allow players to prefix their messages with ">" to colour the message green. Allows for quotes and 4chan-style greentext stories.
clump-radius-x: 3 # Set the item drop clumping radius on the X-axis. Set to 0 to disable. Ridiculously large values may cause issues.
clump-radius-y: 2 # Set the item drop clumping radius on the Y-axis. Set to 0 to disable. Ridiculously large values may cause issues.
clump-radius-z: 3 # Set the item drop clumping radius on the Z-axis. Set to 0 to disable. Ridiculously large values may cause issues.
obscure-kick-reason: true # Set any and all kick-reasons to "You have been disconnected from the server"
defer-entity-updates: false # (Unimplemented) when the server TPS is continuously low, defer updating entities in priority of type and distance from players.
random-spawn: true # Spawn players in a random radius around 0,0 (see spawn-radius-x and spawn-radius-z)
restrict-nether-roof: true # Forcefully disallow access to the nether roof
```