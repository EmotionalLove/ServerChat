package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.util.DeathMessageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = EntityPlayerMP.class, remap = false)
public class EntityPlayerMPMixin {

    /**
     * @author CalamityCraft
     * @reason Completely overhaul original death message system
     */
    @Overwrite
    private String deathMessage(Entity entity) {
        EntityPlayer p = (EntityPlayerMP) (Object) this;
        return DeathMessageUtil.generateDeathMessage(p, entity);
    }

}
