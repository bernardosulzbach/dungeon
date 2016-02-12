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
   * @param cols the number of columns
   * @param rows the number of rows
   */
  IterationLimits(@NotNull Point center, int cols, int rows) {
    minX = center.getX() - (cols - 1) / 2;
    maxX = minX + cols - 1;
    minY = center.getY() + (rows - 1) / 2;
    maxY = minY - rows + 1;
  }

}
