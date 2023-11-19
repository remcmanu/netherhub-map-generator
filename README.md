# netherhub-map-generator
A Scarpet script to create a Nether hub which represents the Overworld at a 1:8 scale.

For each block in the Nether map, the script scans the corresponding section of the Overworld to determine the **average elevation** and **average block**.

# Installation
1) Install the [Carpet mod](https://github.com/gnembon/fabric-carpet) for Minecraft.

   This script was written for the following versions, but should work in others.
   - **fabric-carpet-1.20-1.4.112+v230608**
   - **Minecraft 1.20.1**

2) Install the script into your world save folder, under `/scripts`.
3) In game, enter the following command:

   `/script load netherhub`

# Usage

There are two commands.

### /netherhub switch
This will switch the player's position between the Overworld and Nether, maintaining relative world position (1:8).

### /netherhub generate_map <from> <to> <water_block>
This will generate a map from position `<from>` to position `<to>`. 

The bottom layer of the map will be a solid platform of `<water_block>`, which represents sea level.

# Example
![2023-11-19_04 07 12](https://github.com/remcmanu/netherhub-map-generator/assets/54556405/e5f70d7a-e54f-4582-8fc5-8bf1c47d0d37)
![2023-11-19_04 07 55](https://github.com/remcmanu/netherhub-map-generator/assets/54556405/6b139187-ba0d-4622-a7a8-3c546f788019)

# Exceptions

This script does not map sink holes, or other below sea-level generation. Further, it takes for granted that an elevation at or near 0 would be filled with water.
