package online.calamitycraft.serverchat;

import gaming.femboy.tinnitus.Listener;
import gaming.femboy.tinnitus.Reactor;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import online.calamitycraft.serverchat.event.PlayerJoinServerEvent;
import online.calamitycraft.serverchat.event.TryBlockPlaceEvent;

public class NoIllegalFeature implements Listener {

    /**
     * Array of problematic block ids that shouldn't end up in inventories.
     */
    private int[] illegalIdArr = new int[]{};

    public NoIllegalFeature() {
        illegalIdArr = ServerChatMod.config.getIllegalArr(illegalIdArr);
    }
    private final Reactor<?> onBlockPlace = new Reactor<TryBlockPlaceEvent>(event -> {
        for (int i : illegalIdArr) {
            if (event.getTryStack().itemID == i) {
                event.setCancelled(true);
                event.getPlayer().inventory.setCurrentItem(new ItemStack(Block.stone, 1), true);
                event.getPlayer().playerNetServerHandler.kickPlayer("Illegal Item");
            }
            if (event.getTryStack().stackSize > event.getTryStack().getMaxStackSize()) {
                event.setCancelled(true);
                event.getPlayer().inventory.setCurrentItem(new ItemStack(Block.stone, 1), true);
                event.getPlayer().playerNetServerHandler.kickPlayer("Illegal Item");
            }
        }
    }).register(ServerChatMod.getEventManager(), this);
    private final Reactor<?> onJoinServer = new Reactor<PlayerJoinServerEvent>(event -> {
        for (int i = 0; i < event.getPlayer().inventory.mainInventory.length; i++) {
            ItemStack itemStack = event.getPlayer().inventory.mainInventory[i];
            if (itemStack == null) continue;
            for (int ii : illegalIdArr) {
                if (itemStack.itemID == ii) {
                    event.getPlayer().inventory.mainInventory[i] = new ItemStack(Block.stone, 1);
                    ServerChatMod.LOGGER.error(event.getPlayer().getDisplayName() + " logged on with illegal items");
                }
                if (itemStack.stackSize > itemStack.getMaxStackSize()) {
                    event.getPlayer().inventory.mainInventory[i] = new ItemStack(Block.stone, 1);
                    ServerChatMod.LOGGER.error(event.getPlayer().getDisplayName() + " logged on with illegal items");
                }
            }
        }
        for (int i = 0; i < event.getPlayer().inventory.armorInventory.length; i++) {
            ItemStack itemStack = event.getPlayer().inventory.mainInventory[i];
            if (itemStack == null) continue;
            for (int ii : illegalIdArr) {
                if (itemStack.itemID == ii) {
                    event.getPlayer().inventory.mainInventory[i] = null;
                    ServerChatMod.LOGGER.error(event.getPlayer().getDisplayName() + " logged on with illegal items");
                }
                if (itemStack.stackSize > itemStack.getMaxStackSize()) {
                    event.getPlayer().inventory.mainInventory[i] = null;
                    ServerChatMod.LOGGER.error(event.getPlayer().getDisplayName() + " logged on with illegal items");
                }
            }
        }
    }).register(ServerChatMod.getEventManager(), this);

}
