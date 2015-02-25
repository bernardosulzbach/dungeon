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

import org.dungeon.game.IssuedCommand;
import org.dungeon.io.IO;

import java.awt.Color;
import java.math.BigInteger;

/**
 * A collection of mathematical utility methods.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public class Math {

  /**
   * The maximum allowed value for the fibonacci command.
   */
  private static final int FIBONACCI_MAX = 65535;
  private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-8;

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
   * The public method that should be invoked using the input words.
   *
   * @param issuedCommand the command entered by the player.
   */
  public static void fibonacci(IssuedCommand issuedCommand) {
    int intArgument;
    if (issuedCommand.hasArguments()) {
      for (String strArgument : issuedCommand.getArguments()) {
        try {
          intArgument = Integer.parseInt(strArgument);
        } catch (NumberFormatException warn) {
          Messenger.printInvalidNumberFormatOrValue();
          return;
        }
        if (0 < intArgument && intArgument <= FIBONACCI_MAX) {
          char[] result = fibonacci(intArgument).toCharArray();
          StringBuilder sb = new StringBuilder(result.length + 64);
          sb.append("fibonacci").append('(').append(intArgument).append(')').append(" = ");
          int newLineCharacters = 0;
          for (char character : result) {
            if ((sb.length() + 1 - newLineCharacters) % Constants.COLS == 0) {
              sb.append("\\\n");
              newLineCharacters++;
            } else {
              sb.append(character);
            }
          }
          IO.writeString(sb.toString());
        } else {
          IO.writeString("n must be positive and smaller than " + (FIBONACCI_MAX + 1) + ".", Color.ORANGE);
        }
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  /**
   * Returns the n-th element of the fibonacci sequence.
   *
   * @param n the position of the element on the sequence. Must be positive.
   */
  private static String fibonacci(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("n must be positive.");
    }
    BigInteger a = BigInteger.ZERO;
    BigInteger b = BigInteger.ONE;
    // Swap variable.
    BigInteger s;
    for (int i = 1; i < n; i++) {
      s = a;
      a = b;
      b = b.add(s);
    }
    return a.toString();
  }

}
