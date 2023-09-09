package online.calamitycraft.serverchat.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.server.world.WorldServer;

import java.util.Random;

public class SpawnUtil {

    /**
     * Find a random spawn point within a range of 0,0. The spawn point will be solid at the highest point in the world.
     * This method will recurse if the randomly chosen spawn point is not solid ground.
     * This method will load/generate chunks if they are not loaded, so large ranges may cause performance issues.
     * @param xRange
     * @param zRange
     * @param world the world
     */
    public static ChunkCoordinates findSuitableSpawnPoint(int xRange, int zRange, World world) {
        Random rand = new Random();
        int x, y, z;
        x = rand.nextBoolean() ? rand.nextInt(xRange) : -rand.nextInt(xRange);
        z = rand.nextBoolean() ? rand.nextInt(zRange) : -rand.nextInt(zRange);
        y = findHighestSolidBlock(world, x, z);
        if (y == -1) return findSuitableSpawnPoint(xRange, zRange, world);
        return new ChunkCoordinates(x, y, z);
    }

    private static int findHighestSolidBlock(World world, int x, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        x &= 0xF;
        z &= 0xF;
        for (int heightBlocks = world.getHeightBlocks() - 1; heightBlocks > 0; heightBlocks--) {
            int id = chunk.getBlockID(x, heightBlocks, z);
            if (id == 0) continue;
            Material mat = Block.blocksList[id].blockMaterial;
            if (mat.isSolid()) return heightBlocks + 1;
            if (mat.isLiquid()) return -1;
        }
        return -1;
    }
    public static int findHighestSolidBlockNether(World world, int x, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        x &= 0xF;
        z &= 0xF;
        for (int heightBlocks = world.getHeightBlocks() - 3; heightBlocks > 0; heightBlocks--) {
            int id = chunk.getBlockID(x, heightBlocks, z);
            if (id == 0) {
                int idBelow = chunk.getBlockID(x, heightBlocks - 1, z);
                if (idBelow != 0) continue;
                int idBelowBelow = chunk.getBlockID(x, heightBlocks - 2, z);
                if (idBelowBelow == 0) continue;
                Material mat = Block.blocksList[idBelowBelow].blockMaterial;
                if (mat.isSolid()) return heightBlocks - 1;
            }
        }
        return -1;
    }
}
