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
            return String.format("%s%-16s%-16s%s\n", Constants.MARGIN, name, alias, help);
        } else {
            return String.format("%s%-32s%s\n", Constants.MARGIN, name, help);
        }

    }

}
