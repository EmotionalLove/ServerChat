package online.calamitycraft.serverchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.entity.EntityLiving;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerChatMod implements ModInitializer {
    public static final String MOD_ID = "serverchat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("ServerChat initialized.");
    }

    public static boolean isHoldingItem(EntityLiving e) {
        return e.getHeldItem() != null && e.getHeldItem().itemID != 0;
    }
}
