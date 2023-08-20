package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.net.ServerConfigurationManager;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = NetLoginHandler.class, remap = false)
public class NetLoginHandlerMixin {

    @Redirect(method = "doLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V", ordinal = 0))
    private void sendPacketToAllPlayers(ServerConfigurationManager instance, Packet i) {
        if (i instanceof Packet3Chat) {
            Packet3Chat message = (Packet3Chat) i;
            String m = message.message.replaceAll("\247.", "");
            String player = m.split("\\ ")[0].trim();
            instance.sendPacketToAllPlayers(new Packet3Chat(TextFormatting.GRAY + player + " joined."));
        }
    }

}
