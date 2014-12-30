/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

import org.dungeon.achievements.Achievement;
import org.dungeon.creatures.Creature;
import org.dungeon.creatures.Hero;
import org.dungeon.io.IO;
import org.dungeon.util.Constants;

import java.awt.Color;
import java.util.Random;

/**
 * Engine class that contains most static methods that need to be called to
 * alter a GameState object.
 *
 * @author Bernardo Sulzbach
 */
public class Engine {

  // The single Random object used by all the methods.
  public static final Random RANDOM = new Random();

  /**
   * Refreshes the game, should be called after every turn.
   */
  public static void refresh() {
    refreshAchievements();
    refreshSpawners();
  }

  /**
   * Iterates over all achievements in GameData, trying to unlock yet to be
   * unlocked achievements.
   */
  private static void refreshAchievements() {
    Hero hero = Game.getGameState().getHero();
    for (Achievement a : GameData.ACHIEVEMENTS.values()) {
      a.update(hero);
    }
  }

  /**
   * Refreshes all relevant Spawners in the world, currently, that is the
   * spawner of the location the Hero is at.
   */
  private static void refreshSpawners() {
    Game.getGameState().getHeroLocation().refreshSpawners();
  }

  /**
   * Parses an array of words to move the hero to another location.
   *
   * @param issuedCommand the command entered by the player.
   * @return how many seconds the player walk took.
   */
  public static int parseHeroWalk(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      for (Direction dir : Direction.values()) {
        if (dir.equalsIgnoreCase(issuedCommand.getFirstArgument())) {
          return heroWalk(dir);
        }
      }
      IO.writeString(Constants.INVALID_INPUT);
    } else {
      IO.writeString("To where?", Color.ORANGE);
    }
    // The user did not walk.
    return 0;
  }

  /**
   * Attempts to move the hero character in a given direction.
   *
   * @return the number of seconds the player walk took as an integer.
   */
  private static int heroWalk(Direction dir) {
    GameState gameState = Game.getGameState();
    World world = gameState.getWorld();
    Point point = gameState.getHeroPosition();
    Hero hero = gameState.getHero();
    Point destinationPoint = new Point(gameState.getHeroPosition(), dir);
    if (world.getLocation(destinationPoint).isBlocked(dir.invert()) || world.getLocation(point).isBlocked(dir)) {
      IO.writeString("You cannot go " + dir + ".");
      return TimeConstants.WALK_BLOCKED;
    }
    Location destination = gameState.getWorld().moveHero(dir);
    refreshSpawners(); // Update the spawners of the location the Hero moved to.
    hero.setLocation(destination);
    hero.look(true);
    hero.getExplorationLog().addVisit(destinationPoint);
    return TimeConstants.WALK_SUCCESS;
  }

  /**
   * Simulates a battle between two Creatures and returns the number of turns the battle had.
   *
   * @param attacker the attacker.
   * @param defender the defender.
   * @return an integer representing the number of turns the battle had.
   */
  public static int battle(Creature attacker, Creature defender) {
    if (attacker == defender) {
      // Two different messages.
      if (RANDOM.nextBoolean()) {
        IO.writeString(Constants.SUICIDE_ATTEMPT_1);
      } else {
        IO.writeString(Constants.SUICIDE_ATTEMPT_2);
      }
      return 0;
    }
    /**
     * A counter variable that register how many turns the battle had.
     */
    int turns = 0;
    while (attacker.isAlive() && defender.isAlive()) {
      attacker.hit(defender);
      turns++;
      if (defender.isAlive()) {
        defender.hit(attacker);
        turns++;
      }
    }
    Creature survivor;
    Creature defeated;
    if (attacker.isAlive()) {
      survivor = attacker;
      defeated = defender;
    } else {
      survivor = defender;
      defeated = attacker;
    }
    IO.writeString(String.format("%s managed to kill %s.", survivor.getName(), defeated.getName()), Color.CYAN);
    if (attacker instanceof Hero) {
      Hero hero = (Hero) attacker;
      hero.getBattleStatistics().addBattle(attacker, defender, attacker == survivor, turns);
      hero.getExplorationLog().addKill(Game.getGameState().getHeroPosition());
    }
    battleCleanup(survivor, defeated);
    return turns;
  }

  private static void battleCleanup(Creature survivor, Creature defeated) {
    // Remove the dead creature from the location.
    survivor.getLocation().removeCreature(defeated);
  }

}
