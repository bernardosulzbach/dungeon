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

package org.mafagafogigante.dungeon.date;

import org.mafagafogigante.dungeon.util.DungeonMath;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Represents a nonnegative amount of time.
 */
public class Duration implements Serializable {

  /**
   * The duration, in milliseconds.
   */
  private final long duration;

  /**
   * Constructs a Duration from a specified start date to an end date.
   *
   * @param start the starting instance
   * @param end the ending instance
   */
  public Duration(@NotNull Date start, @NotNull Date end) {
    long difference = end.getTime() - start.getTime();
    if (difference >= 0) {
      duration = difference;
    } else {
      throw new IllegalArgumentException("end is before start.");
    }
  }

  /**
   * Constructs a Duration from the specified duration, in milliseconds.
   *
   * @param duration the duration, in milliseconds
   */
  Duration(long duration) {
    this.duration = duration;
  }

  public long getSeconds() {
    return duration / 1000;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    Duration duration = (Duration) object;

    return this.duration == duration.duration;
  }

  @Override
  public int hashCode() {
    return (int) (duration ^ (duration >>> 32));
  }

  @Override
  public String toString() {
    if (duration < DungeonTimeUnit.DAY.milliseconds) {
      return "Less than a day";
    }
    TimeStringBuilder builder = new TimeStringBuilder();
    int years = DungeonMath.safeCastLongToInteger(duration / DungeonTimeUnit.YEAR.milliseconds);
    long monthsLong = (duration % DungeonTimeUnit.YEAR.milliseconds) / DungeonTimeUnit.MONTH.milliseconds;
    int months = DungeonMath.safeCastLongToInteger(monthsLong);
    long daysLong = (duration % DungeonTimeUnit.MONTH.milliseconds) / DungeonTimeUnit.DAY.milliseconds;
    int days = DungeonMath.safeCastLongToInteger(daysLong);
    builder.set(DungeonTimeUnit.YEAR, years);
    builder.set(DungeonTimeUnit.MONTH, months);
    builder.set(DungeonTimeUnit.DAY, days);
    return builder.toString();
  }

}
