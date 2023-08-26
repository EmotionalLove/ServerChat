package online.calamitycraft.serverchat.command;

import net.minecraft.server.entity.player.EntityPlayerMP;

public interface ICommand {
    void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args);

    String getCommandName(CommandProcessor processor, boolean withPrefix);
}
