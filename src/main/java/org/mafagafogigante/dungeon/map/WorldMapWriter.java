package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.Direction;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public final class WorldMapWriter {

  private WorldMapWriter() {
    throw new AssertionError();
  }

  public static void writeMap() {
    renderMap(WorldMap.makeWorldMap(getMapRows(), getMapColumns(), true));
  }

  public static void writeDebugMap() {
    renderMap(WorldMap.makeWorldMap(getMapRows(), getMapColumns(), false));
  }

  private static int getMapRows() {
    return GameWindow.getRows() - 1;
  }

  private static int getMapColumns() {
    return GameWindow.getColumns();
  }

  /**
   * Writes a WorldMap to the screen. This erases all the content currently on the screen.
   *
   * @param map a WorldMap, not null
   */
  private static void renderMap(@NotNull WorldMap map) {
    DungeonString string = new DungeonString();
    WorldMapSymbol[][] worldMapSymbolMatrix = map.getSymbolMatrix();
    for (int i = 0; i < worldMapSymbolMatrix.length - 2; i++) {
      for (WorldMapSymbol symbol : worldMapSymbolMatrix[i]) {
        // OK as setColor verifies if the color change is necessary (does not replace a color by itself).
        string.setColor(symbol.getColor());
        string.append(symbol.getCharacterAsString());
      }
      if (i < worldMapSymbolMatrix.length - 1) {
        string.append("\n");
      }
    }

    //Rendering the map legend
    ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
    directions.remove(Direction.UP);
    directions.remove(Direction.DOWN);
    for (int i = 0; i < directions.size(); i++) {
      Point adjacentPoint = new Point(Game.getGameState().getHero().getLocation().getPoint(), directions.get(i));
      if (Game.getGameState().getWorld().hasLocationAt(adjacentPoint)) {
        Location adjacentLocation = Game.getGameState().getWorld().getLocation(adjacentPoint);
        string.append("\t\t" + adjacentLocation.getName().getSingular().substring(0, 1) + " - " +
                adjacentLocation.getName().getSingular());
      }
      if (i == 1) {
        string.append("\n");
      }
    }
    Writer.write(string);
  }

}
