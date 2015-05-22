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

import org.dungeon.game.GameData;
import org.dungeon.game.Selectable;
import org.dungeon.io.IO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * General utility class. All utility methods of the game should be placed in this class.
 *
 * @author Bernardo Sulzbach
 */
public final class Utils {

  private Utils() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

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

  /**
   * Enumerates the elements of a List in a human-readable way.
   * <p/>
   * This method calls {@code toString()} on each object, so the result depends on what that method returns.
   *
   * @param list the List of Objects.
   * @return a String.
   */
  public static String enumerate(final List list) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append(list.get(i).toString());
      if (i < list.size() - 2) {
        stringBuilder.append(", ");
      } else if (i == list.size() - 2) {
        if (list.size() >= 3) {
          // A serial comma (only used when we have three or more items).
          stringBuilder.append(",");
        }
        stringBuilder.append(" and ");
      }
    }
    return stringBuilder.toString();
  }

  /**
   * Finds the best matches to the provided tokens among the {@code Selectable}s of a specified {@code Collection}.
   *
   * @param collection a {@code Collection} of {@code Selectable} objects
   * @param tokens     the search Strings
   * @param <T>        a type T that extends {@code Selectable}
   * @return a {@code Matches} object with zero or more elements of type T
   */
  public static <T extends Selectable> Matches<T> findBestMatches(Collection<T> collection, String... tokens) {
    return findMatches(collection, false, tokens);
  }

  /**
   * Finds the best complete matches to the provided tokens among the {@code Selectable}s of a specified
   * {@code Collection}. A match is considered complete if it has a word for each provided token.
   * <p/>
   * This is the method that should be used to select objects of the class {@code Entity}, as, for instance,
   * {@code "Fruit Bat"} should never match a {@code "Bat"}.
   *
   * @param collection a {@code Collection} of {@code Selectable} objects
   * @param tokens     the search Strings
   * @param <T>        a type T that extends {@code Selectable}
   * @return a {@code Matches} object with zero or more elements of type T
   */
  public static <T extends Selectable> Matches<T> findBestCompleteMatches(Collection<T> collection, String... tokens) {
    return findMatches(collection, true, tokens);
  }

  /**
   * Finds matches of {@code Selectable}s based on a given {@code Collection} of objects and an array of search tokens.
   *
   * @param collection a {@code Collection} of {@code Selectable} objects
   * @param complete   if true, only elements that match all tokens are returned
   * @param tokens     the search Strings
   * @param <T>        a type T that extends {@code Selectable}
   * @return a {@code Matches} object with zero or more elements of type T
   */
  public static <T extends Selectable> Matches<T> findMatches(Collection<T> collection, boolean complete,
      String... tokens) {
    List<T> listOfMatches = new ArrayList<T>();
    // Do not start with 0, as this would gather all Articles if the query did not match any Article.
    double maximumSimilarity = 1e-6;
    for (T candidate : collection) {
      String[] titleWords = split(candidate.getName().getSingular());
      int matches = countMatches(tokens, titleWords);
      if (!complete || matches == tokens.length) {
        double matchesOverTitleWords = matches / (double) titleWords.length;
        double matchesOverSearchArgs = matches / (double) tokens.length;
        double similarity = Math.mean(matchesOverTitleWords, matchesOverSearchArgs);
        int comparisonResult = Math.fuzzyCompare(similarity, maximumSimilarity);
        if (comparisonResult > 0) {
          maximumSimilarity = similarity;
          listOfMatches.clear();
          listOfMatches.add(candidate);
        } else if (comparisonResult == 0) {
          listOfMatches.add(candidate);
        }
      }
    }
    return Matches.fromCollection(listOfMatches);
  }

  /**
   * Counts how many Strings in the entry array start with the Strings of the query array.
   */
  private static int countMatches(String[] query, String[] entry) {
    int matches = 0;
    int indexOfLastMatchPlusOne = 0;
    for (int i = 0; i < query.length && indexOfLastMatchPlusOne < entry.length; i++) {
      for (int j = indexOfLastMatchPlusOne; j < entry.length; j++) {
        if (startsWithIgnoreCase(entry[j], query[i])) {
          indexOfLastMatchPlusOne = j + 1;
          matches++;
        }
      }
    }
    return matches;
  }

}
