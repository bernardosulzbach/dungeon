package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;

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
    Point center = Game.getGameState().getHero().getLocation().getPoint();
    int cols = GameWindow.COLS;
    int rows = GameWindow.ROWS - 1;
    limits = new IterationLimits(center, cols, rows);
    matrix = new WorldMapSymbol[rows][cols]; // Add 1 to account for newlines.
    stringRepresentation = rows + "x" + cols + " map.";
  }

  /**
   * Makes a standard WorldMap.
   */
  @NotNull
  public static WorldMap makeWorldMap() {
    World world = Game.getGameState().getWorld();
    Point heroPosition = Game.getGameState().getHero().getLocation().getPoint();
    ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
    WorldMapSymbolFactory factory = new WorldMapSymbolFactory(world, heroPosition, explorationStatistics);
    return renderWorldMap(factory);
  }

  /**
   * Makes a debug WorldMap.
   */
  @NotNull
  public static WorldMap makeDebugWorldMap() {
    World world = Game.getGameState().getWorld();
    Point heroPosition = Game.getGameState().getHero().getLocation().getPoint();
    WorldMapSymbolFactory factory = new WorldMapSymbolFactory(world, heroPosition);
    return renderWorldMap(factory);
  }

  private static WorldMap renderWorldMap(WorldMapSymbolFactory symbolFactory) {
    WorldMap map = new WorldMap();
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

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
