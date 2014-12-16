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

  private final long time;
  private static final long daysInMonth = 10;
  private static final long monthsInYear = 10;
  private static final long millisInDay = 1000 * 60 * 60 * 24;
  private static final long millisInMonth = millisInDay * daysInMonth;
  private static final long millisInYear = millisInMonth * monthsInYear;

  private Date() {
    time = 0;
  }

  private Date(long millis) {
    time = millis;
  }

  public Date(int year, int month, int day) {
    if (year < 0) {
      DLogger.warning("Tried to construct Date with negative year!");
      year = 0;
    }
    if (month < 0) {
      DLogger.warning("Tried to construct Date with negative month!");
      month = 0;
    }
    if (day < 0) {
      DLogger.warning("Tried to construct Date with negative day!");
      day = 0;
    }
    time = millisInYear * year + millisInMonth * month + millisInDay * day;
  }

  public long getDay() {
    return (time % millisInMonth) / millisInDay;
  }

  public long getMonth() {
    return (time % millisInYear) / millisInMonth;
  }

  public long getYear() {
    return time / millisInYear;
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

}