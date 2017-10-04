package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.DungeonTimeUnit;
import org.mafagafogigante.dungeon.date.Duration;

import org.junit.Assert;
import org.junit.Test;

public class DateOfBirthGeneratorTest {

  @Test
  public void generateDateOfBirthShouldGenerateValidDates() throws Exception {
    Date now = new Date(1000, 1, 1);
    for (int age = 0; age < 100; age++) {
      DateOfBirthGenerator dateOfBirthGenerator = new DateOfBirthGenerator(now, age);
      for (int i = 0; i < 100; i++) {
        Date dateOfBirth = dateOfBirthGenerator.generateDateOfBirth();
        Duration duration = new Duration(dateOfBirth, now);
        long maximumSeconds = (age + 1) * DungeonTimeUnit.YEAR.as(DungeonTimeUnit.SECOND);
        Assert.assertTrue(duration.getSeconds() < maximumSeconds);
      }
    }
  }

}
