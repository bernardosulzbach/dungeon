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

package org.dungeon.game;

import org.dungeon.util.Percentage;

/**
 * Random class that encapsulates the single Random object shared by the whole application.
 * The reason for this is that other parts of the code should not be able to call some of Random public methods, such as setSeed.
 */
public class Random {

  private static final java.util.Random RANDOM = new java.util.Random();

  /**
   * Simulates a random roll.
   *
   * @param chance the probability of a true result.
   * @return a boolean indicating if the roll was successful or not.
   */
  public static boolean roll(Percentage chance) {
    return chance.toDouble() > RANDOM.nextDouble();
  }

  /**
   * Simulates a random roll.
   *
   * @param chance the probability of a true result. Must be nonnegative and smaller than or equal to 1.
   *               Will be passed to Percentage's constructors in order to guarantee that it is a valid value.
   * @return a boolean indicating if the roll was successful or not.
   */
  public static boolean roll(double chance) {
    return roll(new Percentage(chance));
  }

  /**
   * Returns a pseudorandom, uniformly distributed boolean.
   *
   * @return a boolean
   */
  public static boolean nextBoolean() {
    return RANDOM.nextBoolean();
  }

  /**
   * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive).
   *
   * @param n the bound on the random number to be returned, must be positive
   * @return an int in the range [0, n)
   */
  public static int nextInteger(int n) {
    return RANDOM.nextInt(n);
  }

}
