package online.calamitycraft.serverchat.event;

import gaming.femboy.tinnitus.event.CancellableEvent;
import net.minecraft.core.net.packet.Packet10Flying;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class PlayerTryMoveEvent extends CancellableEvent {

    private final EntityPlayerMP player;
    private boolean onGround;
    private boolean moving;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public PlayerTryMoveEvent(EntityPlayerMP player, Packet10Flying pck) {
        this.player = player;
        this.onGround = pck.onGround;
        this.moving = pck.moving;
        this.xPosition = pck.xPosition;
        this.yPosition = pck.yPosition;
        this.zPosition = pck.zPosition;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isMoving() {
        return moving;
    }

    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public double getzPosition() {
        return zPosition;
    }
}
