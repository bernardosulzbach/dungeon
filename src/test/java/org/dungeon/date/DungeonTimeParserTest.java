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

package org.dungeon.date;

import static org.dungeon.date.DungeonTimeUnit.DAY;
import static org.dungeon.date.DungeonTimeUnit.HOUR;
import static org.dungeon.date.DungeonTimeUnit.MINUTE;
import static org.dungeon.date.DungeonTimeUnit.MONTH;
import static org.dungeon.date.DungeonTimeUnit.SECOND;
import static org.dungeon.date.DungeonTimeUnit.YEAR;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class DungeonTimeParserTest {

  @Test
  public void testParsePeriod() throws Exception {
    // Basic tests.
    assertTrue(DungeonTimeParser.parsePeriod("1 millisecond").equals(new Period(1)));
    assertTrue(DungeonTimeParser.parsePeriod("2 milliseconds").equals(new Period(2)));
    assertTrue(DungeonTimeParser.parsePeriod("1 second").equals(new Period(SECOND.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("2 seconds").equals(new Period(2 * SECOND.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("1 minute").equals(new Period(MINUTE.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("2 minutes").equals(new Period(2 * MINUTE.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("1 hour").equals(new Period(HOUR.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("2 hours").equals(new Period(2 * HOUR.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("1 day").equals(new Period(DAY.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("2 days").equals(new Period(2 * DAY.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("1 month").equals(new Period(MONTH.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("2 months").equals(new Period(2 * MONTH.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("1 year").equals(new Period(YEAR.milliseconds)));
    assertTrue(DungeonTimeParser.parsePeriod("2 years").equals(new Period(2 * YEAR.milliseconds)));
    // Advanced tests.
    Period twoMinutesAndTenSeconds = new Period(2 * MINUTE.milliseconds + 10 * SECOND.milliseconds);
    assertTrue(DungeonTimeParser.parsePeriod("2 minutes and 10 seconds").equals(twoMinutesAndTenSeconds));
    // Javadoc example.
    long duration = 2 * YEAR.milliseconds + 5 * MONTH.milliseconds + 8 * DAY.milliseconds + 20 * HOUR.milliseconds;
    Period javadocExample = new Period(duration);
    assertTrue(DungeonTimeParser.parsePeriod("2 years, 5 months, 8 days, and 20 hours").equals(javadocExample));
    // Exception tests.
    try {
      DungeonTimeParser.parsePeriod("");
      Assert.fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      DungeonTimeParser.parsePeriod("1");
      Assert.fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      DungeonTimeParser.parsePeriod("second");
      Assert.fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      DungeonTimeParser.parsePeriod("and second");
      Assert.fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      DungeonTimeParser.parsePeriod("-1 seconds");
      Assert.fail();
    } catch (DungeonTimeParser.InvalidMultiplierException expected) {
    }
    try {
      DungeonTimeParser.parsePeriod("0 seconds");
      Assert.fail();
    } catch (DungeonTimeParser.InvalidMultiplierException expected) {
    }
    try {
      DungeonTimeParser.parsePeriod("1 foo");
      Assert.fail();
    } catch (DungeonTimeParser.InvalidUnitException expected) {
    }
  }

}