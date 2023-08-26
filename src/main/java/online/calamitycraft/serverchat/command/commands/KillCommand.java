package online.calamitycraft.serverchat.command.commands;

import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.command.Command;

public class KillCommand extends Command {

    public KillCommand() {
        super("kill");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        player.animateHurt();
        player.onDeath(player);
        player.health = 0;
    }
}
