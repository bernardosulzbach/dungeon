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
package org.dungeon.help;

import org.dungeon.io.IO;
import org.dungeon.io.WriteStyle;
import org.dungeon.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Help {

    private static final List<CommandHelp> COMMANDS = new ArrayList<CommandHelp>();

    static {
        COMMANDS.add(new CommandHelp(
                "achievements",
                "",
                "Shows unlocked achievements.",
                "achievements"));

        COMMANDS.add(new CommandHelp(
                "commands",
                "",
                "Displays a list of valid commands.",
                "commands"));

        COMMANDS.add(new CommandHelp(
                "date",
                "",
                "Prints the current date.",
                "date"));

        COMMANDS.add(new CommandHelp(
                "destroy",
                "crash",
                "Destroys a item on the ground.",
                "destroy [item]"));

        COMMANDS.add(new CommandHelp(
                "drop",
                "",
                "Drops your current weapon.",
                "drop"));

        COMMANDS.add(new CommandHelp(
                "exit",
                "quit",
                "Exits the game.",
                "exit"));

        COMMANDS.add(new CommandHelp(
                "help", "?", "Displays the help text for a given command.", "help [command]"));

        COMMANDS.add(new CommandHelp(
                "hero", "me", "Shows the status of your hero.", "hero"));

        COMMANDS.add(new CommandHelp(
                "kill", "attack", "Attacks the target chosen by the player.", "kill [target]"));

        COMMANDS.add(new CommandHelp(
                "look", "peek", "Describes what your character can see.", "look"));

        COMMANDS.add(new CommandHelp(
                "pick", "loot", "Swap your current weapon.", "pick [item]"));

        COMMANDS.add(new CommandHelp(
                "rest", "", "Rest until you are completely healed.", "rest"));

        COMMANDS.add(new CommandHelp(
                "save", "", "Saves the game.", "save"));

        COMMANDS.add(new CommandHelp(
                "spawns", "", "Shows the spawn counters.", "spawns"));

        COMMANDS.add(new CommandHelp(
                "status", "", "Prints the hero's current status.", "status"));

        COMMANDS.add(new CommandHelp(
                "time", "", "Prints the current time.", "time"));

        COMMANDS.add(new CommandHelp(
                "walk", "go", "Move to the direction chosen.", "walk [direction]"));
    }

    /**
     * Print the a help string based on the specifiers.
     */
    public static void printCommandHelp(String[] words) {
        if (words.length == 1) {
            // There are no specifiers, report the correct usage of this method.
            IO.writeString(Constants.HELP_USAGE, WriteStyle.MARGIN);
        } else {

            for (CommandHelp command : COMMANDS) {
                if (command.equalsIgnoreCase(words[1])) {
                    // Output to toString method of the first command that matches the input.
                    IO.writeString(command.toString(), WriteStyle.COMMAND);
                    return;
                }
            }
            // There was no match.
            IO.writeString(String.format("No help text for '%s' could be found.", words[1]), WriteStyle.MARGIN);
        }
    }

    /**
     * Prints a list of commands based on the commands in COMMANDS.
     */
    public static void printCommandList() {
        StringBuilder builder = new StringBuilder();
        for (CommandHelp command : COMMANDS) {
            builder.append(command.toOneLineString());
            builder.append('\n');
        }
        IO.writeString(builder.toString(), WriteStyle.MARGIN);
    }

}
