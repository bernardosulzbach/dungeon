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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Direction enum that implements all the possible movement directions in the game.
 */
public enum Direction {

  // This order is of uttermost importance and some methods in this class DEPEND on it.
  // Do not change without checking all the methods of this enum.
  UP("Up", "U", new Point(0, 0, 1)),
  NORTH("North", "N", new Point(0, 1, 0)),
  EAST("East", "E", new Point(1, 0, 0)),
  DOWN("Down", "D", new Point(0, 0, -1)),
  SOUTH("South", "S", new Point(0, -1, 0)),
  WEST("West", "W", new Point(-1, 0, 0));

  private final String name;
  private final String abbreviation;
  private final Point offset;

  Direction(String name, String abbreviation, Point offset) {
    this.name = name;
    this.abbreviation = abbreviation;
    this.offset = offset;
  }

  /**
   * Returns the Direction that a given abbreviation corresponds to.
   *
   * @param abbreviation an abbreviation such as "N" or "E"
   * @return a Direction value or null if there is no match
   */
  public static Direction fromAbbreviation(String abbreviation) {
    for (Direction direction : values()) {
      if (direction.abbreviation.equals(abbreviation)) {
        return direction;
      }
    }
    return null;
  }

  /**
   * Returns a modifiable Collection with all the Directions except the specified one.
   *
   * @param exception the exception (passing null retrieves all Directions)
   */
  public static Collection<Direction> getAllExcept(Direction exception) {
    List<Direction> directions = new ArrayList<Direction>(Arrays.asList(values()));
    directions.remove(exception);
    return directions;
  }

  public Point getOffset() {
    return offset;
  }

  /**
   * Returns the opposite direction.
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
