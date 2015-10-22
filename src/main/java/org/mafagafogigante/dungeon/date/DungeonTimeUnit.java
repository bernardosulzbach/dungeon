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

package org.mafagafogigante.dungeon.date;

/**
 * The time units of the Dungeon calendar.
 */
public enum DungeonTimeUnit {

  MILLISECOND(1), SECOND(1000 * MILLISECOND.milliseconds), MINUTE(60 * SECOND.milliseconds),
  HOUR(60 * MINUTE.milliseconds), DAY(24 * HOUR.milliseconds), MONTH(10 * DAY.milliseconds),
  YEAR(10 * MONTH.milliseconds);

  public final long milliseconds;

  DungeonTimeUnit(long milliseconds) {
    this.milliseconds = milliseconds;
  }

  /**
   * Returns how many of the specified unit are equivalent to one of this unit.
   *
   * @param unit a DungeonTimeUnit that is smaller than this one, not null
   * @return a long bigger than one
   */
  public long as(DungeonTimeUnit unit) {
    if (this.milliseconds <= unit.milliseconds) {
      throw new IllegalArgumentException("unit is bigger than or equal to the caller.");
    } else {
      return this.milliseconds / unit.milliseconds;
    }
  }

}
