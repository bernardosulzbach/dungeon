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
 * TurnResult class that stores the result of a turn.
 */
class TurnResult {

  int turnLength;
  private boolean configurationsChanged;

  /**
   * Resets the fields. Should be called after the results were read to prepare the class for the next turn.
   */
  void clear() {
    turnLength = 0;
    configurationsChanged = false;
  }

  /**
   * Evaluates if the GameState has changed.
   * <p/>
   * Basically, returns {@code turnLength != 0 || configurationsChanged}.
   */
  boolean gameStateChanged() {
    return turnLength != 0 || configurationsChanged;
  }

}
