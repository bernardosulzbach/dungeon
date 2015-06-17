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

package org.dungeon.stats;

import org.dungeon.entity.creatures.Creature;
import org.dungeon.game.ID;
import org.dungeon.util.CounterMap;
import org.dungeon.util.Utils;

import java.io.Serializable;

/**
 * BattleStatistics class that stores battle statistics to enable achievements.
 */
public class BattleStatistics implements Serializable {

  private final CounterMap<String> killsByCreatureType = new CounterMap<String>();
  private final CounterMap<ID> killsByCreatureID = new CounterMap<ID>();
  private final CounterMap<CauseOfDeath> killsByCauseOfDeath = new CounterMap<CauseOfDeath>();
  private final Record longestBattleLength = new Record(Record.Type.MAXIMUM);
  private int battleCount;

  /**
   * Adds the outcome of a battle to the statistics.
   *
   * @param foe   the hero's foe
   * @param turns how many turns the battle took.
   */
  public void addBattle(Creature foe, CauseOfDeath causeOfDeath, int turns) {
    battleCount++;
    killsByCreatureType.incrementCounter(foe.getType());
    killsByCreatureID.incrementCounter(foe.getID());
    killsByCauseOfDeath.incrementCounter(causeOfDeath);
    longestBattleLength.update(turns);
  }

  public CounterMap<String> getKillsByCreatureType() {
    return killsByCreatureType;
  }

  public CounterMap<ID> getKillsByCreatureID() {
    return killsByCreatureID;
  }

  public CounterMap<CauseOfDeath> getKillsByCauseOfDeath() {
    return killsByCauseOfDeath;
  }

  public int getLongestBattleLength() {
    return Utils.zeroIfNull(longestBattleLength.getValue());
  }

  public int getBattleCount() {
    return battleCount;
  }

}
