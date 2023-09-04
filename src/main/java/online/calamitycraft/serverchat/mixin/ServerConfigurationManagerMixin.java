package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.core.util.helper.AES;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import online.calamitycraft.serverchat.util.WhisperUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

import static online.calamitycraft.serverchat.util.WhisperUtil.sendEncrypted;

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
            ss.forEach(split -> sendEncrypted(this.playerEntities, split.trim()));
        } else sendEncrypted(playerEntities, message);

    }

    @Inject(method = "savePlayerStates", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/save/IPlayerFileData;writePlayerData(Lnet/minecraft/core/entity/player/EntityPlayer;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void savePlayerStates(CallbackInfo ci, int i) {
        this.playerEntities.get(i).playerNetServerHandler.kickPlayer(TextFormatting.ORANGE + "You have been disconnected from the server");
    }

}
