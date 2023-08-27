package online.calamitycraft.serverchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.entity.EntityLiving;
import online.calamitycraft.serverchat.command.Command;
import online.calamitycraft.serverchat.command.CommandProcessor;
import online.calamitycraft.serverchat.command.commands.*;
import online.calamitycraft.serverchat.util.TickrateUtil;
import online.calamitycraft.serverchat.util.YMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ServerChatMod implements ModInitializer {
    public static final String MOD_ID = "serverchat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static File chatFeatureDataFolder;
    public static boolean chatFeatureReady = false;

    private static CommandProcessor commandProcessor = new CommandProcessor("/");

    @Override
    public void onInitialize() {
        commandProcessor.registerCommand(new AboutCommand());
        commandProcessor.registerCommand(new KillCommand());
        commandProcessor.registerCommand(new TpsCommand(new TickrateUtil()));
        //
        File file = new File("chat_feature_data");
        boolean success = file.mkdir();
        chatFeatureDataFolder = file;
        if (success || file.isDirectory()) {
            LOGGER.info("Chat feature is ready.");
            commandProcessor.registerCommand(new WhisperCommand());
            commandProcessor.registerCommand(new WhisperCommand$1());
            commandProcessor.registerCommand(new WhisperCommand$2());
            commandProcessor.registerCommand(new WhisperCommand$3());
            chatFeatureReady = true;
        }
        LOGGER.info("ServerChat initialized.");
    }

    public static boolean isHoldingItem(EntityLiving e) {
        return e.getHeldItem() != null && e.getHeldItem().itemID != 0;
    }

    public static CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }
}
