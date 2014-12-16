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

  private static final long millisInSecond = 1000;
  private static final long millisInMinute = millisInSecond * SECONDS_IN_MINUTE;
  private static final long millisInHour = millisInMinute * MINUTES_IN_HOUR;
  private static final long millisInDay = millisInHour * HOURS_IN_DAY;
  private static final long millisInMonth = millisInDay * DAYS_IN_MONTH;
  private static final long millisInYear = millisInMonth * MONTHS_IN_YEAR;

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
    time += hour * millisInHour + minute * millisInMinute + second * millisInSecond;
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
    time = millisInYear * (year - 1) + millisInMonth * (month - 1) + millisInDay * (day - 1);
  }

  public long getTime() {
    return time;
  }

  public long getSecond() {
    return (time % millisInMinute) / millisInSecond;
  }

  public long getMinute() {
    return (time % millisInHour) / millisInMinute;
  }

  public long getHour() {
    return (time % millisInDay) / millisInHour;
  }

  public long getDay() {
    return (time % millisInMonth) / millisInDay + 1;
  }

  public long getMonth() {
    return (time % millisInYear) / millisInMonth + 1;
  }

  public long getYear() {
    return time / millisInYear + 1;
  }

  public Date minusSeconds(int seconds) {
    if (seconds < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - seconds * millisInSecond);
  }

  public Date minusMinutes(int minutes) {
    if (minutes < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - minutes * millisInMinute);
  }

  public Date minusHours(int hours) {
    if (hours < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - hours * millisInHour);
  }

  public Date minusDays(int days) {
    if (days < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - days * millisInDay);
  }

  public Date minusMonths(int months) {
    if (months < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - months * millisInMonth);
  }

  public Date minusYears(int years) {
    if (years < 0) {
      DLogger.warning("Passed negative argument to minus method.");
    }
    return new Date(time - years * millisInYear);
  }

  public Date plusSeconds(int seconds) {
    if (seconds < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + seconds * millisInSecond);
  }

  public Date plusMinutes(int minutes) {
    if (minutes < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + minutes * millisInMinute);
  }

  public Date plusHours(int hours) {
    if (hours < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + hours * millisInHour);
  }

  public Date plusDays(int days) {
    if (days < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + days * millisInDay);
  }

  public Date plusMonths(int months) {
    if (months < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + months * millisInMonth);
  }

  public Date plusYears(int years) {
    if (years < 0) {
      DLogger.warning("Passed negative argument to plus method.");
    }
    return new Date(time + years * millisInYear);
  }

  public String toDateString() {
    return "day " + getDay() + " of month " + getMonth() + " of the year " + getYear();
  }

  public String toTimeString() {
    return getHour() + ":" + getMinute() + ":" + getSecond();
  }
}