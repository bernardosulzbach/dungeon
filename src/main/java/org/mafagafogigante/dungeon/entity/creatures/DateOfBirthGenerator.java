package org.mafagafogigante.dungeon.entity.creatures;

import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.SECOND;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.YEAR;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.util.DungeonMath;

import org.jetbrains.annotations.NotNull;

/**
 * A class that generates pseudorandom dates of birth.
 */
class DateOfBirthGenerator {

  private static final int SECONDS_IN_YEAR = DungeonMath.safeCastLongToInteger(YEAR.as(SECOND));
  private final Date today;
  private final int age;

  /**
   * Constructs a new DateOfBirthGenerator for a specified date and age.
   *
   * @param now the current date of the World
   * @param age the desired age
   */
  DateOfBirthGenerator(@NotNull Date now, int age) {
    this.today = now;
    if (age < 0) {
      throw new IllegalArgumentException("age must be nonnegative.");
    }
    this.age = age;
  }

  /**
   * Generates a new random date of birth.
   */
  Date generateDateOfBirth() {
    Date minimumDate = today.minus(age + 1, YEAR).plus(1, SECOND);
    // Will add up to SECONDS_IN_YEAR - 1 seconds to the minimum date, which should respect the year.
    return minimumDate.plus(Random.nextInteger(SECONDS_IN_YEAR - 1) + 1, SECOND);
  }

  @Override
  public String toString() {
    return "DateOfBirthGenerator{" + "today=" + today + ", age=" + age + '}';
  }

}
