/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that is responsible for distributing dungeons.
 */
class DungeonDistributor implements Serializable {

  private static final Percentage dungeonProbability = Percentage.fromString("2%");
  private static final MinimumBoundingRectangle biggestDungeonPossible = DungeonCreator.getMinimumBoundingRectangle();

  private final Set<Point> entrances = new HashSet<>();

  public DungeonDistributor() {
  }

  /**
   * Makes a list with all the Points that should not contain a dungeon entrance.
   */
  static List<Point> makeNoEntrancesZonePointList(Point point, MinimumBoundingRectangle minimumBoundingRectangle) {
    /*

    Suppose that the minimum bounding rectangle of the biggest dungeon is 2 by 2.
    That way, the dungeon could be any of the following:

      E#  #E  ##  ##
      ##  ##  E#  #E

    We must also ensure that between rooms of two different dungeons there is at least one space.
    If we place this rectangle in the four diagonals we get the zone that cannot have any dungeon entrance.

      ## ##
      ## ##
        E
      ## ##
      ## ##

     */
    final int width = minimumBoundingRectangle.getWidth();
    final int height = minimumBoundingRectangle.getHeight();
    List<Point> points = new ArrayList<>();
    for (int x = point.getX() - width; x <= point.getX() + width; x++) {
      for (int y = point.getY() - height; y <= point.getY() + height; y++) {
        if (x != point.getX() || y != point.getY()) {
          points.add(new Point(x, y, point.getZ()));
        }
      }
    }
    return points;
  }

  /**
   * Randomly decides whether or not a point should have a dungeon entrance if it is isolated enough.
   */
  public boolean rollForDungeon(Point point) {
    return isIsolatedEnough(point) && Random.roll(dungeonProbability);
  }

  public void registerDungeonEntrance(Point point) {
    if (entrances.contains(point)) {
      throw new IllegalStateException("point " + point.toString() + " is already registered");
    } else {
      entrances.add(point);
    }
  }

  /**
   * Tests if there is no dungeon entrances in the 5x5 square centered at the provided point. Therefore, currently it is
   * only safe to make dungeons that are at most 3x3 and centered at the entrance.
   */
  private boolean isIsolatedEnough(Point point) {
    for (Point pointToCheck : makeNoEntrancesZonePointList(point, biggestDungeonPossible)) {
      if (entrances.contains(pointToCheck)) {
        return false;
      }
    }
    return true;
  }

}
