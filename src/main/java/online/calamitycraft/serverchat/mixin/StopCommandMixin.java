package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.commands.StopCommand;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = StopCommand.class, remap = false)
public class StopCommandMixin {

    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;savePlayerStates()V"))
    private void savePlayerStates(ServerConfigurationManager instance) {
        instance.savePlayerStates();
        for (EntityPlayerMP playerEntity : instance.playerEntities) {
            playerEntity.playerNetServerHandler.kickPlayer(TextFormatting.ORANGE + "You have been disconnected from the server");
        }
    }
}
