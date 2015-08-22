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

import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.Hero;
import org.dungeon.io.Writer;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Utils;

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
   * Refreshes the game. This method should be called whenever the state of the game is changed and the engine should be
   * updated. If time passed, use {@link org.dungeon.game.Engine#rollDateAndRefresh(int)}.
   */
  public static void refresh() {
    effectivelyUpdate(0);
  }

  /**
   * Rolls the world date forward and refreshes the game. This method should be called whenever the state of the game is
   * changed and the engine should be updated. If no time passed, use {@link org.dungeon.game.Engine#refresh()}.
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
    Game.getGameState().getHero().getAchievementTracker().update();
  }

  /**
   * Parses an issued command to move the player.
   */
  public static void parseHeroWalk(String[] arguments) {
    if (arguments.length != 0) {
      for (Direction dir : Direction.values()) {
        if (dir.equalsIgnoreCase(arguments[0])) {
          heroWalk(dir);
          return;
        }
      }
      Writer.writeString("Invalid input.");
    } else {
      Writer.writeString("To where?", Color.ORANGE);
    }
  }

  /**
   * Attempts to move the hero in a given direction.
   *
   * <p>If the hero moves, this method refreshes both locations at the latest date. If the hero does not move, this
   * method refreshes the location where the hero is.
   */
  private static void heroWalk(Direction dir) {
    GameState gameState = Game.getGameState();
    World world = gameState.getWorld();
    Point point = gameState.getHeroPosition();
    Hero hero = gameState.getHero();
    Point destinationPoint = new Point(gameState.getHeroPosition(), dir);
    if (world.getLocation(destinationPoint).isBlocked(dir.invert()) || world.getLocation(point).isBlocked(dir)) {
      rollDateAndRefresh(WALK_BLOCKED); // The hero tries to go somewhere.
      Writer.writeString("You cannot go " + dir + ".");
    } else {
      Location destination = gameState.getWorld().moveHero(dir);
      rollDateAndRefresh(WALK_SUCCESS); // Time spent walking.
      hero.setLocation(destination);
      refresh(); // Hero arrived in a new location, refresh the game.
      hero.look(dir.invert());
      ExplorationStatistics explorationStatistics = gameState.getStatistics().getExplorationStatistics();
      explorationStatistics.addVisit(destinationPoint, world.getLocation(destinationPoint).getId());
    }
  }

  /**
   * Simulates a battle between the hero and a creature.
   *
   * @param hero the attacker
   * @param foe the defender
   */
  public static void battle(Hero hero, Creature foe) {
    if (hero == foe) {
      Writer.writeString("You cannot attempt suicide.");
      return;
    }
    while (hero.getHealth().isAlive() && foe.getHealth().isAlive()) {
      hero.hit(foe);
      Engine.rollDateAndRefresh(BATTLE_TURN_DURATION);
      // No contract specifies that calling hit on the Hero will not kill it, so check both creatures again.
      // Additionally, rolling the date forward may kill the hero in the future.
      if (hero.getHealth().isAlive() && foe.getHealth().isAlive()) {
        foe.hit(hero);
        Engine.rollDateAndRefresh(BATTLE_TURN_DURATION);
      }
    }
    Creature survivor = hero.getHealth().isAlive() ? hero : foe;
    Creature defeated = (survivor == hero) ? foe : hero;
    // Imagine if a third factor (such as hunger) could kill one of the creatures.
    // I think it still makes sense to say that the survivor managed to kill the defeated, but that's just me.
    Writer.writeString(survivor.getName() + " managed to kill " + defeated.getName() + ".", Color.CYAN);
    writeDrops(defeated);
    if (hero == survivor) {
      PartOfDay partOfDay = PartOfDay.getCorrespondingConstant(Game.getGameState().getWorld().getWorldDate());
      Game.getGameState().getStatistics().getBattleStatistics().addBattle(foe, defeated.getCauseOfDeath(), partOfDay);
      Game.getGameState().getStatistics().getExplorationStatistics().addKill(Game.getGameState().getHeroPosition());
    }
  }

  /**
   * Writes a message with what the creature dropped if it dropped something.
   *
   * @param source a dead Creature
   */
  private static void writeDrops(Creature source) {
    if (!source.getDroppedItemsList().isEmpty()) {
      DungeonStringBuilder builder = new DungeonStringBuilder();
      builder.append(source.getName().getSingular() + " dropped ");
      builder.append(Utils.enumerateEntities(source.getDroppedItemsList()));
      builder.append(".");
      Writer.write(builder);
    }
  }

}
