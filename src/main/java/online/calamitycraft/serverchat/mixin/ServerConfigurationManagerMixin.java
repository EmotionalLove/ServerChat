package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.core.util.helper.AES;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import online.calamitycraft.serverchat.util.WhisperUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(value = ServerConfigurationManager.class, remap = false)
public class ServerConfigurationManagerMixin {

    @Shadow public List<EntityPlayerMP> playerEntities;

    /**
     * @author CalamityCraft
     * @reason Word-wrap overhaul
     */
    @Overwrite
    public void sendEncryptedChatToAllPlayers(String message) {
        if (WhisperUtil.getStringWidth(message)[1] != -1) {
            List<String> ss = WhisperUtil.slice(message);
            ss.forEach(split -> this.sendEncrypted(split.trim()));
        } else sendEncrypted(message);

    }

    @Unique
    private void sendEncrypted(String message) {
        for (EntityPlayerMP entityplayermp : this.playerEntities) {
            entityplayermp.playerNetServerHandler.sendPacket(new Packet3Chat(message, AES.keyChain.get(entityplayermp.username)));
        }
    }

}
