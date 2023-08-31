package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.PortalHandler;
import net.minecraft.core.world.World;
import net.minecraft.core.world.type.WorldType;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.threading.Task;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = PortalHandler.class, remap = false)
public abstract class PortalHandlerMixin {

    @Shadow public abstract boolean attemptToTeleportToClosestPortal(World world, Entity entity, Dimension oldDim, Dimension newDim);

    @Shadow public abstract boolean generatePortal(World world, Entity entity, Dimension oldDim, Dimension newDim);

    /**
     * @author CalamityCraft
     * @reason Reduce server-halting lag induced by checking for closest portal twice.
     */
    @Overwrite
    public void teleportEntity(World world, Entity entity, Dimension oldDim, Dimension newDim) {
        ServerChatMod.taskManager.queueTask(new Task(() -> {
            if (!this.canFindClosestPortal(world, entity, oldDim, newDim)) {
                ServerChatMod.taskManager.queueTask(new Task(() -> {
                    this.generatePortal(world, entity, oldDim, newDim);
                    this.attemptToTeleportToClosestPortal(world, entity, oldDim, newDim);
                }), false);
            } else {
                ServerChatMod.taskManager.queueTask(new Task(() -> this.attemptToTeleportToClosestPortal(world, entity, oldDim, newDim)), false);
            }
        }), true);


    }

    @Unique
    public boolean canFindClosestPortal(World world, final Entity entity, Dimension oldDim, Dimension newDim) {
        int searchRadius = 128;
        double lowestEntityDistanceSquared = -1.0;
        int closestPortalX = 0;
        int closestPortalY = 0;
        int closestPortalZ = 0;
        int entityBlockX = MathHelper.floor_double(entity.x);
        int entityBlockZ = MathHelper.floor_double(entity.z);
        WorldType oldWorldType = oldDim.getDimensionData(world).getWorldType();
        WorldType newWorldType = newDim.getDimensionData(world).getWorldType();
        double entityPosYScaled = entity.y;
        int oldDimRangeY = oldWorldType.getMaxY() - oldWorldType.getMinY();
        entityPosYScaled -= (double)oldWorldType.getMinY();
        entityPosYScaled /= (double)oldDimRangeY;
        int newDimRangeY = newWorldType.getMaxY() - newWorldType.getMinY();
        entityPosYScaled *= (double)newDimRangeY;
        entityPosYScaled += (double)newWorldType.getMinY();
        int targetPortalId = newDim.homeDim == null ? oldDim.portalBlockId : newDim.portalBlockId;
        for (int dx = entityBlockX - searchRadius; dx <= entityBlockX + searchRadius; ++dx) {
            double xEntityDistance = (double)dx + 0.5 - entity.x;
            for (int dz = entityBlockZ - searchRadius; dz <= entityBlockZ + searchRadius; ++dz) {
                double zEntityDistance = (double)dz + 0.5 - entity.z;
                for (int dy = newWorldType.getMaxY() - 1; dy >= newWorldType.getMinY(); --dy) {
                    if (world.getBlockId(dx, dy, dz) != targetPortalId) continue;
                    while (world.getBlockId(dx, dy - 1, dz) == targetPortalId) {
                        --dy;
                    }
                    double yEntityDistance = (double)dy + 0.5 - entityPosYScaled;
                    double entityDistanceSquared = xEntityDistance * xEntityDistance + yEntityDistance * yEntityDistance + zEntityDistance * zEntityDistance;
                    if (!(lowestEntityDistanceSquared < 0.0) && !(entityDistanceSquared < lowestEntityDistanceSquared)) continue;
                    lowestEntityDistanceSquared = entityDistanceSquared;
                    closestPortalX = dx;
                    closestPortalY = dy;
                    closestPortalZ = dz;
                }
            }
        }
        if (lowestEntityDistanceSquared >= 0.0) {
            world.getBlockId(closestPortalX - 1, closestPortalY, closestPortalZ);
            world.getBlockId(closestPortalX + 1, closestPortalY, closestPortalZ);
            world.getBlockId(closestPortalX, closestPortalY, closestPortalZ - 1);
            world.getBlockId(closestPortalX, closestPortalY, closestPortalZ + 1);
            return true;
        }
        return false;
    }
}
