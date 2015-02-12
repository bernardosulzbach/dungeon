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

import org.dungeon.game.Engine;
import org.dungeon.game.GameData;
import org.dungeon.game.Selectable;
import org.dungeon.io.IO;

import java.util.List;

/**
 * General utility class. All utility methods of the game should be placed in this class.
 *
 * @author Bernardo Sulzbach
 */
public class Utils {

  /**
   * Pads a string with spaces at the end in order to reach a desired length. If the provided string's length is
   * bigger than the desired length, the same string is returned.
   */
  public static String padString(String original, int desiredLength) {
    int requiredSpaces = desiredLength - original.length();
    if (requiredSpaces > 0) {
      StringBuilder stringBuilder = new StringBuilder(desiredLength);
      stringBuilder.append(original);
      for (int i = 0; i < requiredSpaces; i++) {
        stringBuilder.append(' ');
      }
      return stringBuilder.toString();
    } else {
      return original;
    }
  }

  /**
   * Parses a list of Selectable candidates trying to find a match based on a query string.
   * <p/>
   * This method is not case-sensitive.
   * <p/>
   * Parts of individual tokens will result in a match if the parts order matches the order of the candidate's name.
   * <p/>
   * (e. g.: "g b r" will match "Giant Black Rat" as "W Watch" will match "Wrist Watch")
   *
   * @param candidates a list of Selectable candidates.
   * @param tokens     the text tokens.
   * @return a SelectionResult object with all the matches found.
   */
  public static <T extends Selectable> SelectionResult<T> selectFromList(List<T> candidates, String[] tokens) {
    SelectionResult<T> selectionResult = new SelectionResult<T>();
    for (T candidate : candidates) {
      if (checkQueryMatch(tokens, split(candidate.getName()))) {
        selectionResult.addMatch(candidate);
      }
    }
    return selectionResult;
  }

  /**
   * Checks if two string arrays match. Not case-sensitive. Only checks for the same starting characters.
   * <p/>
   * "g", "g b", "g r" and "b r" match "Giant Black Rat". "g r b", "b g r", "b r g", "r b g", "r g b" do not.
   *
   * @param query     the query array.
   * @param candidate the candidate array.
   * @return a boolean indicating if the tokens match or not.
   */
  public static boolean checkQueryMatch(String[] query, String[] candidate) {
    if (query.length > candidate.length) {
      return false;
    }
    boolean foundMatch = false;
    int indexOfLastMatchInCandidate = 0;
    for (String queryToken : query) {
      // TODO: reword this. Too messy.
      while (indexOfLastMatchInCandidate < candidate.length && !foundMatch) {
        if (startsWithIgnoreCase(candidate[indexOfLastMatchInCandidate], queryToken)) {
          foundMatch = true;
        }
        indexOfLastMatchInCandidate++;
      }
      if (!foundMatch) {
        return false;
      } else {
        foundMatch = false;
      }
    }
    return true;
  }

  /**
   * Simulates a random roll.
   *
   * @param chance the probability of a true result. Must be nonnegative and smaller than or equal to 1.
   *               Will be passed to Percentage's constructors in order to guarantee that it is a valid value.
   * @return a boolean indicating if the roll was successful or not.
   */
  public static boolean roll(double chance) {
    chance = (new Percentage(chance)).toDouble();
    return chance > Engine.RANDOM.nextDouble();
  }

  /**
   * Checks if a string starts with a given string, ignoring case differences.
   *
   * @param a the base string.
   * @param b the prefix.
   * @return true, if the base string starts with the prefix, ignoring case differences.
   */
  public static boolean startsWithIgnoreCase(String a, String b) {
    return a.toLowerCase().startsWith(b.toLowerCase());
  }

  /**
   * Creates a string of repeated characters.
   */
  public static String makeRepeatedCharacterString(int repetitions, char character) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < repetitions; i++) {
      builder.append(character);
    }
    return builder.toString();
  }

  /**
   * Split a string of text into an array of words.
   */
  public static String[] split(String string) {
    return string.split("\\s+");
  }

  /**
   * Strips all newlines and spaces at the end of the string.
   */
  public static String clearEnd(String str) {
    StringBuilder stringBuilder = new StringBuilder(str);
    int length = stringBuilder.length();
    char lastChar;
    while (length > 0) {
      lastChar = stringBuilder.charAt(length - 1);
      if (Character.isSpaceChar(lastChar) || lastChar == '\n') {
        stringBuilder.setLength(stringBuilder.length() - 1);
      } else {
        break;
      }
      length = stringBuilder.length();
    }
    return stringBuilder.toString();
  }

  /**
   * Joins a sequence of strings with a specified delimiter string.
   *
   * @param delimiter the delimiter string.
   * @param elements  the sequence of strings to be joined.
   * @return a single String.
   */
  public static String join(String delimiter, String... elements) {
    if (elements.length == 0) {
      throw new IllegalArgumentException("elements must have at least one element.");
    }
    // Assume that the average length is equal to the length of the first element.
    StringBuilder sb = new StringBuilder(elements.length * (delimiter.length() + elements[0].length()) + 16);
    sb.append(elements[0]);
    for (int i = 1; i < elements.length; i++) {
      sb.append(delimiter).append(elements[i]);
    }
    return sb.toString();
  }

  /**
   * Converts a given number of bytes to a human readable format.
   *
   * @return a String.
   */
  public static String bytesToHuman(long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    }
    // 2 ^ 10 (1 kB) has (63 - 10) = 53 leading zeros.
    // 2 ^ 20 (1 MB) has (63 - 20) = 43 leading zeros.
    // And so forth.
    // Bits used to represent the number of bytes = number of bits available - number of leading zeros.
    int bitsUsed = 63 - Long.numberOfLeadingZeros(bytes);
    // (1L << (bitsUsed - bitsUsed % 10)) shifts the one (in binary) to the left by a multiple of 10.
    // This is a fast way to get the power of 1024 by which we must divide the number of bytes.
    double significand = (double) bytes / (1L << (bitsUsed - bitsUsed % 10));
    // By dividing the number of bits used by 10, get the prefix that should be used.
    // Subtract one as Strings are zero indexed.
    char prefix = "kMGTPE".charAt(bitsUsed / 10 - 1);
    return String.format("%.1f %sB", significand, prefix);
  }

  /**
   * Prints the game's license.
   */
  public static void printLicense() {
    IO.writeString(GameData.LICENSE);
  }

  /**
   * Converts an array of Strings into a single String object, separating Strings with the specified separator.
   *
   * @param strings   the Strings.
   * @param separator a String to be inserted between Strings of the array.
   * @return a single String.
   */
  public static String stringArrayToString(String[] strings, String separator) {
    if (strings.length == 0) {
      return "";
    } else if (strings.length == 1) {
      return strings[0];
    } else {
      StringBuilder builder = new StringBuilder(strings[0]);
      for (int index = 1; index < strings.length; index++) {
        builder.append(separator);
        builder.append(strings[index]);
      }
      return builder.toString();
    }
  }

}
