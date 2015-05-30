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

import org.junit.Assert;
import org.junit.Test;

public class PeriodTest {

  @Test
  public void testToString() throws Exception {
    Date start = new Date(2014, Date.MONTHS_IN_YEAR, Date.DAYS_IN_MONTH, 0, 0, 0);
    Date end = new Date(2014, Date.MONTHS_IN_YEAR, Date.DAYS_IN_MONTH, 0, 0, 0);

    end = end.plusDays(1);
    Assert.assertEquals("1 day", new Period(start, end).toString());
    end = end.plusDays(1);
    Assert.assertEquals("2 days", new Period(start, end).toString());
    end = end.minusDays(2);

    end = end.plusMonths(1);
    Assert.assertEquals("1 month", new Period(start, end).toString());
    end = end.plusMonths(1);
    Assert.assertEquals("2 months", new Period(start, end).toString());
    end = end.plusDays(1);
    Assert.assertEquals("2 months and 1 day", new Period(start, end).toString());
    end = end.plusDays(1);
    Assert.assertEquals("2 months and 2 days", new Period(start, end).toString());
    end = end.minusDays(2);
    end = end.minusMonths(2);

    end = end.plusYears(1);
    Assert.assertEquals("1 year", new Period(start, end).toString());
    end = end.plusYears(1);
    Assert.assertEquals("2 years", new Period(start, end).toString());
    end = end.plusDays(1);
    Assert.assertEquals("2 years and 1 day", new Period(start, end).toString());
    end = end.minusDays(1);
    end = end.plusMonths(1);
    Assert.assertEquals("2 years and 1 month", new Period(start, end).toString());
    end = end.plusDays(1);
    Assert.assertEquals("2 years, 1 month and 1 day", new Period(start, end).toString());
  }

}
