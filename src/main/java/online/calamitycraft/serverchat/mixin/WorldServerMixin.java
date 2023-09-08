package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.server.net.ServerConfigurationManager;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WorldServer.class, remap = false)
public abstract class WorldServerMixin extends World {

    public WorldServerMixin(World world, Dimension dimension) {
        super(world, dimension);
    }

    @Redirect(method = "updateEnoughPlayersSleepingFlag", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayersInDimension(Lnet/minecraft/core/net/packet/Packet;I)V"))
    private void sendPacketToAllPlayersInDimension(ServerConfigurationManager instance, Packet pck, int dim) {
        return;
    }

    @Redirect(method = "updateSleepingPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayersInDimension(Lnet/minecraft/core/net/packet/Packet;I)V"))
    private void sendPacketToAllPlayersInDimension$1(ServerConfigurationManager instance, Packet pck, int dim) {
        for (EntityPlayer player : this.players) {
            player.addChatMessage(TextFormatting.LIGHT_GRAY + "The server sleep requirement has been met.");
            player.addChatMessage(TextFormatting.LIGHT_GRAY + "Night has been skipped.");
        }
    }
}
