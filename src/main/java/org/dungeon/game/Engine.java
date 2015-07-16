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
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.Hero;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.io.IO;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;

import java.awt.Color;

/**
 * Engine class that contains most static methods that need to be called to alter the loaded GameState.
 */
public final class Engine {

  private static final int BATTLE_TURN_DURATION = 30;
  private static final int WALK_BLOCKED = 2;
  private static final int WALK_SUCCESS = 200;

  private Engine() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Refreshes the game.
   * This method should be called whenever the state of the game is changed and the engine should be updated.
   * If time passed, use {@link org.dungeon.game.Engine#rollDateAndRefresh(int)}.
   */
  public static void refresh() {
    effectivelyUpdate(0);
  }

  /**
   * Rolls the world date forward and refreshes the game.
   * This method should be called whenever the state of the game is changed and the engine should be updated.
   * If no time passed, use {@link org.dungeon.game.Engine#refresh()}.
   *
   * @param seconds how many seconds to roll the date forward, a positive integer
   */
  public static void rollDateAndRefresh(int seconds) {
    if (seconds <= 0) {
      throw new IllegalArgumentException("seconds should be positive.");
    }
    effectivelyUpdate(seconds);
  }

  /**
   * Effectively updates the game. Rolls time forward before silently refreshing the game.
   *
   * @param seconds how many seconds to roll the date forward, nonnegative
   */
  private static void effectivelyUpdate(int seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException("seconds should be nonnegative.");
    }
    if (seconds > 0) {
      Game.getGameState().getWorld().rollDate(seconds);
    }
    silentRefresh();
    notifyGameStateModification();
  }

  /**
   * Sets the status of the GameState to unsaved.
   */
  private static void notifyGameStateModification() {
    Game.getGameState().setSaved(false);
  }

  /**
   * Silently refreshes the game. Does not produce any visible textual output.
   */
  private static void silentRefresh() {
    refreshSpawners();
    refreshItems();
  }

  /**
   * Ends the turn, refreshing the game state and checking if any achievements were unlocked.
   */
  public static void endTurn() {
    silentRefresh();
    refreshAchievements();
  }

  /**
   * Refreshes all relevant Spawners in the world, currently, that is the spawners of the location the Hero is at.
   */
  private static void refreshSpawners() {
    Game.getGameState().getHeroLocation().refreshSpawners();
  }

  /**
   * Refreshes all the items in the location the Hero is at.
   */
  private static void refreshItems() {
    Game.getGameState().getHeroLocation().refreshItems();
  }

  /**
   * Iterates over all achievements in GameData, trying to unlock yet to be unlocked achievements.
   */
  private static void refreshAchievements() {
    Game.getGameState().getHero().getAchievementTracker().update(GameData.ACHIEVEMENTS.values());
  }

  /**
   * Parses an issued command to move the player.
   *
   * @param issuedCommand the command entered by the player.
   */
  public static void parseHeroWalk(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      for (Direction dir : Direction.values()) {
        if (dir.equalsIgnoreCase(issuedCommand.getFirstArgument())) {
          heroWalk(dir);
          return;
        }
      }
      IO.writeString(Constants.INVALID_INPUT);
    } else {
      IO.writeString("To where?", Color.ORANGE);
    }
  }

  /**
   * Attempts to move the hero in a given direction.
   * <p/>
   * If the hero moves, this method refreshes both locations at the latest date.
   * If the hero does not move, this method refreshes the location where the hero is.
   */
  private static void heroWalk(Direction dir) {
    GameState gameState = Game.getGameState();
    World world = gameState.getWorld();
    Point point = gameState.getHeroPosition();
    Hero hero = gameState.getHero();
    Point destinationPoint = new Point(gameState.getHeroPosition(), dir);
    if (world.getLocation(destinationPoint).isBlocked(dir.invert()) || world.getLocation(point).isBlocked(dir)) {
      rollDateAndRefresh(WALK_BLOCKED); // The hero tries to go somewhere.
      IO.writeString("You cannot go " + dir + ".");
    } else {
      Location destination = gameState.getWorld().moveHero(dir);
      rollDateAndRefresh(WALK_SUCCESS); // Time spent walking.
      hero.setLocation(destination);
      refresh(); // Hero arrived in a new location, refresh the game.
      hero.look(dir.invert());
      ExplorationStatistics explorationStatistics = gameState.getStatistics().getExplorationStatistics();
      explorationStatistics.addVisit(destinationPoint, world.getLocation(destinationPoint).getID());
    }
  }

  /**
   * Simulates a battle between the hero and a creature.
   *
   * @param hero the attacker
   * @param foe  the defender
   */
  // Whenever wanting to allow foes to start battle, allow for a boolean parameter that indicates if the foe starts.
  public static void battle(Hero hero, Creature foe) {
    if (hero == foe) {
      IO.writeString("You cannot attempt suicide.");
    }
    CauseOfDeath causeOfDeath = null;
    // A counter variable that register how many turns the battle had.
    while (hero.isAlive() && foe.isAlive()) {
      causeOfDeath = hero.hit(foe);
      hero.getSkillRotation().refresh();
      foe.getSkillRotation().refresh();
      Engine.rollDateAndRefresh(BATTLE_TURN_DURATION);
      if (foe.isAlive()) {
        foe.hit(hero);
        hero.getSkillRotation().refresh();
        foe.getSkillRotation().refresh();
        Engine.rollDateAndRefresh(BATTLE_TURN_DURATION);
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
    if (hero == survivor) {
      if (causeOfDeath == null) { // Should never happen. The hit must be a fatal blow for the target to die.
        throw new AssertionError();
      }
      PartOfDay partOfDay = PartOfDay.getCorrespondingConstant(Game.getGameState().getWorld().getWorldDate());
      Game.getGameState().getStatistics().getBattleStatistics().addBattle(foe, causeOfDeath, partOfDay);
      Game.getGameState().getStatistics().getExplorationStatistics().addKill(Game.getGameState().getHeroPosition());
      battleCleanup(survivor, defeated);
    }
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
