package org.mafagafogigante.dungeon.date;

import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.DAY;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.HOUR;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.MINUTE;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.MONTH;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.SECOND;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.YEAR;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DungeonTimeParserTest {

  @Test
  public void parsePeriodShouldWorkWithSingulars() throws Exception {
    Assertions.assertEquals(DungeonTimeParser.parseDuration("1 millisecond"), new Duration(1));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("1 second"), new Duration(SECOND.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("1 hour"), new Duration(HOUR.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("1 minute"), new Duration(MINUTE.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("1 day"), new Duration(DAY.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("1 month"), new Duration(MONTH.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("1 year"), new Duration(YEAR.milliseconds));
  }

  @Test
  public void parsePeriodShouldWorkWithPlurals() throws Exception {
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 milliseconds"), new Duration(2));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 seconds"), new Duration(2 * SECOND.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 minutes"), new Duration(2 * MINUTE.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 hours"), new Duration(2 * HOUR.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 days"), new Duration(2 * DAY.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 months"), new Duration(2 * MONTH.milliseconds));
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 years"), new Duration(2 * YEAR.milliseconds));
  }

  @Test
  public void parsePeriodShouldWorkWithMultipleUnits() throws Exception {
    Duration twoMinutesAndTenSeconds = new Duration(2 * MINUTE.milliseconds + 10 * SECOND.milliseconds);
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 minutes and 10 seconds"), twoMinutesAndTenSeconds);
  }

  @Test
  public void parsePeriodShouldWorkWithTheJavadocExample() throws Exception {
    long duration = 2 * YEAR.milliseconds + 5 * MONTH.milliseconds + 8 * DAY.milliseconds + 20 * HOUR.milliseconds;
    Duration javadocExample = new Duration(duration);
    Assertions.assertEquals(DungeonTimeParser.parseDuration("2 years, 5 months, 8 days, and 20 hours"), javadocExample);
  }

  @Test
  public void parsePeriodShouldThrowExceptionOnEmptyString() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonTimeParser.parseDuration("");
    });
  }

  @Test
  public void parsePeriodShouldThrowExceptionOnMissingUnits() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonTimeParser.parseDuration("1");
    });
  }

  @Test
  public void parsePeriodShouldThrowExceptionOnMissingMultipliers() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonTimeParser.parseDuration("second");
    });
  }

  @Test
  public void parsePeriodShouldThrowExceptionOnNegativeMultipliers() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonTimeParser.parseDuration("-1 seconds");
    });
  }

  @Test
  public void parsePeriodShouldThrowExceptionOnZeroMultipliers() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonTimeParser.parseDuration("0 seconds");
    });
  }

  @Test
  public void parsePeriodShouldThrowExceptionOnMalformedInput() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonTimeParser.parseDuration("and second");
    });
  }

  @Test
  public void parsePeriodShouldThrowExceptionOnInvalidUnits() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonTimeParser.parseDuration("1 foo");
    });
  }

}
