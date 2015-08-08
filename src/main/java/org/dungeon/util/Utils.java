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

import org.dungeon.entity.Entity;
import org.dungeon.game.GameData;
import org.dungeon.game.Name;
import org.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * General utility class. All utility methods of the game should be placed in this class.
 */
public final class Utils {

  private Utils() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Pads a string with spaces at the end in order to reach a desired length. If the provided string's length is bigger
   * than the desired length, the same string is returned.
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
   * Split a string of text into an array of words.
   */
  public static String[] split(String string) {
    return string.split("\\s+");
  }

  /**
   * Joins a sequence of strings with a specified delimiter string.
   *
   * @param delimiter the delimiter string.
   * @param elements the sequence of strings to be joined.
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
    Writer.writeString(GameData.LICENSE);
  }

  /**
   * Enumerates the elements of a Collection in a human-readable way.
   *
   * <p>This method calls {@code toString()} on each object, so the result depends on what that method returns.
   *
   * @param collection a Collection
   * @return a String
   */
  public static String enumerate(@NotNull final Collection<?> collection) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<?> iterator = collection.iterator();
    for (int i = 0; i < collection.size(); i++) {
      stringBuilder.append(iterator.next().toString());
      if (i < collection.size() - 2) {
        stringBuilder.append(", ");
      } else if (i == collection.size() - 2) {
        if (collection.size() >= 3) {
          // A serial comma (only used when we have three or more items).
          stringBuilder.append(",");
        }
        stringBuilder.append(" and ");
      }
    }
    return stringBuilder.toString();
  }

  /**
   * Returns a String representation of the enumeration of all the Entities in a given List.
   */
  public static String enumerateEntities(final List<? extends Entity> listOfEntities) {
    CounterMap<Name> nameOccurrences = new CounterMap<Name>();
    for (Entity entity : listOfEntities) {
      nameOccurrences.incrementCounter(entity.getName());
    }
    ArrayList<String> quantifiedNames = new ArrayList<String>();
    for (Name name : nameOccurrences.keySet()) {
      quantifiedNames.add(name.getQuantifiedName(nameOccurrences.getCounter(name)));
    }
    return enumerate(quantifiedNames);
  }

  /**
   * Finds the best matches to the provided tokens among the {@code Selectable}s of a specified {@code Collection}.
   *
   * @param collection a {@code Collection} of {@code Selectable} objects
   * @param tokens the search Strings
   * @param <T> a type T that extends {@code Selectable}
   * @return a {@code Matches} object with zero or more elements of type T
   */
  public static <T extends Selectable> Matches<T> findBestMatches(Collection<T> collection, String... tokens) {
    return findMatches(collection, false, tokens);
  }

  /**
   * Finds the best complete matches to the provided tokens among the {@code Selectable}s of a specified {@code
   * Collection}. A match is considered complete if it has a word for each provided token.
   *
   * <p>This is the method that should be used to select objects of the class {@code Entity}, as, for instance, {@code
   * "Fruit Bat"} should never match a {@code "Bat"}.
   *
   * @param collection a {@code Collection} of {@code Selectable} objects
   * @param tokens the search Strings
   * @param <T> a type T that extends {@code Selectable}
   * @return a {@code Matches} object with zero or more elements of type T
   */
  public static <T extends Selectable> Matches<T> findBestCompleteMatches(Collection<T> collection, String... tokens) {
    return findMatches(collection, true, tokens);
  }

  /**
   * Finds matches of {@code Selectable}s based on a given {@code Collection} of objects and an array of search tokens.
   *
   * @param collection a {@code Collection} of {@code Selectable} objects
   * @param complete if true, only elements that match all tokens are returned
   * @param tokens the search Strings
   * @param <T> a type T that extends {@code Selectable}
   * @return a {@code Matches} object with zero or more elements of type T
   */
  private static <T extends Selectable> Matches<T> findMatches(Collection<T> collection, boolean complete,
      String... tokens) {
    List<T> listOfMatches = new ArrayList<T>();
    // Do not start with 0, as this would gather all Articles if the query did not match any Article.
    double maximumSimilarity = 1e-6;
    for (T candidate : collection) {
      String[] titleWords = split(candidate.getName().getSingular());
      int matches = countMatches(tokens, titleWords);
      if (!complete || matches >= tokens.length) {
        double matchesOverTitleWords = matches / (double) titleWords.length;
        double matchesOverSearchArgs = matches / (double) tokens.length;
        double similarity = DungeonMath.mean(matchesOverTitleWords, matchesOverSearchArgs);
        int comparisonResult = DungeonMath.fuzzyCompare(similarity, maximumSimilarity);
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
