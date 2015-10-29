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
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.util.DungeonMath;

import org.jetbrains.annotations.NotNull;

/**
 * A class that generates pseudorandom dates of birth.
 */
public class DateOfBirthGenerator {

  private final Date today;
  private final int age;

  /**
   * Constructs a new DateOfBirthGenerator for a specified date and age.
   *
   * @param now the current date of the World
   * @param age the desired age
   */
  public DateOfBirthGenerator(@NotNull Date now, int age) {
    this.today = now;
    if (age < 0) {
      throw new IllegalArgumentException("age must be nonnegative.");
    }
    this.age = age;
  }

  /**
   * Generates a new random date of birth.
   */
  public Date generateDateOfBirth() {
    Date minimumDate = today.minus(age + 1, DungeonTimeUnit.YEAR).plus(1, DungeonTimeUnit.SECOND);
    int secondsInYear = DungeonMath.safeCastLongToInteger(DungeonTimeUnit.YEAR.as(DungeonTimeUnit.SECOND));
    // Will add up to secondsInYear - 1 seconds to the minimum date, which should respect the year.
    return minimumDate.plus(Random.nextInteger(secondsInYear), DungeonTimeUnit.SECOND);
  }

  @Override
  public String toString() {
    return "DateOfBirthGenerator{" + "today=" + today + ", age=" + age + '}';
  }

}
