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

import org.dungeon.achievements.BattleStatisticsRequirement;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.game.PartOfDay;
import org.dungeon.util.CounterMap;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * BattleStatistics class that stores battle statistics to enable achievements.
 */
public class BattleStatistics implements Serializable {

  private final CounterMap<BattleRecord> records = new CounterMap<BattleRecord>();

  /**
   * Adds the outcome of a battle to the statistics.
   *
   * @param foe the defeated Creature, not null
   * @param causeOfDeath the CauseOfDeath, not null
   * @param partOfDay the PartOfDay in which the last hit took place, not null
   */
  public void addBattle(@NotNull Creature foe, @NotNull CauseOfDeath causeOfDeath, @NotNull PartOfDay partOfDay) {
    BattleRecord record = new BattleRecord(foe.getId(), foe.getType(), causeOfDeath, partOfDay);
    records.incrementCounter(record);
  }

  /**
   * Returns a CounterMap of CauseOfDeath representing how many times each CauseOfDeath already registered occurred.
   */
  public CounterMap<CauseOfDeath> getKillsByCauseOfDeath() {
    CounterMap<CauseOfDeath> causeOfDeathCounterMap = new CounterMap<CauseOfDeath>();
    for (BattleRecord record : records.keySet()) {
      causeOfDeathCounterMap.incrementCounter(record.getCauseOfDeath(), records.getCounter(record));
    }
    return causeOfDeathCounterMap;
  }

  /**
   * Evaluates if this BattleStatistics satisfies a BattleStatisticsRequirement.
   */
  public boolean satisfies(BattleStatisticsRequirement requirement) {
    int count = 0;
    for (BattleRecord record : records.keySet()) {
      if (requirement.getQuery().matches(record)) {
        count += records.getCounter(record);
        if (count >= requirement.getCount()) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("BattleStatistics{records=%s}", records);
  }

}
