package online.calamitycraft.serverchat.command.commands;

import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.command.Command;

public class AboutCommand extends Command {

    public AboutCommand() {
        super("about");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        player.addChatMessage("Test");
    }
}
