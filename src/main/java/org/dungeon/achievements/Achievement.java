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

package org.dungeon.achievements;

import org.dungeon.creatures.Hero;
import org.dungeon.game.ID;
import org.dungeon.io.IO;
import org.dungeon.util.Constants;
import org.dungeon.util.Utils;

/**
 * Achievement class.
 * <p/>
 * Created by Bernardo on 07/12/2014.
 */
public class Achievement {

  private final ID id;
  private final String name;
  private final String info;

  private final BattleComponent battle = new BattleComponent();
  private final ExplorationComponent exploration = new ExplorationComponent();

  /**
   * Constructs an Achievement with the specified ID, name and info.
   *
   * @param id   the achievement's ID.
   * @param name the achievement's name.
   * @param info the achievement's info.
   */
  public Achievement(String id, String name, String info) {
    this.id = new ID(id);
    this.name = name;
    this.info = info;
  }

  public ID getID() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getInfo() {
    return info;
  }

  /**
   * Sets the minimum battle count to fulfill this Achievement.
   *
   * @param minimumBattleCount the minimum battle count to fulfill this achievement.
   */
  public void setMinimumBattleCount(int minimumBattleCount) {
    battle.battleCount = minimumBattleCount;
  }

  /**
   * Sets the longest battle length to fulfill this Achievement.
   *
   * @param longestBattleLength the longest battle length to fulfill this Achievement.
   */
  public void setLongestBattleLength(int longestBattleLength) {
    battle.longestBattleLength = longestBattleLength;
  }

  /**
   * Increment how many kills with a certain weapon are needed in order to unlock this achievement.
   *
   * @param id     the id of the weapon.
   * @param amount the increment.
   */
  public void incrementKillsByWeapon(String id, int amount) {
    battle.killsByWeapon.incrementCounter(new ID(id), amount);
  }

  /**
   * Increment how many kills of certain creature are needed in order to unlock this achievement.
   *
   * @param id     the creature's id.
   * @param amount the increment.
   */
  public void incrementKillsByCreatureID(String id, int amount) {
    battle.killsByCreatureID.incrementCounter(new ID(id), amount);
  }

  /**
   * Increment how many kills of certain type of creature are needed in order to unlock this achievement.
   *
   * @param id     the creature type.
   * @param amount the increment.
   */
  public void incrementKillsByCreatureType(String id, int amount) {
    battle.killsByCreatureType.incrementCounter(id, amount);
  }

  /**
   * Set the required kill count in a specified Location.
   *
   * @param locationID the CreatureID.
   * @param amount     the required kill count.
   */
  public void incrementKillsByLocationID(String locationID, int amount) {
    exploration.killCounter.incrementCounter(new ID(locationID), amount);
  }

  /**
   * Increment the required visit count to distinct Locations with a specified Location ID.
   *
   * @param locationID the CreatureID.
   * @param amount     the required kill count.
   */
  public void incrementVisitsToDistinctLocations(String locationID, int amount) {
    exploration.distinctLocationsVisitCount.incrementCounter(new ID(locationID), amount);
  }

  /**
   * Increment the required visit count to the same Location with a specified Location ID.
   *
   * @param locationID the CreatureID.
   * @param amount     the required kill count.
   */
  public void incrementVisitsToTheSameLocation(String locationID, int amount) {
    exploration.sameLocationVisitCounter.incrementCounter(new ID(locationID), amount);
  }

  /**
   * Evaluates if a specified Hero fulfills this Achievement conditions.
   *
   * @param hero the Hero.
   * @return a boolean.
   */
  boolean isFulfilled(Hero hero) {
    return battle.isFulfilled(hero) && exploration.isFulfilled(hero);
  }

  /**
   * Updates the state of the Achievement.
   */
  public final void update(Hero hero) {
    if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled(hero)) {
      // All the requirements OK, unlock the achievement.
      printAchievementUnlocked();
      hero.getAchievementTracker().unlock(this);
    }
  }

  /**
   * Outputs an achievement unlocked message with some information about the unlocked achievement.
   */
  void printAchievementUnlocked() {
    IO.writeString(Utils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-') + "\n" + getName() + "\n" + getInfo());
  }

}
