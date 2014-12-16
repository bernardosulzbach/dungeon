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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateTest {

  /**
   * As there is not a maximum year defined by the Date class, just test stuff up to the year 1000.
   */
  private static final int MAX_YEAR = 1000;

  @Test
  public void testGetDay() throws Exception {
    for (int i = 1; i <= Date.DAYS_IN_MONTH; i++) {
      assertEquals(i, new Date(1, 1, i).getDay());
    }
  }

  @Test
  public void testGetMonth() throws Exception {
    for (int i = 1; i <= Date.MONTHS_IN_YEAR; i++) {
      assertEquals(i, new Date(1, i, 1).getMonth());
    }
  }

  @Test
  public void testGetYear() throws Exception {
    for (int i = 1; i <= MAX_YEAR; i++) {
      assertEquals(i, new Date(i, 1, 1).getYear());
    }
  }

  @Test
  public void testMinusDays() throws Exception {
    Date date = new Date(1, 1, Date.DAYS_IN_MONTH);
    for (long i = Date.DAYS_IN_MONTH; i > 0; i--) {
      assertEquals(i, date.getDay());
      date = date.minusDays(1);
    }
  }

  @Test
  public void testMinusMonths() throws Exception {
    Date date = new Date(1, Date.MONTHS_IN_YEAR, 1);
    for (long i = Date.MONTHS_IN_YEAR; i > 0; i--) {
      assertEquals(i, date.getMonth());
      date = date.minusMonths(1);
    }
  }

  @Test
  public void testMinusYears() throws Exception {
    Date date = new Date(MAX_YEAR, 1, 1);
    for (long i = MAX_YEAR; i > 0; i--) {
      assertEquals(i, date.getYear());
      date = date.minusYears(1);
    }
  }

  @Test
  public void testPlusDays() throws Exception {
    Date date = new Date(1, 1, 1);
    for (long i = 1; i <= Date.DAYS_IN_MONTH; i++) {
      assertEquals(i, date.getDay());
      date = date.plusDays(1);
    }
  }

  @Test
  public void testPlusMonths() throws Exception {
    Date date = new Date(1, 1, 1);
    for (long i = 1; i <= Date.MONTHS_IN_YEAR; i++) {
      assertEquals(i, date.getMonth());
      date = date.plusMonths(1);
    }
  }

  @Test
  public void testPlusYears() throws Exception {
    Date date = new Date(1, 1, 1);
    for (long i = 1; i <= MAX_YEAR; i++) {
      assertEquals(i, date.getYear());
      date = date.plusYears(1);
    }
  }

}