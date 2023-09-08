package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import net.minecraft.server.net.handler.NetLoginHandler;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.event.PlayerJoinServerEvent;
import online.calamitycraft.serverchat.util.ChatFeaturePlayer;
import online.calamitycraft.serverchat.util.YMLParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.IOException;

@Mixin(value = NetLoginHandler.class, remap = false)
public class NetLoginHandlerMixin {

    @Redirect(method = "doLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V", ordinal = 0))
    private void sendPacketToAllPlayers(ServerConfigurationManager instance, Packet i) {
        if (i instanceof Packet3Chat) {
            Packet3Chat message = (Packet3Chat) i;
            String m = message.message.replaceAll("\247.", "");
            String player = m.split("\\ ")[0].trim();
            File file = new File(ServerChatMod.chatFeatureDataFolder, player.toLowerCase() + ".yml");
            if (file.exists()) {
                YMLParser parser = new YMLParser(file);
                if (ChatFeaturePlayer.isValidFormat(parser)) {
                    ChatFeaturePlayer.chatFeaturePlayerRegistry.put(player.toLowerCase(), new ChatFeaturePlayer(parser));
                }
            } else {
                try {
                    if (!file.createNewFile()) {
                        throw new IOException("could not create " + player.toLowerCase() + ".yml");
                    }
                    ChatFeaturePlayer newFeature = new ChatFeaturePlayer();
                    newFeature.save(file, new YMLParser(file));
                    ChatFeaturePlayer.chatFeaturePlayerRegistry.put(player.toLowerCase(), new ChatFeaturePlayer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!ServerChatMod.config.allowJoinLeaveMessages(true)) return;
            instance.sendPacketToAllPlayers(new Packet3Chat(TextFormatting.GRAY + player + " joined."));
        }
    }

    @Inject(method = "doLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;readPlayerDataFromFile(Lnet/minecraft/server/entity/player/EntityPlayerMP;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void doLogin(Packet1Login packet1login, CallbackInfo ci, EntityPlayerMP entityplayermp) {
        PlayerJoinServerEvent event = new PlayerJoinServerEvent(entityplayermp);
        ServerChatMod.getEventManager().invokeEvent(event);
    }

}
