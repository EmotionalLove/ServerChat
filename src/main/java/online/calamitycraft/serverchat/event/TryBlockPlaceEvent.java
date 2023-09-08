package online.calamitycraft.serverchat.event;

import gaming.femboy.tinnitus.event.CancellableEvent;
import net.minecraft.core.item.ItemStack;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class TryBlockPlaceEvent extends CancellableEvent {

    private final ItemStack tryStack;
    private final EntityPlayerMP player;
    public TryBlockPlaceEvent(EntityPlayerMP player, ItemStack tryStack) {
        this.player = player;
        this.tryStack = tryStack;
    }

    public ItemStack getTryStack() {
        return tryStack;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }
}
