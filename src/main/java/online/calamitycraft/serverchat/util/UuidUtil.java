package online.calamitycraft.serverchat.util;

import net.minecraft.server.entity.player.EntityPlayerMP;
import org.shanerx.mojang.Mojang;

/**
 * Contains methods to communicate with the Mojang API to resolve usernames to UUID's and vice-versa.
 * <p>
 * These methods are used to fulfill the functionality of the Playerdata-by-UUID feature found in SaveHandlerServerMixin.java
 */
public class UuidUtil {

    public static String getUUID(EntityPlayerMP mp) {
        return getUUID(mp.getDisplayName());
    }

    public static String getUUID(String s) {
        Mojang api = new Mojang().connect();
        return api.getUUIDOfUsername(s);
    }

    public static String getUsername(String uuid) {
        Mojang api = new Mojang().connect();
        return api.getPlayerProfile(uuid).getUsername();
    }
}
