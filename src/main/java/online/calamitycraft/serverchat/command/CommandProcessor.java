package online.calamitycraft.serverchat.command;

import net.minecraft.server.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {

    private List<ICommand> commandRegistry = new ArrayList<>();
    protected String commmandPrefix;

    public CommandProcessor(String commmandPrefix) {
        this.commmandPrefix = commmandPrefix;
    }

    public void registerCommand(ICommand command) {
        if (command == null) return;
        commandRegistry.add(command);
    }

    public boolean processCommand(EntityPlayerMP player, String input) throws Exception {
        input = input.trim();
        if (!input.startsWith(commmandPrefix)) return false; // wrong prefix
        for (ICommand command : this.commandRegistry) {
            if (commandMatches(command, input)) {
                boolean hasArgs = false;
                String[] args = input.split(" ");
                if (args.length > 1) {
                    String commandless = input.substring(input.indexOf(' ') + 1);
                    hasArgs = true;
                    List<String> list = new ArrayList<>();
                    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(commandless);
                    while (m.find()) {
                        list.add(m.group(1).replace("\"", ""));
                    }
                    args = list.toArray(new String[0]);
                }
                command.onCommand(player, hasArgs, hasArgs ? args : new String[0]);
                return true;
            }
        }
        return false; // command not found
    }

    private boolean commandMatches(ICommand cmd, String input) {
        for (String name : cmd.getCommandNames()) {
            if (input.equalsIgnoreCase(this.commmandPrefix + name) || input.toLowerCase().startsWith(this.commmandPrefix + name.toLowerCase() + " "))
                return true;
        }
        return false;

    }

}
