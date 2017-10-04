package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;

import org.jetbrains.annotations.NotNull;

/**
 * WorldMap class that represents an ASCII map from the surroundings of the player.
 */
class WorldMap {

  private final WorldMapSymbol[][] matrix;
  private final IterationLimits limits;

  /**
   * Initializes the WorldMap with a proper IterationLimits object and a matrix of null objects.
   */
  private WorldMap(int rows, int columns) {
    Point center = Game.getGameState().getHero().getLocation().getPoint();
    this.limits = new IterationLimits(center, rows, columns);
    this.matrix = new WorldMapSymbol[rows][columns];
  }

  /**
   * Makes a WorldMap of the specified size. If limited, only contains already seen locations.
   */
  @NotNull
  static WorldMap makeWorldMap(int rows, int columns, boolean limited) {
    World world = Game.getGameState().getWorld();
    Point heroPosition = Game.getGameState().getHero().getLocation().getPoint();
    if (limited) {
      ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
      return renderWorldMap(new WorldMapSymbolFactory(world, heroPosition, explorationStatistics), rows, columns);
    } else {
      return renderWorldMap(new WorldMapSymbolFactory(world, heroPosition), rows, columns);

    }
  }

  private static WorldMap renderWorldMap(WorldMapSymbolFactory symbolFactory, int rows, int columns) {
    WorldMap map = new WorldMap(rows, columns);
    for (int curY = map.limits.minY; curY >= map.limits.maxY; curY--) {
      for (int curX = map.limits.minX; curX <= map.limits.maxX; curX++) {
        Point currentPosition = new Point(curX, curY, 0);
        map.matrix[map.limits.minY - curY][curX - map.limits.minX] = symbolFactory.getSymbol(currentPosition);
      }
    }
    return map;
  }

  WorldMapSymbol[][] getSymbolMatrix() {
    return matrix;
  }

}
