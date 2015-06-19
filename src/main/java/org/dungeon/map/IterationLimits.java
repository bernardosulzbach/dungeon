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

package org.dungeon.map;

import org.dungeon.game.Point;

import org.jetbrains.annotations.NotNull;

class IterationLimits {

  final int minX;
  final int maxX;
  final int minY;
  final int maxY;

  /**
   * Constructs a new IterationLimits based on the center of the map, the number of columns and the number of rows.
   *
   * @param center the Point at the center of the map, not null
   * @param cols   the number of columns
   * @param rows   the number of rows
   */
  IterationLimits(@NotNull Point center, int cols, int rows) {
    minX = center.getX() - (cols - 1) / 2;
    maxX = minX + cols - 1;
    minY = center.getY() + (rows - 1) / 2;
    maxY = minY - rows + 1;
  }

}
