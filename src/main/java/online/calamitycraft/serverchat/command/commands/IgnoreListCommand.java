package online.calamitycraft.serverchat.command.commands;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.command.Command;
import online.calamitycraft.serverchat.util.ChatFeaturePlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static online.calamitycraft.serverchat.command.commands.WhisperCommand.sendWhisper;

public class IgnoreListCommand extends Command {

    public IgnoreListCommand() {
        super("View who you're not seeing messages from", "ignorelist");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        ChatFeaturePlayer senderFeaturePlayer = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(player.getDisplayName().toLowerCase());
        if (senderFeaturePlayer == null) {
            player.addChatMessage(TextFormatting.RED + "This feature is currently unavailable.");
            return;
        }
        List<String> ignoreList = senderFeaturePlayer.getIgnoreList();
        if (ignoreList.isEmpty()) {
            player.addChatMessage(TextFormatting.RED + "Your ignore-list is empty.");
            return;
        }
        StringBuilder commaSeperated = new StringBuilder();
        ignoreList.forEach(i -> commaSeperated.append(i).append(", "));
        String finalSeperated = commaSeperated.toString().trim();
        finalSeperated = finalSeperated.substring(0, finalSeperated.length() - 1);
        player.addChatMessage(TextFormatting.LIGHT_GRAY + finalSeperated);
        player.addChatMessage(TextFormatting.ORANGE + "Ignoring " + ignoreList.size() + " player(s).");
    }
}
