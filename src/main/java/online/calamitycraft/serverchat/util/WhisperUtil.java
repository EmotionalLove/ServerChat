package online.calamitycraft.serverchat.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.core.util.helper.AES;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.ServerChatMod;
import org.w3c.dom.Text;

import java.util.*;

public class WhisperUtil {

    private Map<String, String> replyMap = new HashMap<>();
    private Map<String, String> lastMap = new HashMap<>();

    private static final char[] smallChars = new char[]{'i', 'l', 'j', ',', '.', '\'', '\"', '[', '{', ']', '}', '!'};
    private final static int largeCharWidth = 14;
    private final static int normalCharWidth = 12;
    private final static int smallCharWidth = 6;
    public final static int maxWidth = 633;

    /**
     * This method will analyse `String s`, and always return an array with a length of 2
     * This method is used to determine whether the given String will fit in the vanilla Minecraft chat UI without wrapping.
     *
     * @param s The string to be analysed
     * @return [0] will be the length in pixels of the given String, [1] will be the character the String needs to be split at to avoid wrapping.
     */
    public static int[] getStringWidth(String s) {
        int count = 0;
        int wrapPosition = -1;
        for (int i = 0; i < s.toCharArray().length; i++) {
            char c = s.toCharArray()[i];
            if (c == '\247') continue;
            boolean flag = false;
            for (char smallChar : smallChars) {
                if (c == smallChar) {
                    flag = true;
                    break;
                }
            }
            count += flag ? smallCharWidth : normalCharWidth;
            if (Character.isUpperCase(c)) {
                count += (largeCharWidth - normalCharWidth);
            }
            if (count >= maxWidth) {
                wrapPosition = i - 1;
            }
        }
        return new int[]{count, wrapPosition};
    }

    /**
     * This method will take the given String, and automatically split and preserve Chat Colors.
     * This method will help you avoid client-side word wrapping, which resets the Chat Colors on every line.
     * Splitting a message into multiple lines can help you avoid this.
     *
     * @param s The string to split
     * @return a list, in order, of the lines you need to send to the player.
     */
    public static List<String> slice(String s) {
        int count = 0;
        int wrapPosition = 0;
        String lastColour = "\247r";
        List<String> list = new ArrayList<>();
        if (getStringWidth(s)[1] == -1) {
            list.add(s);
            return list; // does not need slicing
        }
        char[] arr = s.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (i + 1 < arr.length) {
                String col = c + String.valueOf(arr[i + 1]);
                if (col.matches("§\\S")) {
                    lastColour = col;
                    i++;
                    continue;
                }
            }
            boolean flag = false;
            for (char smallChar : smallChars) {
                if (c == smallChar) {
                    flag = true;
                    break;
                }
            }
            count += flag ? smallCharWidth : normalCharWidth;
            if (Character.isUpperCase(c)) {
                count += (largeCharWidth - normalCharWidth);
            }
            if (count >= maxWidth) {
                list.add(lastColour + s.substring(wrapPosition, i - 1));
                wrapPosition = i - 1;
                count = 0;
            }
        }
        if (s.length() > wrapPosition) list.add(lastColour + s.substring(wrapPosition));
        return list;
    }

    public static void sendEncryptedChatToPlayers(EntityPlayerMP sender, String message) {
        message = message.trim().replaceAll("\\s+", " ");
        List<EntityPlayerMP> playerEntities = new ArrayList<>(MinecraftServer.getInstance().configManager.playerEntities);
        ChatFeaturePlayer senderChatFeaturePlayer = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(sender.getDisplayName().toLowerCase());
        if (senderChatFeaturePlayer == null) {
            sender.addChatMessage(TextFormatting.RED + "You cannot send messages due to an error.");
            sender.addChatMessage(TextFormatting.RED + "A re-log may re-enable chat.");
            return;
        }
        if (!senderChatFeaturePlayer.isChatEnabled()) {
            sender.addChatMessage(TextFormatting.RED + "Your global chat is disabled.");
            sender.addChatMessage(TextFormatting.RED + "Run /togglechat or /tc to re-enable.");
            return;
        }
        for (EntityPlayerMP playerEntity : MinecraftServer.getInstance().configManager.playerEntities) {
            if (playerEntity.equals(sender)) continue;
            ChatFeaturePlayer i = ChatFeaturePlayer.chatFeaturePlayerRegistry.get(playerEntity.getDisplayName().toLowerCase());
            if (i == null) continue;
            if (!i.isChatEnabled() || i.isPlayerIgnored(sender)) playerEntities.remove(playerEntity);
            else if (senderChatFeaturePlayer.isPlayerIgnored(playerEntity)) playerEntities.remove(playerEntity);
        }
        if (WhisperUtil.getStringWidth(message)[1] != -1) {
            List<String> ss = WhisperUtil.slice(message);
            ss.forEach(split -> sendEncrypted(playerEntities, split.trim()));
        } else sendEncrypted(playerEntities, message);

    }

    public static void sendEncrypted(List<EntityPlayerMP> playerList, String message) {
        for (EntityPlayerMP entityplayermp : playerList) {
            entityplayermp.playerNetServerHandler.sendPacket(new Packet3Chat(message, AES.keyChain.get(entityplayermp.username)));
        }
    }

}
