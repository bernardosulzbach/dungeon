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

package org.dungeon.game;

import org.dungeon.gui.GameWindow;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;

/**
 * WorldMap class that represents an ASCII map from the surroundings of the player.
 */
class WorldMap {

  private static final char HERO_SYMBOL = '@';
  private static final char NOT_YET_SEEN_SYMBOL = '?';
  private static final char NOT_YET_GENERATED_SYMBOL = '~';
  // TODO colors (to avoid problems such as Swamp and Savannah both starting with 'S' and to make the map prettier).
  private final String map;

  /**
   * Constructs a map based on the position of the Hero in the World.
   */
  public WorldMap(World world, ExplorationStatistics explorationStatistics, Point heroPosition) {
    int rows = GameWindow.ROWS - 1;
    int cols = Constants.COLS;
    int initX = heroPosition.getX() - (cols - 1) / 2;
    int lastX = initX + cols - 1;
    int initY = heroPosition.getY() + (rows - 1) / 2;
    int lastY = initY - rows + 1;
    // Add 1 to account for newlines.
    StringBuilder builder = new StringBuilder((cols + 1) * rows);
    for (int curY = initY; curY >= lastY; curY--) {
      for (int curX = initX; curX <= lastX; curX++) {
        Point currentPosition = new Point(curX, curY);
        if (currentPosition.equals(heroPosition)) {
          builder.append(HERO_SYMBOL);
        } else if (world.hasLocation(currentPosition)) {
          if (explorationStatistics.hasBeenSeen(currentPosition)) {
            builder.append(world.getLocation(currentPosition).getName().getSingular().charAt(0));
          } else {
            builder.append(NOT_YET_SEEN_SYMBOL);
          }
        } else {
          builder.append(NOT_YET_GENERATED_SYMBOL);
        }
      }
      builder.append('\n');
    }
    map = builder.toString();
  }

  @Override
  public String toString() {
    return map;
  }

}
