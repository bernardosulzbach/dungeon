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
