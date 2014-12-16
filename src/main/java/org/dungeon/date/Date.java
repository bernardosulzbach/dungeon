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

import org.dungeon.io.DLogger;

import java.io.Serializable;

/**
 * Dungeon Date class. Stores an immutable time stamp.
 * <p/>
 * The new Date system has eras of 800 years, years of 10 months, months of 10 days and days of 24 hours of 60 minutes.
 * <p/>
 * Created by Bernardo Sulzbach on 16/12/14.
 */
public class Date implements Serializable {

  public static final long SECONDS_IN_MINUTE = 60;
  public static final long MINUTES_IN_HOUR = 60;
  public static final long HOURS_IN_DAY = 24;
  public static final long DAYS_IN_MONTH = 10;
  public static final long MONTHS_IN_YEAR = 10;

  static final long MILLIS_IN_SECOND = 1000;
  static final long MILLIS_IN_MINUTE = MILLIS_IN_SECOND * SECONDS_IN_MINUTE;
  static final long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * MINUTES_IN_HOUR;
  static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * HOURS_IN_DAY;
  static final long MILLIS_IN_MONTH = MILLIS_IN_DAY * DAYS_IN_MONTH;
  static final long MILLIS_IN_YEAR = MILLIS_IN_MONTH * MONTHS_IN_YEAR;

  private long time;

  protected Date(long millis) {
    time = millis;
  }

  public Date(long year, long month, long day, long hour, long minute, long second) {
    this(year, month, day);
    if (hour < 0) {
      DLogger.warning("Tried to construct Date with negative hour!");
      hour = 0;
    } else if (hour >= HOURS_IN_DAY) {
      DLogger.warning("Tried to construct Date with nonexistent hour.");
      // First hour of the next day. Even if the code supplied this, log a warning as this is likely a bug.
      hour = HOURS_IN_DAY;
    }
    if (minute < 0) {
      DLogger.warning("Tried to construct Date with negative minute!");
      minute = 0;
    } else if (minute >= MINUTES_IN_HOUR) {
      DLogger.warning("Tried to construct Date with nonexistent minute.");
      // First minute of the next hour. Even if the code supplied this, log a warning as this is likely a bug.
      minute = MINUTES_IN_HOUR;
    }
    if (second < 0) {
      DLogger.warning("Tried to construct Date with negative second!");
      second = 0;
    } else if (second >= SECONDS_IN_MINUTE) {
      DLogger.warning("Tried to construct Date with nonexistent second.");
      // First second of the next minute. Even if the code supplied this, log a warning as this is likely a bug.
      second = SECONDS_IN_MINUTE;
    }
    time += hour * MILLIS_IN_HOUR + minute * MILLIS_IN_MINUTE + second * MILLIS_IN_SECOND;
  }

  public Date(long year, long month, long day) {
    if (year <= 0) {
      DLogger.warning("Tried to construct Date with nonpositive year!");
      year = 1;
    }
    if (month <= 0) {
      DLogger.warning("Tried to construct Date with nonpositive month!");
      month = 1;
    } else if (month > MONTHS_IN_YEAR) {
      DLogger.warning("Tried to construct Date with nonexistent month.");
      month = MONTHS_IN_YEAR;
    }
    if (day <= 0) {
      DLogger.warning("Tried to construct Date with nonpositive day!");
      day = 1;
    } else if (day > DAYS_IN_MONTH) {
      DLogger.warning("Tried to construct Date with nonexistent day.");
      day = DAYS_IN_MONTH;
    }
    time = MILLIS_IN_YEAR * (year - 1) + MILLIS_IN_MONTH * (month - 1) + MILLIS_IN_DAY * (day - 1);
  }

  public long getTime() {
    return time;
  }

  public long getSecond() {
    return (time % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND;
  }

  public long getMinute() {
    return (time % MILLIS_IN_HOUR) / MILLIS_IN_MINUTE;
  }

  public long getHour() {
    return (time % MILLIS_IN_DAY) / MILLIS_IN_HOUR;
  }

  public long getDay() {
    return (time % MILLIS_IN_MONTH) / MILLIS_IN_DAY + 1;
  }

  public long getMonth() {
    return (time % MILLIS_IN_YEAR) / MILLIS_IN_MONTH + 1;
  }

  public long getYear() {
    return time / MILLIS_IN_YEAR + 1;
  }

  public Date minusSeconds(int seconds) {
    if (seconds < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - seconds * MILLIS_IN_SECOND);
  }

  public Date minusMinutes(int minutes) {
    if (minutes < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - minutes * MILLIS_IN_MINUTE);
  }

  public Date minusHours(int hours) {
    if (hours < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - hours * MILLIS_IN_HOUR);
  }

  public Date minusDays(int days) {
    if (days < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - days * MILLIS_IN_DAY);
  }

  public Date minusMonths(int months) {
    if (months < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - months * MILLIS_IN_MONTH);
  }

  public Date minusYears(int years) {
    if (years < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - years * MILLIS_IN_YEAR);
  }

  public Date plusSeconds(int seconds) {
    if (seconds < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + seconds * MILLIS_IN_SECOND);
  }

  public Date plusMinutes(int minutes) {
    if (minutes < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + minutes * MILLIS_IN_MINUTE);
  }

  public Date plusHours(int hours) {
    if (hours < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + hours * MILLIS_IN_HOUR);
  }

  public Date plusDays(int days) {
    if (days < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + days * MILLIS_IN_DAY);
  }

  public Date plusMonths(int months) {
    if (months < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + months * MILLIS_IN_MONTH);
  }

  public Date plusYears(int years) {
    if (years < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + years * MILLIS_IN_YEAR);
  }

  public String toDateString() {
    return "day " + getDay() + " of month " + getMonth() + " of the year " + getYear();
  }

  public String toTimeString() {
    return getHour() + ":" + getMinute() + ":" + getSecond();
  }
}