package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;

import org.jetbrains.annotations.NotNull;

final class WorldMapSymbolFactory {

  private final World world;
  private final Point heroPosition;
  private final ExplorationStatistics explorationStatistics;

  /**
   * Constructs a new WorldMapSymbolFactory for the specified World and Hero position. All locations will have their
   * symbols revealed.
   */
  WorldMapSymbolFactory(World world, Point heroPosition) {
    this.world = world;
    this.heroPosition = heroPosition;
    this.explorationStatistics = null;
  }

  /**
   * Constructs a new WorldMapSymbolFactory for the specified World and Hero position. Just the locations that the
   * player has already seen will have their symbols revealed.
   */
  WorldMapSymbolFactory(World world, Point heroPosition, @NotNull ExplorationStatistics explorationStatistics) {
    this.world = world;
    this.heroPosition = heroPosition;
    this.explorationStatistics = explorationStatistics;
  }

  WorldMapSymbol getSymbol(Point position) {
    if (position.equals(heroPosition)) {
      return WorldMapSymbol.getHeroSymbol();
    } else if (explorationStatistics == null || explorationStatistics.hasBeenSeen(position)) {
      return WorldMapSymbol.makeSymbol(world.getLocation(position));
    } else {
      return WorldMapSymbol.getNotYetGeneratedSymbol();
    }
  }

}
