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
 * @author Bernardo Sulzbach
 */
public class CommandHelp {

    // Do not set any of the field to null. Use a blank string ("") instead.
    /**
     * The command "official" name.
     */
    private final String name;
    /**
     * An alias to the command.
     */
    private final String alias;
    /**
     * A brief comment on what the command does.
     */
    private final String help;
    /**
     * An example of the proper syntax for the command.
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
        if (!alias.isEmpty()) {
            return name.equalsIgnoreCase(command) || alias.equalsIgnoreCase(command);
        } else {
            return name.equalsIgnoreCase(command);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append('\n').append("Action: ").append(help);
        if (usage.isEmpty()) {
            builder.append('\n').append(Constants.NO_USAGE_INFORMATION);
        } else {
            builder.append('\n').append("Usage: ").append(usage);
        }
        if (!alias.isEmpty()) {
            builder.append('\n').append("Alias: ").append(alias);
        }
        return builder.toString();
    }

    public String toOneLineString() {
        return String.format(Constants.SELECTION_ENTRY_FORMAT, name, alias, help);
    }

}
