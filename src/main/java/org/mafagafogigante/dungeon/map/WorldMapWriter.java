package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

  private static void renderLegend(@NotNull WorldMapSymbol[][] worldMapSymbolMatrix, @NotNull DungeonString string) {
    List<WorldMapSymbol> symbols = new ArrayList<>();
    int centerI = (worldMapSymbolMatrix.length - 2) / 2 + 1;
    int centerJ = worldMapSymbolMatrix[centerI].length / 2 - 1;
    symbols.add(worldMapSymbolMatrix[centerI - 1][centerJ]);
    symbols.add(worldMapSymbolMatrix[centerI][centerJ - 1]);
    symbols.add(worldMapSymbolMatrix[centerI][centerJ + 1]);
    symbols.add(worldMapSymbolMatrix[centerI + 1][centerJ]);
    for (int i = 0; i < symbols.size(); i++) {
      if (i % 2 == 0) {
        string.append(String.format("%-16s", ""));
      }
      WorldMapSymbol symbol = symbols.get(i);
      string.setColor(symbol.getColor());
      string.append(symbol.getCharacterAsString());
      string.resetColor();
      string.append(String.format(" - %-32s", symbol.getName()));
      if (i % 2 != 0) {
        string.append("\n");
      }
    }
  }

  /**
   * Writes a WorldMap to the screen. This erases all the content currently on the screen.
   *
   * @param map a WorldMap, not null
   */
  private static void renderMap(@NotNull WorldMap map) {
    DungeonString string = new DungeonString();
    WorldMapSymbol[][] worldMapSymbolMatrix = map.getSymbolMatrix();
    for (int i = 1; i < worldMapSymbolMatrix.length - 1; i++) {
      for (WorldMapSymbol symbol : worldMapSymbolMatrix[i]) {
        // OK as setColor verifies if the color change is necessary (does not replace a color by itself).
        string.setColor(symbol.getColor());
        string.append(symbol.getCharacterAsString());
      }
      if (i < worldMapSymbolMatrix.length - 1) {
        string.append("\n");
      }
    }
    renderLegend(worldMapSymbolMatrix, string);
    Writer.write(string);
  }

}
