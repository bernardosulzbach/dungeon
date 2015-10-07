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

package org.dungeon.entity.creatures;

import org.dungeon.game.Direction;
import org.dungeon.game.DungeonString;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.GameState;
import org.dungeon.game.Point;
import org.dungeon.game.World;
import org.dungeon.io.Writer;
import org.dungeon.stats.ExplorationStatistics;

import java.awt.Color;
import java.io.Serializable;

/**
 * A Walker that is capable of moving between Locations. The Hero needs a Walker component in order to move around.
 */
class Walker implements Serializable {

  private static final int WALK_BLOCKED = 2;
  private static final int WALK_SUCCESS = 200;

  /**
   * Parses an issued command to move the player.
   */
  public void parseHeroWalk(String[] arguments) {
    if (arguments.length != 0) {
      for (Direction dir : Direction.values()) {
        if (dir.equalsIgnoreCase(arguments[0])) {
          heroWalk(dir);
          return;
        }
      }
      Writer.write("Invalid input.");
    } else {
      Writer.write(new DungeonString("To where?", Color.ORANGE));
    }
  }

  /**
   * Attempts to move the hero in a given direction.
   *
   * <p>If the hero moves, this method refreshes both locations at the latest date. If the hero does not move, this
   * method refreshes the location where the hero is.
   */
  private void heroWalk(Direction dir) {
    GameState gameState = Game.getGameState();
    World world = gameState.getWorld();
    Point point = gameState.getHeroPosition();
    Point destinationPoint = new Point(point, dir);
    // This order is important. Calling .getLocation may trigger location creation, so avoid creating a location that we
    // don't need if we can't get there.
    if (world.getLocation(point).isBlocked(dir) || world.getLocation(destinationPoint).isBlocked(dir.invert())) {
      Engine.rollDateAndRefresh(WALK_BLOCKED); // The hero tries to go somewhere.
      Writer.write("You cannot go " + dir + ".");
    } else {
      Hero hero = gameState.getHero();
      Engine.rollDateAndRefresh(WALK_SUCCESS); // Time spent walking.
      hero.getLocation().removeCreature(hero);
      world.getLocation(destinationPoint).addCreature(hero);
      hero.setLocation(world.getLocation(destinationPoint));
      gameState.setHeroPosition(destinationPoint);
      Engine.refresh(); // Hero arrived in a new location, refresh the game.
      hero.look(dir.invert());
      updateExplorationStatistics(destinationPoint);
    }
  }

  private void updateExplorationStatistics(Point destination) {
    ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
    explorationStatistics.addVisit(destination, Game.getGameState().getWorld().getLocation(destination).getId());
  }

}
