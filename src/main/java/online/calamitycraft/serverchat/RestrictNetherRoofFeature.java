package online.calamitycraft.serverchat;

import gaming.femboy.tinnitus.Listener;
import gaming.femboy.tinnitus.Reactor;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import online.calamitycraft.serverchat.event.PlayerJoinServerEvent;
import online.calamitycraft.serverchat.event.PlayerTryMoveEvent;
import online.calamitycraft.serverchat.event.TryBlockPlaceEvent;
import online.calamitycraft.serverchat.util.SpawnUtil;

public class RestrictNetherRoofFeature implements Listener {

    public RestrictNetherRoofFeature() {

    }
    private final Reactor<?> onMove = new Reactor<PlayerTryMoveEvent>(event -> {
        if (!event.isMoving() || !event.getPlayer().isAlive()) return;
        if (event.getPlayer().world.dimension.id == 1 && event.getyPosition() > 255) {
            int newY = SpawnUtil.findHighestSolidBlockNether(event.getPlayer().world, (int) event.getxPosition(), (int) event.getzPosition());
            if (newY == -1) {
                event.getPlayer().animateHurt();
                event.getPlayer().onDeath(null);
                event.getPlayer().health = 0;
            }
            event.getPlayer().playerNetServerHandler.teleportTo(event.getxPosition(), newY, event.getzPosition(), event.getPlayer().yRot, event.getPlayer().xRot);
        }
    }).register(ServerChatMod.getEventManager(), this);

}
