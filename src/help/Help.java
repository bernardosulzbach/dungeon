package help;

import game.Game;
import game.IO;

import java.util.ArrayList;

public class Help {

    // HashMap is not synchronized, what makes it better for non-threaded applications.
    /**
     * This HashMap maps all the aliases of a command to its CommandHelp object.
     */
    private static final ArrayList<CommandHelp> COMMANDS = new ArrayList<>();

    static {
        COMMANDS.add(new CommandHelp(
                "commands",
                null,
                "Display a list of valid commands.",
                "commands"));

        COMMANDS.add(new CommandHelp(
                "help",
                "?",
                "Display the help text for a given command.",
                "help [command]"));

        COMMANDS.add(new CommandHelp(
                "look",
                "peek",
                "Look around, describing what your character can see.",
                "look"));

    }

    /**
     * A complete command list.
     */
    public static final String COMMAND_LIST
            = "  Command List\n"
            + "    Exploration\n"
            + "      look                   Looks around, describing what the player sees.\n"
            + "      pick                   Pick an item from the ground..\n"
            + "      status                 Prints the player's character current status.\n"
            + "    Combat\n"
            + "      attack                 Enters target chooser to perform an attack.\n"
            + "      kill                   Enters target chooser to perform an attack.\n"
            + "    Other\n"
            + "      time                   Get the current time.\n"
            + "      date                   Get the current date.\n"
            + "      exit                   Exit the game.\n"
            + "        aliases: quit\n"
            + "      quit                   Exit the game.\n"
            + "      spawns                 Get the spawn counters.";

    // The correct usage of printHelp.
    private static final String PRINT_HELP_TEXT_USAGE = "  Usage: help (command)";

    /**
     * Print the a help string based on the specifiers.
     */
    public static void printCommandHelp(String[] words) {
        if (words.length == 1) {
            // There are no specifiers, report the correct usage of this method.
            IO.writeString(PRINT_HELP_TEXT_USAGE);
        } else {
            for (CommandHelp command : COMMANDS) {
                if (command.equalsIgnoreCase(words[1])) {
                    IO.writeString(command.toString());
                    return;
                }
            }
            // There was no match.
            IO.writeString(String.format("  No help text for '%s' could be found.", words[1]));
        }
    }

    /**
     * Prints a list of commands based on the commands in COMMANDS.
     */
    public static void printCommandList() {
        StringBuilder builder = new StringBuilder();
        for (CommandHelp command : COMMANDS) {
            builder.append(command.toOneLineString());
        }
        IO.writeString(builder.toString());
    }

}
