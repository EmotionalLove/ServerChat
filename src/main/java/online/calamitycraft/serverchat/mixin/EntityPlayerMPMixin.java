package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetServerHandler;
import online.calamitycraft.serverchat.util.DeathMessageUtil;
import online.calamitycraft.serverchat.util.WhisperUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = EntityPlayerMP.class, remap = false)
public class EntityPlayerMPMixin {

    @Shadow public NetServerHandler playerNetServerHandler;

    /**
     * @author CalamityCraft
     * @reason Completely overhaul original death message system
     */
    @Overwrite
    private String deathMessage(Entity entity) {
        EntityPlayer p = (EntityPlayerMP) (Object) this;
        return DeathMessageUtil.generateDeathMessage(p, entity);
    }

    /**
     * @author CalamityCraft
     * @reason Overhaul word-wrap
     */
    @Overwrite
    public void addChatMessage(String s) {
        if (WhisperUtil.getStringWidth(s)[1] == -1) {
            this.playerNetServerHandler.sendPacket(new Packet3Chat(I18n.getInstance().translateKey(s)));
            return;
        }
        List<String> ss = WhisperUtil.slice(s);
        ss.forEach(split -> {
            this.playerNetServerHandler.sendPacket(new Packet3Chat(split.trim()));
        });
    }
}
