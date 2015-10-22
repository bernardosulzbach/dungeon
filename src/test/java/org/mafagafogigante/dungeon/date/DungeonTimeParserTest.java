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

package org.mafagafogigante.dungeon.date;

import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.DAY;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.HOUR;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.MINUTE;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.MONTH;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.SECOND;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.YEAR;

import org.junit.Assert;
import org.junit.Test;

public class DungeonTimeParserTest {

  @Test
  public void testParsePeriod() throws Exception {
    // Basic tests.
    Assert.assertTrue(DungeonTimeParser.parsePeriod("1 millisecond").equals(new Duration(1)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 milliseconds").equals(new Duration(2)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("1 second").equals(new Duration(SECOND.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 seconds").equals(new Duration(2 * SECOND.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("1 minute").equals(new Duration(MINUTE.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 minutes").equals(new Duration(2 * MINUTE.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("1 hour").equals(new Duration(HOUR.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 hours").equals(new Duration(2 * HOUR.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("1 day").equals(new Duration(DAY.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 days").equals(new Duration(2 * DAY.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("1 month").equals(new Duration(MONTH.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 months").equals(new Duration(2 * MONTH.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("1 year").equals(new Duration(YEAR.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 years").equals(new Duration(2 * YEAR.milliseconds)));
    // Advanced tests.
    Duration twoMinutesAndTenSeconds = new Duration(2 * MINUTE.milliseconds + 10 * SECOND.milliseconds);
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 minutes and 10 seconds").equals(twoMinutesAndTenSeconds));
    // Javadoc example.
    long duration = 2 * YEAR.milliseconds + 5 * MONTH.milliseconds + 8 * DAY.milliseconds + 20 * HOUR.milliseconds;
    Duration javadocExample = new Duration(duration);
    Assert.assertTrue(DungeonTimeParser.parsePeriod("2 years, 5 months, 8 days, and 20 hours").equals(javadocExample));
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