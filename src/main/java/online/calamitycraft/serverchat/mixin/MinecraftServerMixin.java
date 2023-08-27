package online.calamitycraft.serverchat.mixin;

import net.minecraft.server.MinecraftServer;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.command.commands.TpsCommand;
import online.calamitycraft.serverchat.util.TickrateUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixin {

    @Inject(method = "doTick", at = @At("HEAD"))
    private void doTick(CallbackInfo ci) {
        TpsCommand.tpsUtil.measure();
    }
}
