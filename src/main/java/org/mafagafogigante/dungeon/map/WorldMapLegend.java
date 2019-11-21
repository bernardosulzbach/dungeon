package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.DungeonString;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


final class WorldMapLegend {
  /**
   * Setting the amount of locations to display. MAX_LEGEND_LOCATIONS needs to be an even number for proper formatting.
   */
  private static final int MAX_LEGEND_LOCATIONS = 6;
  private static final int INITIAL_SPACE_VALUE = MAX_LEGEND_LOCATIONS / 2;
  private static final int NEW_LINE_VALUE = INITIAL_SPACE_VALUE - 1;

  private WorldMapLegend() {
    throw new AssertionError();
  }

  static void renderLegend(@NotNull WorldMapSymbol[][] worldMapSymbolMatrix, @NotNull DungeonString string) {
    Set<WorldMapSymbol> symbols = new HashSet<>();
    final int centerI = (worldMapSymbolMatrix.length - 2) / 2 + 1;
    final int centerJ = worldMapSymbolMatrix[centerI].length / 2 - 1;
    symbols.add(worldMapSymbolMatrix[centerI - 1][centerJ - 1]);
    symbols.add(worldMapSymbolMatrix[centerI - 1][centerJ]);
    symbols.add(worldMapSymbolMatrix[centerI - 1][centerJ + 1]);
    symbols.add(worldMapSymbolMatrix[centerI][centerJ - 1]);
    symbols.add(worldMapSymbolMatrix[centerI][centerJ + 1]);
    symbols.add(worldMapSymbolMatrix[centerI + 1][centerJ - 1]);
    symbols.add(worldMapSymbolMatrix[centerI + 1][centerJ]);
    symbols.add(worldMapSymbolMatrix[centerI + 1][centerJ + 1]);

    Iterator<WorldMapSymbol> it = symbols.iterator();
    for (int i = 0; i < MAX_LEGEND_LOCATIONS && it.hasNext(); i++) {
      WorldMapSymbol symbol = it.next();
      if (i % INITIAL_SPACE_VALUE == 0) {
        string.append(String.format("%-2s", ""));
      }
      string.setColor(symbol.getColor());
      string.append(String.format("%-32s", (symbol.getCharacterAsString() + " - " + symbol.getName())));
      string.resetColor();
      if (i == NEW_LINE_VALUE) {
        string.append("\n");
      }
    }
  }
}
