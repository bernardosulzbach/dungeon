/* 
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package help;

import game.IO;
import java.util.ArrayList;
import java.util.List;

public class Help {

    // HashMap is not synchronized, what makes it better for non-threaded applications.
    /**
     * This HashMap maps all the aliases of a command to its CommandHelp object.
     */
    private static final List<CommandHelp> COMMANDS = new ArrayList<>();

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
