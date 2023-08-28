package online.calamitycraft.serverchat.command;

import javax.annotation.Nullable;

public abstract class Command implements ICommand {

    private final String[] commandName;

    public Command(String... name) {
        this.commandName = name;
    }

    @Override
    public final String[] getCommandNames() {
        return this.commandName;
    }
}
