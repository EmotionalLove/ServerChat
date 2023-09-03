package online.calamitycraft.serverchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.EntityLiving;
import online.calamitycraft.serverchat.command.CommandProcessor;
import online.calamitycraft.serverchat.command.commands.*;
import online.calamitycraft.serverchat.util.ConfigUtil;
import online.calamitycraft.serverchat.util.Threads;
import online.calamitycraft.serverchat.util.TickrateUtil;
import online.calamitycraft.serverchat.util.WhisperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ServerChatMod implements ModInitializer {
    public static final String MOD_ID = "serverchat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static File chatFeatureDataFolder;

    public static ConfigUtil config;
    public static boolean chatFeatureReady = false;

    private static CommandProcessor commandProcessor = new CommandProcessor("/");
    public static WhisperUtil whisperUtil;

    @Override
    public void onInitialize() {
        Threads.init();
        try {
            initConfig();
        } catch (IOException x) {
            x.printStackTrace(); //todo
        }

        commandProcessor.registerCommand(new HelpCommand());
        commandProcessor.registerCommand(new KillCommand());
        commandProcessor.registerCommand(new TpsCommand(new TickrateUtil()));
        //
        File file = new File("chat_feature_data");
        boolean success = file.mkdir();
        chatFeatureDataFolder = file;
        if (success || file.isDirectory()) {
            LOGGER.info("Chat feature is ready.");
            commandProcessor.registerCommand(new WhisperCommand());
            commandProcessor.registerCommand(new ReplyCommand());
            commandProcessor.registerCommand(new LastCommand());
            commandProcessor.registerCommand(new IgnoreCommand());
            commandProcessor.registerCommand(new IgnoreListCommand());
            commandProcessor.registerCommand(new ToggleChatCommand());
            chatFeatureReady = true;
        }
        //whisperUtil = new WhisperUtil(Minecraft.getMinecraft(this));
        LOGGER.info("ServerChat initialized.");
    }

    private void initConfig() throws IOException {
        config = new ConfigUtil();
    }

    public static boolean isHoldingItem(EntityLiving e) {
        return e.getHeldItem() != null && e.getHeldItem().itemID != 0;
    }

    public static CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }
}
