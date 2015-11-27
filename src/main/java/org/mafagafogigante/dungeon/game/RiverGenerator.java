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

package org.mafagafogigante.dungeon.game;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The component of WorldGenerator that generates rivers.
 */
final class RiverGenerator implements Serializable {

  private static final int MIN_DIST_RIVER = 6;
  private static final int MAX_DIST_RIVER = 11;
  private static final int START = 10; // Rivers do not appear in x > 10 || x < 10.
  private final ExpandableIntegerSet lines;
  private final HashMap<Integer, River> rivers;

  public RiverGenerator() {
    lines = new ExpandableIntegerSet(MIN_DIST_RIVER, MAX_DIST_RIVER);
    rivers = new HashMap<>();
  }

  /**
   * Expand the river set to ensure that all points whose x coordinate is in the range {@code [point.x - chunkSide,
   * point.x + chunkSide]} will either correspond to a river or to a location that anticipates a river.
   *
   * @param point the point from which the expansion starts
   * @param chunkSide the current chunk side
   */
  void expand(Point point, int chunkSide) {
    for (int river : lines.expand(point.getX() - chunkSide)) {
      if (river <= -START) {
        rivers.put(river, new River());
      }
    }
    for (int river : lines.expand(point.getX() + chunkSide)) {
      if (river >= START) {
        rivers.put(river, new River());
      }
    }
  }

  /**
   * Returns if in this point there should be a river.
   */
  boolean isRiver(Point point) {
    River river = rivers.get(point.getX());
    return river != null && !river.isBridge(point.getY());
  }

  /**
   * Returns if in this point there should be a bridge.
   */
  boolean isBridge(Point point) {
    River river = rivers.get(point.getX());
    return river != null && river.isBridge(point.getY());
  }

}
