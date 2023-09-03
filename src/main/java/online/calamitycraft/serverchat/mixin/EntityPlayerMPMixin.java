package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import net.minecraft.server.net.handler.NetServerHandler;
import online.calamitycraft.serverchat.util.DeathMessageUtil;
import online.calamitycraft.serverchat.util.WhisperUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Mixin(value = EntityPlayerMP.class, remap = false)
public class EntityPlayerMPMixin {

    @Shadow public NetServerHandler playerNetServerHandler;

    @Shadow public MinecraftServer mcServer;

    /**
     * @author CalamityCraft
     * @reason Completely overhaul original death message system
     */
    @Overwrite
    private String deathMessage(Entity entity) {
        return "";
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void sendPacketToAllPlayers(Entity entity, CallbackInfo ci) {
        Future<String> stringFuture = DeathMessageUtil.generateDeathMessageAsync((EntityPlayerMP) (Object) this, entity);
        new Thread(() -> {
            while (!stringFuture.isDone());
            try {
                WhisperUtil.sendEncryptedChatToPlayers((EntityPlayerMP) (Object) this, stringFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
        ci.cancel();
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
