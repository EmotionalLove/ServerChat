package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityPlayer.class, remap = false)
public class EntityPlayerMixin {

    @Shadow public String username;

    /**
     * @author CalamityCraft
     * @reason Remove nicknames
     */
    @Overwrite
    public String getDisplayName() {
        return this.username;
    }

}
