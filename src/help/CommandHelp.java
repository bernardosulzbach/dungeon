package help;

/**
 * @author Bernardo Sulzbach
 */
public class CommandHelp {

    /**
     * The command "official" name.
     */
    private final String name;
    /**
     * [OPTIONAL] An alias to the command.
     */
    private final String alias;
    /**
     * A brief comment on what the command does.
     */
    private final String help;
    /**
     * [OPTIONAL] An example of the proper syntax for the command.
     */
    private final String usage;

    public CommandHelp(String name, String alias, String help, String usage) {
        this.name = name;
        this.alias = alias;
        this.help = help;
        this.usage = usage;
    }

    /**
     * Verifies if any of the command aliases matches a string.
     */
    protected boolean equalsIgnoreCase(String command) {
        if (alias != null) {
            return name.equalsIgnoreCase(command) || alias.equalsIgnoreCase(command);
        } else {
            return name.equalsIgnoreCase(command);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("    ").append(name);
        builder.append("\n\n").append("    ").append("Action: ").append(help);
        if (usage != null) {
            builder.append("\n\n").append("    ").append("Usage: ").append(usage);
        }
        if (alias != null) {
            builder.append("\n\n").append("    ").append("Alias: ").append(alias);
        }
        return builder.toString();
    }

    public String toOneLineString() {
        if (alias != null) {
            return String.format("  %-10s%-10s%s\n", name, alias, help);
        } else {
            return String.format("  %-20s%s\n", name, help);
        }

    }

}
