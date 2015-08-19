/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.dungeon.util;

import org.dungeon.commands.IssuedCommand;
import org.dungeon.gui.GameWindow;
import org.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * A collection of mathematical utility methods.
 */
public final class DungeonMath {

  private static final int SECOND_IN_NANOSECONDS = 1000000000;
  private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-8;
  private static final String TIMEOUT = "TIMEOUT";

  private DungeonMath() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Evaluates the weighted average of two values.
   *
   * @param a the first value
   * @param b the second value
   * @param bContribution how much the second value contributes to the average
   * @return the weighted average between the two values
   */
  public static double weightedAverage(double a, double b, Percentage bContribution) {
    return a + (b - a) * bContribution.toDouble();
  }

  /**
   * Calculates the arithmetic mean of a sequence of doubles.
   */
  public static double mean(double... values) {
    double sum = 0;
    for (double value : values) {
      sum += value;
    }
    return sum / values.length;
  }

  /**
   * Compares two doubles with the default tolerance margin.
   */
  public static int fuzzyCompare(double a, double b) {
    return fuzzyCompare(a, b, DEFAULT_DOUBLE_TOLERANCE);
  }

  /**
   * Compares two doubles with a specified tolerance margin.
   */
  private static int fuzzyCompare(double a, double b, double epsilon) {
    if (a + epsilon < b) {
      return -1;
    } else if (a - epsilon > b) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * Parses the "fibonacci" command.
   */
  public static void parseFibonacci(IssuedCommand issuedCommand) {
    int n;
    if (issuedCommand.hasArguments()) {
      for (String argument : issuedCommand.getArguments()) {
        try {
          n = Integer.parseInt(argument);
        } catch (NumberFormatException warn) {
          Messenger.printInvalidNumberFormatOrValue();
          return;
        }
        if (n < 1) {
          Messenger.printInvalidNumberFormatOrValue();
          return;
        }
        String result = fibonacci(n);
        if (result.equals(TIMEOUT)) {
          Writer.writeString("Calculation exceeded the time limit.");
        } else {
          Writer.writeString(functionEvaluationString("fibonacci", String.valueOf(n), fibonacci(n)));
        }
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  /**
   * Finds the n-th element of the fibonacci sequence if it can be computed in less than one second.
   *
   * @param n the position of the element on the sequence
   * @return a String representation of the number or the {@code TIMEOUT} constant
   */
  private static String fibonacci(int n) {
    // Allow this method to run for one second.
    final long interruptTime = System.nanoTime() + SECOND_IN_NANOSECONDS;
    BigInteger a = BigInteger.ZERO;
    BigInteger b = BigInteger.ONE;
    // Swap variable.
    BigInteger s;
    for (int i = 1; i < n; i++) {
      s = a;
      a = b;
      b = b.add(s);
      if (System.nanoTime() >= interruptTime) {
        return TIMEOUT;
      }
    }
    return a.toString();
  }

  /**
   * Makes a pretty String representation of a function evaluation.
   *
   * <p>Example: {@code functionName(argument) = result}
   *
   *
   * <p>If the String exceeds the maximum number of columns, a backslash is used to break lines.
   *
   * @param functionName the name of the function
   * @param argument the argument passed to the function
   * @param result the result of the evaluation
   * @return a String longer than the three provided Strings combined
   */
  private static String functionEvaluationString(String functionName, String argument, String result) {
    String original = String.format("%s(%s) = %s", functionName, argument, result);
    return insertBreaksAtTheColumnLimit(original);
  }

  /**
   * Inserts line breaks (a backslash followed by a newline) at all the indices that are multiples of the column count.
   *
   * @param string the original String
   * @return a String with no lines longer than the column count defined in org.dungeon.util.Constants
   */
  private static String insertBreaksAtTheColumnLimit(String string) {
    if (string.length() <= GameWindow.COLS) {
      return string;
    }
    StringBuilder builder = new StringBuilder();
    int charactersOnThisLine = 0;
    for (char character : string.toCharArray()) {
      if (charactersOnThisLine == GameWindow.COLS) {
        builder.insert(builder.length() - 1, "\\\n");
        charactersOnThisLine = 1; // The last number "fell" to the newest line after the insertion.
      }
      builder.append(character);
      charactersOnThisLine++;
    }
    return builder.toString();
  }

  /**
   * Safely casts a long into an integer.
   *
   * @param l the long that will be converted, should be in the range [Integer.MIN_VALUE, Integer.MAX_VALUE]
   * @return an integer equal to the provided long
   * @throws IllegalArgumentException if the long does not fit into an integer
   */
  public static int safeCastLongToInteger(long l) {
    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
      throw new IllegalArgumentException(l + " does not fit into an integer.");
    } else {
      return (int) l;
    }
  }

  /**
   * Returns the sum of an array of integers.
   *
   * @param integers the array of integers, not null
   * @return the sum
   */
  public static int sum(@NotNull int[] integers) {
    int total = 0;
    for (int integer : integers) {
      total += integer;
    }
    return total;
  }

}
