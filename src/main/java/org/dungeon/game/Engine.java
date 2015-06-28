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

import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.Date;
import org.dungeon.date.DungeonTimeUnit;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.Hero;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.io.IO;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;

import java.awt.Color;

/**
 * Engine class that contains most static methods that need to be called to alter a GameState object.
 */
public class Engine {

  private static final int BATTLE_TURN_DURATION = 30;
  private static final int WALK_BLOCKED = 2;
  private static final int WALK_SUCCESS = 200;

  /**
   * Refreshes the game, should be called after every turn.
   */
  public static void refresh() {
    refreshAchievements();
    refreshSpawners();
    refreshItems();
  }

  /**
   * Iterates over all achievements in GameData, trying to unlock yet to be unlocked achievements.
   */
  private static void refreshAchievements() {
    Hero hero = Game.getGameState().getHero();
    hero.getAchievementTracker().update(GameData.ACHIEVEMENTS.values());
  }

  /**
   * Refreshes all relevant Spawners in the world, currently, that is the spawner of the location the Hero is at.
   */
  private static void refreshSpawners() {
    Game.getGameState().getHeroLocation().refreshSpawners();
  }

  private static void refreshItems() {
    Game.getGameState().getHeroLocation().getInventory().refreshItems();
    Game.getGameState().getHero().getInventory().refreshItems();
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
      return WALK_BLOCKED;
    }
    Location destination = gameState.getWorld().moveHero(dir);
    refreshSpawners(); // Update the spawners of the location the Hero moved to.
    hero.setLocation(destination);
    hero.look(dir.invert());
    ExplorationStatistics explorationStatistics = gameState.getStatistics().getExplorationStatistics();
    explorationStatistics.addVisit(destinationPoint, world.getLocation(destinationPoint).getID());
    return WALK_SUCCESS;
  }

  /**
   * Simulates a battle between two Creatures and returns the number of turns the battle had.
   *
   * @param hero the attacker.
   * @param foe  the defender.
   * @return how many seconds the battle lasted.
   */
  // Whenever wanting to allow foes to start battle, allow for a boolean parameter that indicates if the foe starts.
  public static int battle(Hero hero, Creature foe) {
    if (hero == foe) {
      IO.writeString("You cannot attempt suicide.");
      return 0;
    }
    CauseOfDeath causeOfDeath = null;
    // A counter variable that register how many turns the battle had.
    int turns = 0;
    while (hero.isAlive() && foe.isAlive()) {
      causeOfDeath = hero.hit(foe);
      hero.getSkillRotation().refresh();
      foe.getSkillRotation().refresh();
      turns++;
      if (foe.isAlive()) {
        foe.hit(hero);
        hero.getSkillRotation().refresh();
        foe.getSkillRotation().refresh();
        turns++;
      }
    }
    Creature survivor;
    Creature defeated;
    if (hero.isAlive()) {
      survivor = hero;
      defeated = foe;
    } else {
      survivor = foe;
      defeated = hero;
    }
    IO.writeString(survivor.getName() + " managed to kill " + defeated.getName() + ".", Color.CYAN);
    int duration = turns * BATTLE_TURN_DURATION;
    if (hero == survivor) {
      if (causeOfDeath == null) { // Should never happen. The hit must be a fatal blow for the target to die.
        throw new AssertionError();
      }
      Date date = Game.getGameState().getWorld().getWorldDate().plus(duration, DungeonTimeUnit.SECOND);
      PartOfDay partOfDay = PartOfDay.getCorrespondingConstant(date);
      Game.getGameState().getStatistics().getBattleStatistics().addBattle(foe, causeOfDeath, partOfDay);
      Game.getGameState().getStatistics().getExplorationStatistics().addKill(Game.getGameState().getHeroPosition());
      battleCleanup(survivor, defeated);
    }
    return duration;
  }

  /**
   * Battle cleanup routine. This method removes the defeated Creature, drops all its Items onto the floor, makes and
   * adds its corpse Item to the Location and resets the SkillRotation of the survivor.
   *
   * @param survivor the Creature that is still alive.
   * @param defeated the Creature that was killed.
   */
  private static void battleCleanup(Creature survivor, Creature defeated) {
    Location defeatedLocation = defeated.getLocation();
    defeatedLocation.removeCreature(defeated);
    if (defeated.hasTag(Creature.Tag.CORPSE)) {
      defeatedLocation.addItem(ItemFactory.makeCorpse(defeated, defeatedLocation.getWorld().getWorldDate()));
    }
    defeated.dropEverything();
    survivor.getSkillRotation().restartRotation();
  }

}
