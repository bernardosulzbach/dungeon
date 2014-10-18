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
package org.dungeon.core.game;

/**
 * Direction enum that implements all the possible movement directions in the game.
 *
 * Edited 09/10/2014 by mafagafogigante: implemented equalsIgnoreCase method and added abbreviations.
 *
 * @author Bernardo Sulzbach
 */
public enum Direction implements Selectable {

    NORTH("North", "N"), EAST("East", "E"), SOUTH("South", "S"), WEST("West", "W");

    private final String name;
    private final String abbreviation;

    Direction(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public boolean equalsIgnoreCase(String str) {
        return name.equalsIgnoreCase(str) || abbreviation.equalsIgnoreCase(str);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toSelectionEntry() {
        return name;
    }
}
