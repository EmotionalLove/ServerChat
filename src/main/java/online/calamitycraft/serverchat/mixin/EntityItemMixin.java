package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.util.phys.AABB;
import online.calamitycraft.serverchat.ServerChatMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityItem.class, remap = false)
public class EntityItemMixin {

    @Inject(method = "clumpToNearbyStack", at = @At(value = "HEAD"), cancellable = true)
    private void clumpToNearbyStack(CallbackInfo ci) {
        boolean flag = ServerChatMod.config.getClumpX(3f) <= 0;
        boolean flag1 = ServerChatMod.config.getClumpY(3f) <= 0;
        boolean flag2 = ServerChatMod.config.getClumpZ(3f) <= 0;
        if (flag && flag1 && flag2) ci.cancel();
    }
    @Redirect(method = "clumpToNearbyStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/phys/AABB;expand(DDD)Lnet/minecraft/core/util/phys/AABB;"))
    private AABB expand(AABB instance, double d, double d1, double d2) {
        float x = ServerChatMod.config.getClumpX(3f);
        float y = ServerChatMod.config.getClumpY(3f);
        float z = ServerChatMod.config.getClumpZ(3f);
        return instance.expand(x, y, z);
        // increase clumping radius
    }
    /**
     * @author CalamityCraft
     * @reason Compare age of both EntityItems, remove oldest
     */
    @Overwrite
    private static void combineItems(EntityItem entityItem1, EntityItem entityItem2) {
        EntityItem oldest = entityItem1.age >= entityItem2.age ? entityItem1 : entityItem2;
        EntityItem youngest = entityItem1.age < entityItem2.age ? entityItem1 : entityItem2;
        youngest.item.stackSize += oldest.item.stackSize;
        oldest.item.stackSize = 0;
        // entityItem1.age = Math.min(entityItem1.age, entityItem2.age);
        youngest.delayBeforeCanPickup = Math.max(youngest.delayBeforeCanPickup, oldest.delayBeforeCanPickup);
        oldest.remove();
    }

}
