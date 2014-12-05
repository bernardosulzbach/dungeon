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
package org.dungeon.game;

/**
 * Direction enum that implements all the possible movement directions in the game.
 * <p/>
 * Edited 09/10/2014 by mafagafogigante: implemented equalsIgnoreCase method and added abbreviations.
 *
 * @author Bernardo Sulzbach
 */
public enum Direction {

    NORTH("North", "N", 0, 1),
    EAST("East", "E", 1, 0),
    SOUTH("South", "S", 0, -1),
    WEST("West", "W", -1, 0);

    private final String name;
    private final String abbreviation;
    private final int x;
    private final int y;

    Direction(String name, String abbreviation, int x, int y) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    /**
     * @return the opposite direction.
     */
    public Direction invert() {
        return values()[(ordinal() + values().length / 2) % values().length];
    }

    public boolean equalsIgnoreCase(String str) {
        return name.equalsIgnoreCase(str) || abbreviation.equalsIgnoreCase(str);
    }

    @Override
    public String toString() {
        return name;
    }

}
