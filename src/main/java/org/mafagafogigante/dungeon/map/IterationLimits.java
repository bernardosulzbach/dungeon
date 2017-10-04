package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.Point;

import org.jetbrains.annotations.NotNull;

class IterationLimits {

  final int minX;
  final int maxX;
  final int minY;
  final int maxY;

  /**
   * Constructs new iteration limits based on the center of the map, the number of columns and the number of rows.
   *
   * @param center the Point at the center of the map, not null
   * @param rows the number of rows
   */
  IterationLimits(@NotNull Point center, int rows, int columns) {
    minX = center.getX() - (columns - 1) / 2;
    maxX = minX + columns - 1;
    minY = center.getY() + (rows - 1) / 2;
    maxY = minY - rows + 1;
  }

}
