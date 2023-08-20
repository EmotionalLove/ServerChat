package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ChatEmotes;
import net.minecraft.server.net.handler.NetServerHandler;
import org.apache.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = NetServerHandler.class, remap = false)
public class NetServerHandlerMixin {

    @Shadow
    private EntityPlayerMP playerEntity;

    @Shadow
    public static Logger logger;

    @Shadow
    private MinecraftServer mcServer;

    @Inject(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ChatEmotes;process(Ljava/lang/String;)Ljava/lang/String;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, remap = false, cancellable = true)
    private void handleChat(Packet3Chat packet, CallbackInfo ci, String s) {
        s = ChatEmotes.process(s);
        if (s.startsWith(">")) {
            s = TextFormatting.LIME + s;
        }
        s = "<" + playerEntity.getDisplayName() + TextFormatting.RESET + "> " + TextFormatting.WHITE + s;
        logger.info(s);
        mcServer.configManager.sendEncryptedChatToAllPlayers(s);
        ci.cancel();
        return;
    }

}
