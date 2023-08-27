package online.calamitycraft.serverchat.util;

import net.minecraft.server.entity.player.EntityPlayerMP;
import org.shanerx.mojang.Mojang;

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
