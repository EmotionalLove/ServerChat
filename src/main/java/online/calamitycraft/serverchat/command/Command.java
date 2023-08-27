package online.calamitycraft.serverchat.command;

import javax.annotation.Nullable;

public abstract class Command implements ICommand {

    private String commandName;

    public Command(String name) {
        this.commandName = name;
    }

    @Override
    public final String getCommandName(@Nullable CommandProcessor processor, boolean withPrefix) {
        return (withPrefix && processor != null) ? processor.commmandPrefix + this.commandName : this.commandName;
    }
}
