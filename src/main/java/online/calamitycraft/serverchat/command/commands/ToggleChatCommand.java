package online.calamitycraft.serverchat.command.commands;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.command.Command;
import online.calamitycraft.serverchat.util.ChatFeaturePlayer;
import online.calamitycraft.serverchat.util.YMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ToggleChatCommand extends Command {

    public ToggleChatCommand() {
        super("Disable or enable global chat.", "togglechat", "tc");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        ChatFeaturePlayer senderFeaturePlayer = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(player.getDisplayName().toLowerCase());
        if (senderFeaturePlayer == null) {
            player.addChatMessage(TextFormatting.RED + "This feature is currently unavailable.");
            return;
        }
        senderFeaturePlayer.setChatEnabled(!senderFeaturePlayer.isChatEnabled());
        File f = new File(ServerChatMod.chatFeatureDataFolder, player.getDisplayName().toLowerCase() + ".yml");
        senderFeaturePlayer.save(f, new YMLParser(f));
        if (senderFeaturePlayer.isChatEnabled()) {
            player.addChatMessage(TextFormatting.LIGHT_GRAY + "Global chat has been " + TextFormatting.ORANGE + "enabled");
        } else {
            player.addChatMessage(TextFormatting.LIGHT_GRAY + "Global chat has been " + TextFormatting.ORANGE + "disabled");
        }
    }
}
