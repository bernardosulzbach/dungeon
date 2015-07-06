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

import static org.dungeon.date.DungeonTimeUnit.DAY;
import static org.dungeon.date.DungeonTimeUnit.HOUR;
import static org.dungeon.date.DungeonTimeUnit.MINUTE;
import static org.dungeon.date.DungeonTimeUnit.MONTH;
import static org.dungeon.date.DungeonTimeUnit.SECOND;
import static org.dungeon.date.DungeonTimeUnit.YEAR;

import org.dungeon.io.DLogger;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Dungeon Date class. Stores an immutable time stamp.
 * <p/>
 * Dungeon's date system has years of 10 months, months of 10 days and days of 24 hours of 60 minutes of 60 seconds.
 * All the public constants made available by this class refer to this date system.
 * Therefore they should not be used to represent or take place into calculations of real world time.
 */
public class Date implements Comparable<Date>, Serializable {

  /**
   * The time, in milliseconds.
   */
  private long time; // Supports 1067519911 full years.

  private Date(long millis) {
    time = millis;
  }

  public Date(long year, long month, long day, long hour, long minute, long second) {
    this(year, month, day);
    if (hour < 0) {
      DLogger.warning("Tried to construct Date with negative hour!");
      hour = 0;
    } else if (hour >= DAY.as(HOUR)) {
      DLogger.warning("Tried to construct Date with nonexistent hour.");
      // First hour of the next day. Even if the code supplied this, log a warning as this is likely a bug.
      hour = DAY.as(HOUR);
    }
    if (minute < 0) {
      DLogger.warning("Tried to construct Date with negative minute!");
      minute = 0;
    } else if (minute >= HOUR.as(MINUTE)) {
      DLogger.warning("Tried to construct Date with nonexistent minute.");
      // First minute of the next hour. Even if the code supplied this, log a warning as this is likely a bug.
      minute = HOUR.as(MINUTE);
    }
    if (second < 0) {
      DLogger.warning("Tried to construct Date with negative second!");
      second = 0;
    } else if (second >= MINUTE.as(SECOND)) {
      DLogger.warning("Tried to construct Date with nonexistent second.");
      // First second of the next minute. Even if the code supplied this, log a warning as this is likely a bug.
      second = MINUTE.as(SECOND);
    }
    time += hour * HOUR.milliseconds + minute * MINUTE.milliseconds + second * SECOND.milliseconds;
  }

  public Date(long year, long month, long day) {
    if (year <= 0) {
      DLogger.warning("Tried to construct Date with nonpositive year!");
      year = 1;
    }
    if (month <= 0) {
      DLogger.warning("Tried to construct Date with nonpositive month!");
      month = 1;
    } else if (month > YEAR.as(MONTH)) {
      DLogger.warning("Tried to construct Date with nonexistent month.");
      month = YEAR.as(MONTH);
    }
    if (day <= 0) {
      DLogger.warning("Tried to construct Date with nonpositive day!");
      day = 1;
    } else if (day > MONTH.as(DAY)) {
      DLogger.warning("Tried to construct Date with nonexistent day.");
      day = MONTH.as(DAY);
    }
    time = YEAR.milliseconds * (year - 1) + MONTH.milliseconds * (month - 1) + DAY.milliseconds * (day - 1);
  }

  public long getTime() {
    return time;
  }

  private long getSecond() {
    return (time % MINUTE.milliseconds) / SECOND.milliseconds;
  }

  private long getMinute() {
    return (time % HOUR.milliseconds) / MINUTE.milliseconds;
  }

  public long getHour() {
    return (time % DAY.milliseconds) / HOUR.milliseconds;
  }

  public long getDay() {
    return (time % MONTH.milliseconds) / DAY.milliseconds + 1;
  }

  public long getMonth() {
    return (time % YEAR.milliseconds) / MONTH.milliseconds + 1;
  }

  public long getYear() {
    return time / YEAR.milliseconds + 1;
  }

  /**
   * Returns a new Date object corresponding to this Date plus the specified amount of time.
   *
   * @param amount a positive integer
   * @param unit   a DungeonTimeUnit value, not null
   * @return a new Date object
   * @throws IllegalArgumentException if amount is not positive or if unit is null
   */
  public Date plus(int amount, DungeonTimeUnit unit) {
    if (amount <= 0) {
      throw new IllegalArgumentException("amount must be positive.");
    } else if (unit == null) {
      throw new IllegalArgumentException("unit should not be null.");
    } else {
      return new Date(time + amount * unit.milliseconds);
    }
  }

  /**
   * Returns a new Date object corresponding to this Date minus the specified amount of time.
   *
   * @param amount a positive integer
   * @param unit   a DungeonTimeUnit value, not null
   * @return a new Date object
   * @throws IllegalArgumentException if amount is not positive or if unit is null
   */
  public Date minus(int amount, DungeonTimeUnit unit) {
    if (amount <= 0) {
      throw new IllegalArgumentException("amount must be positive.");
    } else if (unit == null) {
      throw new IllegalArgumentException("unit should not be null.");
    } else {
      return new Date(time - amount * unit.milliseconds);
    }
  }

  public String toDateString() {
    return "day " + getDay() + " of month " + getMonth() + " of the year " + getYear();
  }

  public String toTimeString() {
    return String.format("%02d:%02d:%02d", getHour(), getMinute(), getSecond());
  }

  @Override
  public int compareTo(@NotNull Date date) {
    if (time > date.time) {
      return 1;
    } else if (time == date.time) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * Returns a String representation of this date, from year to second.
   *
   * @return a String
   */
  @Override
  public String toString() {
    String format = "%d-%02d-%02d %02d:%02d:%02d";
    return String.format(format, getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond());
  }

}