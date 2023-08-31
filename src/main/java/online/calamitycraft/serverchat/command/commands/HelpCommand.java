package online.calamitycraft.serverchat.command.commands;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.ServerChatMod;
import online.calamitycraft.serverchat.command.Command;
import online.calamitycraft.serverchat.command.ICommand;

import java.util.List;

public class HelpCommand extends Command {

    private boolean cached;
    private String[] cachedHelpMessage;

    public HelpCommand() {
        super("Display all commands", "help", "?", "commands");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        if (!cached) buildAndCache();
        for (String s : cachedHelpMessage) {
            player.addChatMessage(s);
        }
    }

    private void buildAndCache() {
        List<ICommand> li = ServerChatMod.getCommandProcessor().getCommandRegistry();
        String[] lines = new String[li.size() + 2];
        int cnt = 0;
        lines[cnt] = TextFormatting.BLUE + "--------------------------------------";
        cnt++;
        for (ICommand iCommand : li) {
            StringBuilder builder = new StringBuilder();
            builder
                    .append(TextFormatting.ORANGE.toString())
                    .append(ServerChatMod.getCommandProcessor().getCommmandPrefix())
                    .append(iCommand.getCommandNames()[0])
                    .append(TextFormatting.LIGHT_GRAY)
                    .append(" - ")
                    .append(iCommand.getCommandDescription());
            lines[cnt] = builder.toString();
            cnt++;
        }
        lines[cnt] = TextFormatting.BLUE + "--------------------------------------";
        cachedHelpMessage = lines;
        cached = true;
    }
}
