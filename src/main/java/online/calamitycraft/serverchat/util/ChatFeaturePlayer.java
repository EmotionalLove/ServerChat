package online.calamitycraft.serverchat.util;

import net.minecraft.server.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.*;

public class ChatFeaturePlayer {

    public static Map<String, ChatFeaturePlayer> chatFeaturePlayerRegistry = new HashMap<>();

    private String lastRecievedMessageFrom;
    private String lastSentMessageTo;
    private List<String> ignoreList;
    private boolean chatEnabled;
    private boolean deathMessagesEnabled;

    public ChatFeaturePlayer() {
        ignoreList = new ArrayList<>();
        chatEnabled = true;
        deathMessagesEnabled = true;
    }

    public ChatFeaturePlayer(YMLParser loadedFile) {
        ignoreList = loadedFile.getStringList("ignore-list");
        chatEnabled = loadedFile.getBoolean("chat-enabled");
        deathMessagesEnabled = loadedFile.getBoolean("dmsg-enabled");
    }

    public String getLastRecievedMessageFrom() {
        return lastRecievedMessageFrom;
    }

    public String getLastSentMessageTo() {
        return lastSentMessageTo;
    }

    public void setLastRecievedMessageFrom(String lastRecievedMessageFrom) {
        this.lastRecievedMessageFrom = lastRecievedMessageFrom;
    }

    public void setLastSentMessageTo(String lastSentMessageTo) {
        this.lastSentMessageTo = lastSentMessageTo;
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public void setChatEnabled(boolean bool) {
        this.chatEnabled = bool;
    }

    public boolean isDeathMessagesEnabled() {
        return deathMessagesEnabled;
    }

    public boolean isPlayerIgnored(String player) {
        return ignoreList.contains(player.toLowerCase());
    }
    public boolean isPlayerIgnored(EntityPlayerMP player) {
        return isPlayerIgnored(player.getDisplayName());
    }

    public List<String> getIgnoreList() {
        return Collections.unmodifiableList(ignoreList);
    }

    public boolean ignorePlayer(EntityPlayerMP player) {
        if (isPlayerIgnored(player)) return false;
        return ignoreList.add(player.getDisplayName().toLowerCase());
    }
    public boolean unignorePlayer(EntityPlayerMP player) {
        if (!isPlayerIgnored(player)) return false;
        return ignoreList.remove(player.getDisplayName().toLowerCase());
    }

    public boolean save(File file, YMLParser loadedFile) {
         loadedFile.set("ignore-list", ignoreList);
         loadedFile.set("chat-enabled", isChatEnabled());
         loadedFile.set("dmsg-enabled", isDeathMessagesEnabled());
         return loadedFile.save(file);
    }
    public boolean save(YMLParser loadedFile) {
         loadedFile.set("ignore-list", ignoreList);
         loadedFile.set("chat-enabled", isChatEnabled());
         loadedFile.getBoolean("dmsg-enabled", isDeathMessagesEnabled());
         return loadedFile.save();
    }

    public static boolean isValidFormat(YMLParser parser) {
        boolean flag = (parser.get("ignore-list") instanceof List<?>);
        boolean flag1 = (parser.get("chat-enabled") instanceof Boolean);
        boolean flag2 = (parser.get("dmsg-enabled") instanceof Boolean);
        return flag && flag1 && flag2;
    }
}
