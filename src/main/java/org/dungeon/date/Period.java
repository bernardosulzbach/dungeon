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

package org.dungeon.date;

import org.dungeon.util.DungeonMath;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Period class to calculate, store and print in-game date differences.
 */
public class Period implements Serializable {

  /**
   * The duration, in milliseconds.
   */
  private final long difference;

  /**
   * Constructs a period from a specified start date to an end date.
   *
   * @param start the start of the period.
   * @param end the end of the period.
   */
  public Period(@NotNull Date start, @NotNull Date end) {
    difference = end.getTime() - start.getTime();
  }

  /**
   * Constructs a period from the specified duration, in milliseconds.
   *
   * @param duration the duration, in milliseconds
   */
  Period(long duration) {
    difference = duration;
  }

  public long getSeconds() {
    return difference / 1000;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Period period = (Period) o;

    return difference == period.difference;
  }

  @Override
  public int hashCode() {
    return (int) (difference ^ (difference >>> 32));
  }

  @Override
  public String toString() {
    if (difference < DungeonTimeUnit.DAY.milliseconds) {
      return "Less than a day";
    }
    TimeStringBuilder builder = new TimeStringBuilder();
    int years = DungeonMath.safeCastLongToInteger(difference / DungeonTimeUnit.YEAR.milliseconds);
    long monthsLong = (difference % DungeonTimeUnit.YEAR.milliseconds) / DungeonTimeUnit.MONTH.milliseconds;
    int months = DungeonMath.safeCastLongToInteger(monthsLong);
    long daysLong = (difference % DungeonTimeUnit.MONTH.milliseconds) / DungeonTimeUnit.DAY.milliseconds;
    int days = DungeonMath.safeCastLongToInteger(daysLong);
    builder.set(EarthTimeUnit.YEAR, years);
    builder.set(EarthTimeUnit.MONTH, months);
    builder.set(EarthTimeUnit.DAY, days);
    return builder.toString();
  }

}
