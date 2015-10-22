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

/**
 * River class that implements a river (a line of water in the World).
 */
class River implements Serializable {

  private static final int MIN_BRIDGE_DIST = 4;
  private static final int MAX_BRIDGE_DIST = 20;

  private final ExpandableIntegerSet bridges;

  /**
   * Make a river.
   */
  River() {
    bridges = new ExpandableIntegerSet(MIN_BRIDGE_DIST, MAX_BRIDGE_DIST);
  }

  /**
   * Expand the set of bridges towards a value of y until there is a bridge at y or after y.
   *
   * @param y the y coordinate
   */
  private void expand(int y) {
    bridges.expand(y);
  }

  /**
   * Evaluates if a given value of y corresponds to a bridge.
   *
   * @param y the y coordinate
   * @return true if there should be a bridge in this y coordinate
   */
  boolean isBridge(int y) {
    expand(y);
    return bridges.contains(y);
  }

  @Override
  public String toString() {
    return "River with bridges in " + bridges;
  }

}
