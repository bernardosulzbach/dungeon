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
package org.dungeon.io;

/**
 * Enumerated type that provides a set of different writing styles that dictate how an output string will be formatted.
 * 
 * @author Bernardo Sulzbach
 */
public enum WriteStyle {
    /**
     * Add a blank line between text lines and use Constants.MARGIN twice before each text line.
     */
    COMMAND,
    /**
     * Add a Constants.MARGIN before all non-empty lines.
     */
    MARGIN,
    /**
     * Add Constants.WARNING before the line (warnings should not have multiple lines).
     */
    WARNING,
    /**
     * Does not modify the provided string before printing it.
     */
    NONE;
}
