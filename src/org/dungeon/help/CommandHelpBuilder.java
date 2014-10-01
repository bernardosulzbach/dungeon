package org.dungeon.help;

// Generated builder for CommandHelp.
public class CommandHelpBuilder {
    private String name;
    private String info;
    private String[] aliases;
    private String[] arguments;

    public CommandHelpBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CommandHelpBuilder setInfo(String info) {
        this.info = info;
        return this;
    }

    public CommandHelpBuilder setAliases(String[] aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandHelpBuilder setArguments(String[] arguments) {
        this.arguments = arguments;
        return this;
    }

    public CommandHelp createCommandHelp() {
        return new CommandHelp(name, info, aliases, arguments);
    }
}