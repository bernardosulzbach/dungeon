package org.mafagafogigante.dungeon.util;

import org.jetbrains.annotations.NotNull;

/**
 * A collection of mathematical utility methods.
 */
public final class DungeonMath {

  private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-8;

  private DungeonMath() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Evaluates the weighted average of two values.
   *
   * @param first the first value
   * @param second the second value
   * @param firstContribution how much the second value contributes to the average
   * @return the weighted average between the two values
   */
  public static double weightedAverage(double first, double second, Percentage firstContribution) {
    return first + (second - first) * firstContribution.toDouble();
  }

  /**
   * Compares two doubles with the default tolerance margin.
   */
  static int fuzzyCompare(double first, double second) {
    return fuzzyCompare(first, second, DEFAULT_DOUBLE_TOLERANCE);
  }

  /**
   * Compares two doubles with a specified tolerance margin.
   */
  private static int fuzzyCompare(double first, double second, double epsilon) {
    if (first + epsilon < second) {
      return -1;
    } else if (first - epsilon > second) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * Safely casts a long into an integer.
   *
   * @param value the long that will be converted, should be in the range [Integer.MIN_VALUE, Integer.MAX_VALUE]
   * @return an integer equal to the provided long
   * @throws IllegalArgumentException if the long does not fit into an integer
   */
  public static int safeCastLongToInteger(long value) {
    if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
      throw new IllegalArgumentException(value + " does not fit into an integer.");
    } else {
      return (int) value;
    }
  }

  /**
   * Returns the sum of an array of integers.
   *
   * @param integers the array of integers, not null
   * @return the sum
   */
  static int sum(@NotNull int[] integers) {
    int total = 0;
    for (int integer : integers) {
      total += integer;
    }
    return total;
  }

}
