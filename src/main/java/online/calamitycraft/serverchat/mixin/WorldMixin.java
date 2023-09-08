package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.save.LevelData;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.util.SpawnUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = World.class, remap = false)
public class WorldMixin {

    @Shadow
    @Final
    public Dimension dimension;

    @Shadow
    protected LevelData levelData;

//    /**
//     * @author CalamityCraft
//     * @reason Impl randomspawn
//     */
//    @Overwrite
//    public ChunkCoordinates getSpawnPoint() {
//        if (this.dimension == Dimension.overworld) {
//            return SpawnUtil.findSuitableSpawnPoint(ServerChatMod.config.getSpawnX(300), ServerChatMod.config.getSpawnZ(300), (World) (Object) this);
//        }
//        return new ChunkCoordinates(this.levelData.getSpawnX(), this.levelData.getSpawnY(), this.levelData.getSpawnZ());
//    }

}
