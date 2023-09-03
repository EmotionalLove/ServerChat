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

public class ReplyCommand extends Command {

    public ReplyCommand() {
        super("Reply to who last sent you a private message", "reply", "r");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        if (!hasArgs) {
            player.addChatMessage(TextFormatting.RED + "/" + this.getCommandNames()[0] + " <message...>");
            return;
        }
        ChatFeaturePlayer senderFeaturePlayer = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(player.getDisplayName().toLowerCase());
        if (senderFeaturePlayer == null) {
            player.addChatMessage(TextFormatting.RED + "This feature is currently unavailable.");
            return;
        }
        if (senderFeaturePlayer.getLastRecievedMessageFrom() == null) {
            player.addChatMessage(TextFormatting.RED + "You have not yet received a private message this session.");
            return;
        }
        List<EntityPlayerMP> playerListSorted = new ArrayList<>(MinecraftServer.getInstance().configManager.playerEntities);
        playerListSorted.sort(Comparator.comparing(EntityPlayer::getDisplayName));
        for (EntityPlayerMP recipient : playerListSorted) {
            if (recipient.getDisplayName().toLowerCase().startsWith(senderFeaturePlayer.getLastRecievedMessageFrom())) {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                sendWhisper(player, message, recipient);
                return;
            }
        }
    }
}
