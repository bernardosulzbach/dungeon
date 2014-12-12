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
 * <p/>
 * Edited on 01/10/2014 by Bernardo Sulzbach : implemented a builder and aliases and arguments are now arrays.
 *
 * @author Bernardo Sulzbach
 */
public class CommandHelp {

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
     * Returns an array with the command name and all its aliases.
     */
    String[] getAllAliases() {
        String[] allAliases = new String[aliases.length + 1];
        allAliases[0] = name;
        System.arraycopy(aliases, 0, allAliases, 1, allAliases.length - 1);
        return allAliases;
    }

    /**
     * Verifies if any of the command aliases matches a string.
     */
    boolean equalsIgnoreCase(String command) {
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
        builder.append('\n').append('\n');
        if (info.isEmpty()) {
            builder.append(HelpConstants.NO_INFO);
        } else {
            builder.append("Info: ").append(info);
        }

        // Aliases
        builder.append('\n').append('\n');
        if (aliases[0].isEmpty()) {
            builder.append(HelpConstants.NO_ALIASES);
        } else {
            builder.append("Aliases: ");
            for (int i = 0; i < aliases.length; i++) {
                // If it is not the first usage example, append a comma.
                if (i != 0) {
                    builder.append(", ");
                }
                builder.append(aliases[i]);
            }
        }

        // Arguments (usage information)
        builder.append('\n').append('\n');
        builder.append("Usage: ");
        for (int i = 0; i < arguments.length; i++) {
            // If it is not the first usage example, append a comma.
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(name);
            if (!arguments[i].isEmpty()) {
                builder.append(" [").append(arguments[i]).append("]");
            }
        }

        // Return what it made.
        return builder.toString();
    }

    /**
     * Returns a String with the command's name and information.
     *
     * @return a String.
     */
    public String toOneLineString() {
        return toOneLineString(name);
    }

    /**
     * Returns a String with a given command's alias and information.
     *
     * @param alias the command's name or one of its aliases. Must be the name or an actual alias.
     * @return a String.
     */
    public String toOneLineString(String alias) {
        if (name.equalsIgnoreCase(alias)) {
            return String.format(Constants.COMMAND_HELP_FORMAT, alias, info);
        }
        for (String registeredAlias : aliases) {
            if (alias.equalsIgnoreCase(registeredAlias)) {
                return String.format(Constants.COMMAND_HELP_FORMAT, alias, info);
            }
        }
        throw new IllegalArgumentException("alias is not a registered alias.");
    }

}
