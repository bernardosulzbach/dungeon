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

package org.mafagafogigante.dungeon.achievements;

import org.jetbrains.annotations.NotNull;

/**
 * The statistical battle-related requirements for the unlock of an achievement.
 */
public class BattleStatisticsRequirement {

  private final BattleStatisticsQuery query;
  private final int count;

  /**
   * Constructs a new BattleStatisticsRequirement.
   *
   * @param query the query, not null
   * @param count the minimum amount of count, a positive integer
   */
  public BattleStatisticsRequirement(@NotNull BattleStatisticsQuery query, int count) {
    if (count < 1) {
      throw new IllegalArgumentException("count must be positive.");
    }
    this.query = query;
    this.count = count;
  }

  public BattleStatisticsQuery getQuery() {
    return query;
  }

  public int getCount() {
    return count;
  }

  @Override
  public String toString() {
    return String.format("BattleStatisticsRequirement{query=%s, count=%d}", query, count);
  }

}
