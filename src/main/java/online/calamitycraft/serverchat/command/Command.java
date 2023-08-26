package online.calamitycraft.serverchat.command;

public abstract class Command implements ICommand {

    private String commandName;

    public Command(String name) {
        this.commandName = name;
    }

    @Override
    public final String getCommandName(CommandProcessor processor, boolean withPrefix) {
        return withPrefix ? processor.commmandPrefix + this.commandName : this.commandName;
    }
}
