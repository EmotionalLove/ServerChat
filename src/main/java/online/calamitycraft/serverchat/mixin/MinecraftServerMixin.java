package online.calamitycraft.serverchat.mixin;

import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.core.world.save.SaveHandlerServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.WorldServer;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.command.commands.TpsCommand;
import online.calamitycraft.serverchat.util.TickrateUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixin {

    @Shadow public WorldServer[] worldMngr;

    @Inject(method = "doTick", at = @At("HEAD"))
    private void doTick(CallbackInfo ci) {
        TpsCommand.tpsUtil.measure();
    }

    @Inject(method = "initWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/ServerConfigurationManager;setPlayerManager([Lnet/minecraft/server/world/WorldServer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void initWorld(ISaveFormat saveFormat, String worldDirName, long l, CallbackInfo ci, SaveHandlerServer saveHandler, int i) {
        this.worldMngr[i].sleepPercent = ServerChatMod.config.getSleepPercent(50);
    }
}
