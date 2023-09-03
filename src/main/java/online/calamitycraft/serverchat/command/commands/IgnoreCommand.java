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

public class IgnoreCommand extends Command {

    public IgnoreCommand() {
        super("Block or unblock chats and messages from a specific player", "ignore", "ignorehard");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        if (!hasArgs) {
            player.addChatMessage(TextFormatting.RED + "/" + this.getCommandNames()[0] + " <player>");
            return;
        }
        ChatFeaturePlayer senderFeaturePlayer = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(player.getDisplayName().toLowerCase());
        if (senderFeaturePlayer == null) {
            player.addChatMessage(TextFormatting.RED + "This feature is currently unavailable.");
            return;
        }
        String r = args[0];
        List<EntityPlayerMP> playerListSorted = new ArrayList<>(MinecraftServer.getInstance().configManager.playerEntities);
        playerListSorted.sort(Comparator.comparing(EntityPlayer::getDisplayName));
        for (EntityPlayerMP recipient : playerListSorted) {
            if (recipient.getDisplayName().equalsIgnoreCase(r)) {
                ignoreOrUnignore(player, senderFeaturePlayer, recipient);
                return;
            }
        }
        for (EntityPlayerMP recipient : playerListSorted) {
            if (recipient.getDisplayName().toLowerCase().startsWith(r.toLowerCase())) {
                ignoreOrUnignore(player, senderFeaturePlayer, recipient);
                return;
            }
        }
        player.addChatMessage(TextFormatting.RED + "Player search query failed for \"" + r + "\"");
    }

    private void ignoreOrUnignore(EntityPlayerMP player, ChatFeaturePlayer senderFeaturePlayer, EntityPlayerMP recipient) {
        if (recipient.getDisplayName().equalsIgnoreCase(player.getDisplayName())) {
            player.addChatMessage(TextFormatting.RED + "You cannot ignore yourself.");
            return;
        }
        if (senderFeaturePlayer.isPlayerIgnored(recipient)) {
            senderFeaturePlayer.unignorePlayer(recipient);
            File f = new File(ServerChatMod.chatFeatureDataFolder, player.getDisplayName().toLowerCase() + ".yml");
            senderFeaturePlayer.save(f, new YMLParser(f));
            player.addChatMessage(TextFormatting.ORANGE + recipient.getDisplayName() + TextFormatting.LIGHT_GRAY + " is no longer ignored.");
        } else {
            senderFeaturePlayer.ignorePlayer(recipient);
            File f = new File(ServerChatMod.chatFeatureDataFolder, player.getDisplayName().toLowerCase() + ".yml");
            senderFeaturePlayer.save(f, new YMLParser(f));
            player.addChatMessage(TextFormatting.ORANGE + recipient.getDisplayName() + TextFormatting.LIGHT_GRAY + " will now be ignored.");
        }
    }
}
