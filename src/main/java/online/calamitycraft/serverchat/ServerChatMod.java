package online.calamitycraft.serverchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.entity.EntityLiving;
import online.calamitycraft.serverchat.command.Command;
import online.calamitycraft.serverchat.command.CommandProcessor;
import online.calamitycraft.serverchat.command.commands.AboutCommand;
import online.calamitycraft.serverchat.command.commands.KillCommand;
import online.calamitycraft.serverchat.command.commands.TpsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerChatMod implements ModInitializer {
    public static final String MOD_ID = "serverchat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static CommandProcessor commandProcessor = new CommandProcessor("/");

    @Override
    public void onInitialize() {
        commandProcessor.registerCommand(new AboutCommand());
        commandProcessor.registerCommand(new KillCommand());
        commandProcessor.registerCommand(new TpsCommand());
        LOGGER.info("ServerChat initialized.");
    }

    public static boolean isHoldingItem(EntityLiving e) {
        return e.getHeldItem() != null && e.getHeldItem().itemID != 0;
    }

    public static CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }
}
