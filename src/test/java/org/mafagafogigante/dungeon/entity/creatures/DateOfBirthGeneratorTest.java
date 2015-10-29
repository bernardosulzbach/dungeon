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
