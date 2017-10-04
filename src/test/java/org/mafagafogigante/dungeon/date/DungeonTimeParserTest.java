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
  public void parsePeriodShouldWorkWithSingulars() throws Exception {
    Assert.assertTrue(DungeonTimeParser.parseDuration("1 millisecond").equals(new Duration(1)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("1 second").equals(new Duration(SECOND.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("1 hour").equals(new Duration(HOUR.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("1 minute").equals(new Duration(MINUTE.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("1 day").equals(new Duration(DAY.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("1 month").equals(new Duration(MONTH.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("1 year").equals(new Duration(YEAR.milliseconds)));
  }

  @Test
  public void parsePeriodShouldWorkWithPlurals() throws Exception {
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 milliseconds").equals(new Duration(2)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 seconds").equals(new Duration(2 * SECOND.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 minutes").equals(new Duration(2 * MINUTE.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 hours").equals(new Duration(2 * HOUR.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 days").equals(new Duration(2 * DAY.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 months").equals(new Duration(2 * MONTH.milliseconds)));
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 years").equals(new Duration(2 * YEAR.milliseconds)));
  }

  @Test
  public void parsePeriodShouldWorkWithMultipleUnits() throws Exception {
    Duration twoMinutesAndTenSeconds = new Duration(2 * MINUTE.milliseconds + 10 * SECOND.milliseconds);
    Assert.assertTrue(DungeonTimeParser.parseDuration("2 minutes and 10 seconds").equals(twoMinutesAndTenSeconds));
  }

  @Test
  public void parsePeriodShouldWorkWithTheJavadocExample() throws Exception {
    long duration = 2 * YEAR.milliseconds + 5 * MONTH.milliseconds + 8 * DAY.milliseconds + 20 * HOUR.milliseconds;
    Duration javadocExample = new Duration(duration);
    Assert
        .assertTrue(DungeonTimeParser.parseDuration("2 years, 5 months, 8 days, and 20 hours").equals(javadocExample));
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePeriodShouldThrowExceptionOnEmptyString() throws Exception {
    DungeonTimeParser.parseDuration("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePeriodShouldThrowExceptionOnMissingUnits() throws Exception {
    DungeonTimeParser.parseDuration("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePeriodShouldThrowExceptionOnMissingMultipliers() throws Exception {
    DungeonTimeParser.parseDuration("second");
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePeriodShouldThrowExceptionOnNegativeMultipliers() throws Exception {
    DungeonTimeParser.parseDuration("-1 seconds");
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePeriodShouldThrowExceptionOnZeroMultipliers() throws Exception {
    DungeonTimeParser.parseDuration("0 seconds");
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePeriodShouldThrowExceptionOnMalformedInput() throws Exception {
    DungeonTimeParser.parseDuration("and second");
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePeriodShouldThrowExceptionOnInvalidUnits() throws Exception {
    DungeonTimeParser.parseDuration("1 foo");
  }

}
