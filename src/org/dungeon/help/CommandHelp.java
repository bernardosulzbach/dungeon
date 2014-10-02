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

import org.dungeon.utils.Constants;

/**
 * CommandHelp class that defines a common structure of the help strings for a command and a few methods that enable
 * easy printing of those strings.
 *
 * Edited on 01/10/2014 by Bernardo Sulzbach : implemented a builder and aliases and arguments are now arrays.
 *
 * @author Bernardo Sulzbach
 */
public class    CommandHelp {

    // Do not set any of the field to null. Use a blank string ("") instead.
    /**
     * The command "official" name.
     */
    private final String name;
    /**
     * A brief comment on what the command does.
     */
    private final String info;
    /**
     * An alias to the command.
     */
    private final String[] aliases;
    /**
     * An example of the proper syntax for the command.
     */
    private final String[] arguments;

    public CommandHelp(String name, String info, String[] aliases, String[] arguments) {
        this.name = name;
        this.info = info;
        this.aliases = aliases;
        this.arguments = arguments;
    }

    /**
     * Verifies if any of the command aliases matches a string.
     */
    protected boolean equalsIgnoreCase(String command) {
        if (name.equalsIgnoreCase(command)) {
            return true;
        }
        // Name was not equal, perform a linear search on the aliases.
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Name
        // Should never be an empty string.
        builder.append(name);

        // Info
        builder.append('\n');
        if (info.isEmpty()) {
            builder.append(HelpConstants.NO_INFO);
        } else {
            builder.append("Info: ").append(info);
        }

        // Aliases
        builder.append('\n');
        if (aliases.length == 0) {
            builder.append(HelpConstants.NO_ALIASES);
        } else {
            builder.append("Aliases: ");
            // Append the first alias before the loop so we avoid preceding commas.
            builder.append(aliases[0]);
            for (int i = 1; i < aliases.length; i++) {
                builder.append(", ").append(aliases[i]);
            }
        }

        // Arguments (usage information)
        builder.append('\n');
        if (arguments.length == 0) {
            builder.append(HelpConstants.NO_ARGUMENTS);
        } else {
            builder.append("Usage: ");
            // Append the first alias before the loop so we avoid preceding commas.
            builder.append(name).append(" [").append(arguments[0]).append("]");
            for (int i = 1; i < arguments.length; i++) {
                builder.append(", ").append(name).append(" [").append(arguments[i]).append("]");
            }
        }

        // Return what it made.
        return builder.toString();
    }

    public String toOneLineString() {
        // TODO: use the aliases (or at least one of them) too.
        return String.format(Constants.SELECTION_ENTRY_FORMAT, name, "", info);
    }

}
