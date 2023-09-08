package online.calamitycraft.serverchat.event;

import gaming.femboy.tinnitus.event.Event;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class PlayerJoinServerEvent extends Event {

    private final EntityPlayerMP player;

    public PlayerJoinServerEvent(EntityPlayerMP player) {
        this.player = player;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }
}
