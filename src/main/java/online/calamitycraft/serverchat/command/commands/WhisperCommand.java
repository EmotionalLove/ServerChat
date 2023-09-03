package online.calamitycraft.serverchat.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.command.Command;
import online.calamitycraft.serverchat.util.ChatFeaturePlayer;
import online.calamitycraft.serverchat.util.WhisperUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WhisperCommand extends Command {

    public WhisperCommand() {
        super("Privately message another player", "tell", "whisper", "msg", "w", "message");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        if (!hasArgs || args.length < 2) {
            player.addChatMessage(TextFormatting.RED + "/" + this.getCommandNames()[0] + " <recipient> <message...>");
            return;
        }
        String r = args[0];
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        List<EntityPlayerMP> playerListSorted = new ArrayList<>(MinecraftServer.getInstance().configManager.playerEntities);
        playerListSorted.sort(Comparator.comparing(EntityPlayer::getDisplayName));
        for (EntityPlayerMP recipient : playerListSorted) {
            if (recipient.getDisplayName().equalsIgnoreCase(r)) {
                sendWhisper(player, message, recipient);
                return;
            }
        }
        for (EntityPlayerMP recipient : playerListSorted) {
            if (recipient.getDisplayName().toLowerCase().startsWith(r.toLowerCase())) {
                sendWhisper(player, message, recipient);
                return;
            }
        }
        player.addChatMessage(TextFormatting.RED + "Player search query failed for \"" + r + "\"");
    }

    protected static void sendWhisper(EntityPlayerMP player, StringBuilder message, EntityPlayerMP recipient) {
        if (recipient.getDisplayName().equalsIgnoreCase(player.getDisplayName())) {
            player.addChatMessage(TextFormatting.RED + "You cannot message yourself.");
            return;
        }
        ChatFeaturePlayer recipientFeaturePlayer = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(recipient.getDisplayName().toLowerCase());
        ChatFeaturePlayer senderFeaturePlayer = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(player.getDisplayName().toLowerCase());
        if (recipientFeaturePlayer == null) {
            player.addChatMessage(TextFormatting.RED + "This player cannot be messaged at this time.");
            return;
        }
        if (senderFeaturePlayer == null) {
            player.addChatMessage(TextFormatting.RED + "This feature is currently unavailable.");
            return;
        }
        if (recipientFeaturePlayer.isPlayerIgnored(player)) {
            player.addChatMessage(TextFormatting.RED + recipient.getDisplayName() + " is ignoring you.");
            return;
        }
        if (senderFeaturePlayer.isPlayerIgnored(recipient)) {
            player.addChatMessage(TextFormatting.RED + "You are ignoring " + recipient.getDisplayName() + ".");
            return;
        }
        senderFeaturePlayer.setLastSentMessageTo(recipient.getDisplayName());
        recipientFeaturePlayer.setLastRecievedMessageFrom(player.getDisplayName());
        recipient.addChatMessage(TextFormatting.PINK + player.getDisplayName() + " whispers " + TextFormatting.PURPLE + "\"" + message.toString().trim() + "\"");
        player.addChatMessage(TextFormatting.PINK + "You whisper to " + recipient.getDisplayName() + TextFormatting.PURPLE + " \"" + message.toString().trim() + "\"");
    }
}
