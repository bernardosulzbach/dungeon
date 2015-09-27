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

import org.dungeon.game.Game;
import org.dungeon.game.Point;
import org.dungeon.game.World;
import org.dungeon.gui.GameWindow;
import org.dungeon.stats.ExplorationStatistics;

import org.jetbrains.annotations.NotNull;

/**
 * WorldMap class that represents an ASCII map from the surroundings of the player.
 */
public class WorldMap {

  private final WorldMapSymbol[][] matrix;
  private final IterationLimits limits;
  private final String stringRepresentation;

  /**
   * Initializes the WorldMap with a proper IterationLimits object and a matrix of null objects.
   */
  private WorldMap() {
    Point center = Game.getGameState().getHeroPosition();
    int cols = GameWindow.COLS;
    int rows = GameWindow.ROWS - 1;
    limits = new IterationLimits(center, cols, rows);
    matrix = new WorldMapSymbol[rows][cols]; // Add 1 to account for newlines.
    stringRepresentation = rows + "x" + cols + " map.";
  }

  @NotNull
  public static WorldMap makeWorldMap() {
    World world = Game.getGameState().getWorld();
    Point heroPosition = Game.getGameState().getHeroPosition();
    ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
    WorldMapSymbolFactory factory = new WorldMapSymbolFactory(world, heroPosition, explorationStatistics);
    return renderWorldMap(factory);
  }

  @NotNull
  public static WorldMap makeDebugWorldMap() {
    World world = Game.getGameState().getWorld();
    Point heroPosition = Game.getGameState().getHeroPosition();
    WorldMapSymbolFactory factory = new WorldMapSymbolFactory(world, heroPosition);
    return renderWorldMap(factory);
  }

  private static WorldMap renderWorldMap(WorldMapSymbolFactory symbolFactory) {
    WorldMap map = new WorldMap();
    for (int curY = map.limits.minY; curY >= map.limits.maxY; curY--) {
      for (int curX = map.limits.minX; curX <= map.limits.maxX; curX++) {
        Point currentPosition = new Point(curX, curY);
        map.matrix[map.limits.minY - curY][curX - map.limits.minX] = symbolFactory.getSymbol(currentPosition);
      }
    }
    return map;
  }

  WorldMapSymbol[][] getSymbolMatrix() {
    return matrix;
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
