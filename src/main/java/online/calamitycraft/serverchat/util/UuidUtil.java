package online.calamitycraft.serverchat.util;

import net.minecraft.server.entity.player.EntityPlayerMP;
import org.shanerx.mojang.Mojang;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains methods to communicate with the Mojang API to resolve usernames to UUID's and vice-versa.
 * <p>
 * These methods are used to fulfill the functionality of the Playerdata-by-UUID feature found in SaveHandlerServerMixin.java
 */
public class UuidUtil {

    private static Map<String, String> uuidCache = new HashMap<>();

    public static String getUUID(EntityPlayerMP mp) {
        return getUUID(mp.getDisplayName());
    }

    public static String getUUID(String s) {
        return getUUID(s, true);
    }

    public static String getUUID(String s, boolean allowCache) {
        if (allowCache) {
            if (uuidCache.containsKey(s.toLowerCase())) {
                return uuidCache.get(s.toLowerCase());
            }
        }
        Mojang api = new Mojang().connect();
        String uuid = api.getUUIDOfUsername(s);
        if (uuid != null) {
            uuidCache.put(s.toLowerCase(), uuid);
        }
        return uuid;
    }

    public static String getUsername(String uuid) {
        Mojang api = new Mojang().connect();
        return api.getPlayerProfile(uuid).getUsername();
    }
}
