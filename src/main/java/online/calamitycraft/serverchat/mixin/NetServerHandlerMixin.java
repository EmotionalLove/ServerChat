package online.calamitycraft.serverchat.mixin;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet134ItemData;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ChatEmotes;
import net.minecraft.server.net.ServerConfigurationManager;
import net.minecraft.server.net.handler.NetServerHandler;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.util.WhisperUtil;
import org.apache.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = NetServerHandler.class, remap = false)
public abstract class NetServerHandlerMixin {

    @Shadow
    private EntityPlayerMP playerEntity;

    @Shadow
    public static Logger logger;

    @Shadow
    private MinecraftServer mcServer;

    @Shadow
    public abstract void kickPlayer(String s);

    @Inject(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ChatEmotes;process(Ljava/lang/String;)Ljava/lang/String;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, remap = false, cancellable = true)
    private void handleChat(Packet3Chat packet, CallbackInfo ci, String s) {
        s = ChatEmotes.process(s);
        StringBuilder ss = new StringBuilder();
        if (s.startsWith(">") && ServerChatMod.config.isGreentextAllowed(true)) {
            ss.append(TextFormatting.LIME).append(s);
        } else {
            ss.append(s);
        }
        s = TextFormatting.WHITE + "<" + playerEntity.getDisplayName() + TextFormatting.RESET + "> " + TextFormatting.WHITE + ss;
        logger.info(s);
        WhisperUtil.sendEncryptedChatToPlayers(playerEntity, s);
        ci.cancel();
    }

    @Redirect(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/handler/NetServerHandler;handleSlashCommand(Ljava/lang/String;)V", ordinal = 0))
    private void handleSlashCommand(NetServerHandler instance, String e) {
        try {
            if (!ServerChatMod.getCommandProcessor().processCommand(this.playerEntity, e)) {
                this.playerEntity.addChatMessage(TextFormatting.RED + "Bad command.");
            }
        } catch (Exception ex) {
            this.playerEntity.addChatMessage(TextFormatting.RED + "An exception has occurred.");
            ex.printStackTrace();
        }
    }

    @Redirect(method = "handleErrorMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V", ordinal = 0))
    private void sendPacketToAllPlayers(ServerConfigurationManager instance, Packet i) {
        if (i instanceof Packet3Chat) {
            Packet3Chat message = (Packet3Chat) i;
            String m = message.message.replaceAll("\247.", "");
            String player = m.split("\\ ")[0].trim();
            instance.sendPacketToAllPlayers(new Packet3Chat(TextFormatting.GRAY + player + " left."));
        }
    }

    @Redirect(method = "kickPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V", ordinal = 0))
    private void sendPacketToAllPlayers$1(ServerConfigurationManager instance, Packet i) {
        if (i instanceof Packet3Chat) {
        }
    }

    @Inject(method = "handleItemData", at = @At("HEAD"), cancellable = true)
    private void handleItemData(Packet134ItemData packet, CallbackInfo ci) {
        boolean isLabel = this.playerEntity.inventory.getStackInSlot(packet.slot).itemID == 16518; // ensure the requested slot is a label
        boolean flag = validateCompoundTag(packet.tag); // ensure the compoundtag only has the four permitted keys in it
        if (!isLabel || !flag) {
            this.kickPlayer(TextFormatting.ORANGE + "You have been disconnected from the server");
            ci.cancel();
        }
    }

    @Unique
    private boolean validateCompoundTag(CompoundTag tag) {
        for (String s : tag.getValue().keySet()) {
            if (s.equals("overrideName")) {
                continue;
            }
            if (s.equals("overrideColor")) {
                continue;
            }
            if (s.equals("name")) {
                String potentialName = tag.getStringOrDefault("name", null);
                if (potentialName == null) return false; // vanilla bta client does not send empty or null strings.
                potentialName = potentialName.replace("\247", "$"); // We don't want section symbols in our custom names.
                if (potentialName.length() != potentialName.getBytes().length) {
                    // Contains unicode characters. Disallow this due to possible exploits
                    // Implement proper sanitization later
                    // Disallowing unicode may not be an option for non-English servers
                    return false;
                }
                tag.putString("name", potentialName);
                continue;
            }
            if (s.equals("color")) {
                continue;
            }
            return false;
        }
        return true;
    }
}
