package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Random class that encapsulates the single Random object shared by the whole application. The reason for this is that
 * other parts of the code should not be able to call some of Random public methods, such as setSeed.
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

  /**
   * Returns a pseudorandom, uniformly distributed int value between minimum (inclusive) and n (exclusive).
   *
   * @param n the bound on the random number to be returned, must be positive
   * @return an int in the range [minimum, n)
   */
  public static int nextInteger(int minimum, int n) {
    if (minimum >= n) {
      throw new IllegalArgumentException("minimum must be less than n");
    }
    return minimum + nextInteger(n - minimum);
  }

  /**
   * Selects a random element from a List.
   *
   * @param list a List object, not empty, not null
   * @param <T> the type of elements held in the List
   * @return an element of list or null
   */
  public static <T> T select(@NotNull List<T> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException("list is empty.");
    }
    return list.get(nextInteger(list.size()));
  }

}
