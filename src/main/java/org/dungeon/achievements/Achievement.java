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
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

/**
 * Achievement class.
 * <p/>
 * Created by Bernardo on 07/12/2014.
 */
public class Achievement {

  private final ID id;
  private final String name;
  private final String info;

  private final BattleComponent battle;
  private final ExplorationComponent exploration;

  public Achievement(String id, String name, String info) {
    this.id = new ID(id);
    this.name = name;
    this.info = info;
    battle = new BattleComponent();
    exploration = new ExplorationComponent();
  }

  public ID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getInfo() {
    return info;
  }

  public void setBattleCount(int battleCount) {
    battle.battleCount = battleCount;
  }

  public void setLongestBattleLength(int longestBattleLength) {
    battle.longestBattleLength = longestBattleLength;
  }

  /**
   * Increment how many kills with a certain weapon are needed in order to unlock this achievement.
   *
   * @param id     the id of the weapon.
   * @param amount the increment.
   */
  public void incrementKillsByWeapon(ID id, int amount) {
    battle.killsByWeapon.incrementCounter(id, amount);
  }

  /**
   * Increment how many kills of certain creature are needed in order to unlock this achievement.
   *
   * @param id     the creature's id.
   * @param amount the increment.
   */
  public void incrementKillsByCreatureId(ID id, int amount) {
    battle.killsByCreatureId.incrementCounter(id, amount);
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

  public void setKillCount(int count) {
    exploration.killCount = count;
  }

  public void setVisitCount(int count) {
    exploration.visitCount = count;
  }

  boolean isFulfilled(Hero hero) {
    return battle.isFulfilled(hero) && exploration.isFulfilled(hero);
  }

  /**
   * Updates the state of the Achievement.
   *
   * @return true if the achievement was unlocked. False otherwise.
   */
  public final void update(Hero hero) {
    if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled(hero)) {
      // All the requirements OK, unlock the achievement.
      printAchievementUnlocked();
      hero.getAchievementTracker().unlock(this);
      return;
    } else {
      return;
    }

  }

  /**
   * Outputs an achievement unlocked message with some information about the unlocked achievement.
   */
  void printAchievementUnlocked() {
    // Initial capacity:
    // First line:                    precisely 100 characters
    // Name, info and three newlines: about 60 characters (using 100 to prevent buffer overflow).
    // How many experience points:    25 characters for a experience reward of three digits.
    // Total:                         100 + 100 + 25 = 225
    StringBuilder sb = new StringBuilder(225);
    sb.append(Utils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-')).append("\n");
    sb.append(getName()).append("\n");
    sb.append(getInfo()).append("\n");
    // Keep the StringBuilder as, in the future, Honor, Karma or something else may be rewarded by the achievements.
    IO.writeString(sb.toString());
  }

}
