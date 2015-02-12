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
import org.dungeon.util.CounterMap;

/**
 * Achievement class.
 * <p/>
 * Created by Bernardo on 07/12/2014.
 */
public class Achievement {

  private final ID id;
  private final String name;
  private final String info;
  private final String text;

  private final BattleComponent battle;
  private final ExplorationComponent exploration;

  /**
   * Constructs an Achievement with the specified ID, name and info.
   *
   * @param info the String displayed when the "Achievements" command is used
   * @param text the String used to explain why the character unlocked the achievement
   */
  public Achievement(String id, String name, String info, String text, int minimumBattleCount, int longestBattleLength,
      CounterMap<ID> killsByCreatureID, CounterMap<String> killsByCreatureType, CounterMap<ID> killsByWeapon,
      CounterMap<ID> killsByLocationID, CounterMap<ID> discoveredLocations, CounterMap<ID> maximumNumberOfVisits) {
    this.id = new ID(id);
    this.name = name;
    this.info = info;
    this.text = text;
    battle = new BattleComponent(minimumBattleCount, longestBattleLength, killsByCreatureID, killsByCreatureType,
        killsByWeapon);
    exploration = new ExplorationComponent(killsByLocationID, discoveredLocations, maximumNumberOfVisits);
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
   * Evaluates if the statistics fulfill this Achievement's conditions.
   *
   * @return true if the Achievement is fulfilled, false otherwise.
   */
  boolean isFulfilled() {
    return battle.isFulfilled() && exploration.isFulfilled();
  }

  /**
   * Updates the state of the Achievement.
   */
  public final void update(Hero hero) {
    if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled()) {
      // All the requirements OK, unlock the achievement.
      printAchievementUnlocked();
      hero.getAchievementTracker().unlock(this);
    }
  }

  /**
   * Outputs an achievement unlocked message with some information about the unlocked achievement.
   */
  void printAchievementUnlocked() {
    IO.writeString("You unlocked the achievement " + getName() + " because you " + text + ".");
  }

}
