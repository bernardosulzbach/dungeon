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

import java.io.Serializable;

/**
 * Period class to calculate, store and print date differences.
 */
public class Period implements Serializable {

  private static final String LESS_THAN_A_DAY = "Less than a day";

  private final long difference;

  /**
   * Constructs a period from a specified start date to an end date.
   *
   * @param start the start of the period.
   * @param end   the end of the period.
   */
  public Period(Date start, Date end) {
    difference = end.getTime() - start.getTime();
  }

  public long getSeconds() {
    return difference / 1000;
  }

  @Override
  public String toString() {
    long years = difference / Date.MILLIS_IN_YEAR;
    long months = (difference % Date.MILLIS_IN_YEAR) / Date.MILLIS_IN_MONTH;
    long days = (difference % Date.MILLIS_IN_MONTH) / Date.MILLIS_IN_DAY;
    StringBuilder builder = new StringBuilder();
    if (years != 0) {
      if (years == 1) {
        builder.append(years).append(" year");
      } else {
        builder.append(years).append(" years");
      }
    }
    if (months != 0) {
      if (builder.length() != 0) {
        if (days == 0) {
          builder.append(" and ");
        } else {
          builder.append(", ");
        }
      }
      if (months == 1) {
        builder.append(months).append(" month");
      } else {
        builder.append(months).append(" months");
      }
    }
    if (days != 0) {
      if (builder.length() != 0) {
        builder.append(" and ");
      }
      if (days == 1) {
        builder.append(days).append(" day");
      } else {
        builder.append(days).append(" days");
      }
    }
    if (builder.length() == 0) {
      builder.append(LESS_THAN_A_DAY);
    }
    return builder.toString();
  }

}
