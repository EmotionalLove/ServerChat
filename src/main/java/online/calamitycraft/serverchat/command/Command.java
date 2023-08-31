package online.calamitycraft.serverchat.command;

public abstract class Command implements ICommand {

    private final String[] names;
    private final String description;

    public Command(String description, String... name) {
        this.names = name;
        this.description = description;
    }

    @Override
    public final String[] getCommandNames() {
        return this.names;
    }

    public String getCommandDescription() {
        return description;
    }
}
