package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.entity.Entity;
import org.mafagafogigante.dungeon.game.Name;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * General utility class.
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
   * @param left the base string.
   * @param right the prefix.
   * @return true, if the base string starts with the prefix, ignoring case differences.
   */
  public static boolean startsWithIgnoreCase(String left, String right) {
    return left.toLowerCase(Locale.ENGLISH).startsWith(right.toLowerCase(Locale.ENGLISH));
  }

  /**
   * Split a string of text into an array of words.
   */
  public static String[] split(String string) {
    return string.split("\\s+");
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
    CounterMap<Name> nameOccurrences = new CounterMap<>();
    for (Entity entity : listOfEntities) {
      nameOccurrences.incrementCounter(entity.getName());
    }
    ArrayList<String> quantifiedNames = new ArrayList<>();
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
    final List<T> listOfMatches = new ArrayList<>();
    // Do not start with 0, as this would gather all Articles if the query did not match any Article.
    double maximumSimilarity = 1e-6;
    // A flag to indicate whether or not the best match was plural.
    boolean bestIsPlural = false;
    // Attempt at singular matches.
    for (T candidate : collection) {
      final String[] titleWords = split(candidate.getName().getSingular());
      final int matches = countMatches(tokens, titleWords);
      if (!complete || matches >= tokens.length) {
        final double matchesOverTitleWords = matches / (double) titleWords.length;
        final double matchesOverSearchArgs = matches / (double) tokens.length;
        final double similarity = DungeonMath.mean(matchesOverTitleWords, matchesOverSearchArgs);
        final int comparisonResult = DungeonMath.fuzzyCompare(similarity, maximumSimilarity);
        if (comparisonResult > 0) {
          maximumSimilarity = similarity;
          listOfMatches.clear();
          listOfMatches.add(candidate);
        } else if (comparisonResult == 0) {
          listOfMatches.add(candidate);
        }
      }
    }
    // Attempt to match pluralized names.
    for (T candidate : collection) {
      final String[] titleWords = split(candidate.getName().getPlural());
      final int matches = countMatches(tokens, titleWords);
      if (!complete || matches >= tokens.length) {
        final double matchesOverTitleWords = matches / (double) titleWords.length;
        final double matchesOverSearchArgs = matches / (double) tokens.length;
        final double similarity = DungeonMath.mean(matchesOverTitleWords, matchesOverSearchArgs);
        final int comparisonResult = DungeonMath.fuzzyCompare(similarity, maximumSimilarity);
        final boolean isBetterMatch = comparisonResult > 0;
        final boolean isAsGoodMatch = comparisonResult == 0;
        if (isBetterMatch) {
          maximumSimilarity = similarity;
          listOfMatches.clear();
          listOfMatches.add(candidate);
          bestIsPlural = true;
        } else if (bestIsPlural && isAsGoodMatch) {
          listOfMatches.add(candidate);
        }
      }
    }
    return Matches.fromCollection(listOfMatches, !bestIsPlural);
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

  /**
   * Given a duration in milliseconds, this method returns a human-readable period string with up to two fields.
   *
   * @param duration a duration in milliseconds, nonnegative
   * @return a String
   */
  public static String makePeriodString(long duration) {
    if (duration < 0) {
      throw new IllegalArgumentException("duration should be nonnegative.");
    }
    Period period = new Period(duration).normalizedStandard();
    period = withTwoMostSignificantNonZeroFieldsOnly(period);
    return PeriodFormat.wordBased(Locale.ENGLISH).print(period);
  }

  private static Period withTwoMostSignificantNonZeroFieldsOnly(Period period) {
    int nonZeroFieldsFound = 0;
    for (DurationFieldType durationFieldType : period.getFieldTypes()) {
      if (period.get(durationFieldType) != 0) {
        if (nonZeroFieldsFound < 2) {
          nonZeroFieldsFound++;
        } else {
          period = period.withField(durationFieldType, 0);
        }
      }
    }
    return period;
  }

}
