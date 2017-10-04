package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.BaseName;
import org.mafagafogigante.dungeon.game.Name;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * A collection of Selectable objects that listMatches a given query.
 */
public class Matches<T extends Selectable> {

  private final List<T> matches = new ArrayList<>();
  private final boolean disjoint;
  private int differentNames = 0;
  private boolean differentNamesUpToDate = true;

  private Matches(boolean disjoint) {
    this.disjoint = disjoint;
  }

  /**
   * Converts a Collection to Matches.
   */
  public static <T extends Selectable> Matches<T> fromCollection(Collection<T> collection) {
    return fromCollection(collection, true);
  }

  /**
   * Converts a Collection to possibly not disjoint Matches.
   */
  private static <T extends Selectable> Matches<T> fromCollection(Collection<T> collection, boolean disjoint) {
    Matches<T> newInstance = new Matches<>(disjoint);
    for (T t : collection) {
      newInstance.addMatch(t);
    }
    return newInstance;
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
    return findMatches(collection, tokens, false);
  }

  /**
   * Finds the best complete matches to the provided tokens among the {@code Selectable}s of a specified {@code
   * Collection}. A listMatches is considered complete if it has a word for each provided token.
   *
   * <p>This is the method that should be used to select objects of the class {@code Entity}, as, for instance, {@code
   * "Fruit Bat"} should never listMatches a {@code "Bat"}.
   *
   * @param <T> a type T that extends {@code Selectable}
   * @param elements a {@code Collection} of {@code Selectable} elements
   * @param tokens the search Strings
   * @return a {@code Matches} object with zero or more elements of type T
   */
  public static <T extends Selectable> Matches<T> findBestCompleteMatches(Collection<T> elements, String... tokens) {
    return findMatches(elements, tokens, true);
  }

  private static double calculateSimilarity(String[] nameWords, String[] tokens, boolean full) {
    if (!full || nameWords.length >= tokens.length) {
      int matches = countMatches(tokens, nameWords);
      SummaryStatistics statistics = new SummaryStatistics();
      statistics.addValue(matches / (double) nameWords.length);
      statistics.addValue(matches / (double) tokens.length);
      return statistics.getMean();
    } else {
      return 0.0;
    }
  }

  private static double calculateSingularSimilarity(Name name, String[] tokens, boolean full) {
    return calculateSimilarity(StringUtils.split(name.getSingular()), tokens, full);
  }

  private static double calculatePluralSimilarity(Name name, String[] tokens, boolean full) {
    return calculateSimilarity(StringUtils.split(name.getPlural()), tokens, full);
  }

  private static MatchResult evaluateMatch(Name name, int frequency, String[] tokens, boolean full) {
    double singularSimilarity = calculateSingularSimilarity(name, tokens, full);
    if (frequency < 2) {
      return new MatchResult(singularSimilarity, false);
    }
    double pluralSimilarity = calculatePluralSimilarity(name, tokens, full);
    if (DungeonMath.fuzzyCompare(singularSimilarity, pluralSimilarity) >= 0) {
      // Disjoint matches are preferred.
      return new MatchResult(singularSimilarity, true);
    } else {
      return new MatchResult(pluralSimilarity, false);
    }
  }

  /**
   * Finds matches of {@code Selectable}s based on a given {@code Collection} of objects and an array of search tokens.
   *
   * @param <T> a type T that extends {@code Selectable}
   * @param elements a {@code Collection} of {@code Selectable} elements
   * @param tokens the search Strings
   * @param full if true, only elements that match all tokens are returned
   * @return a {@code Matches} object with zero or more elements of type T
   */
  private static <T extends Selectable> Matches<T> findMatches(Collection<T> elements, String[] tokens, boolean full) {
    List<T> list = new ArrayList<>();
    double maximumSimilarity = 0.0;
    boolean disjoint = true;
    CounterMap<Name> nameCounters = new CounterMap<>();
    for (T element : elements) {
      nameCounters.incrementCounter(element.getName());
    }
    for (T element : elements) {
      Name name = element.getName();
      MatchResult result = evaluateMatch(name, nameCounters.getCounter(name), tokens, full);
      if (DungeonMath.fuzzyCompare(result.similarity, 0.0) > 0) {
        boolean isBetter = DungeonMath.fuzzyCompare(result.similarity, maximumSimilarity) > 0;
        boolean isAsGood = DungeonMath.fuzzyCompare(result.similarity, maximumSimilarity) == 0;
        if (isBetter) {
          maximumSimilarity = result.similarity;
          disjoint = result.disjoint;
          list.clear();
          list.add(element);
        } else if (isAsGood) {
          if (disjoint == result.disjoint) {
            list.add(element);
          } else if (result.disjoint) {
            // Disjoint matches are preferred.
            // Discard the current non-disjoint matches in favor of the disjoint ones.
            disjoint = true;
            list.clear();
            list.add(element);
          }
        }
      }
    }
    return fromCollection(list, disjoint);
  }

  /**
   * Counts how many Strings in the entry array start with the Strings of the query array.
   */
  private static int countMatches(String[] query, String[] entry) {
    int matches = 0;
    int indexOfLastMatchPlusOne = 0;
    for (int i = 0; i < query.length && indexOfLastMatchPlusOne < entry.length; i++) {
      for (int j = indexOfLastMatchPlusOne; j < entry.length; j++) {
        if (StringUtils.startsWithIgnoreCase(entry[j], query[i])) {
          indexOfLastMatchPlusOne = j + 1;
          matches++;
        }
      }
    }
    return matches;
  }

  /**
   * Returns whether or not the Matches are disjoint.
   *
   * <p>If they are, any element constitutes a listMatches, otherwise, only all elements together constitute a
   * listMatches.
   */
  public boolean isDisjoint() {
    return disjoint;
  }

  private void addMatch(T match) {
    matches.add(match);
    differentNamesUpToDate = false;
  }

  public T getMatch(int index) {
    return matches.get(index);
  }

  public List<T> toList() {
    return new ArrayList<>(matches);
  }

  /**
   * Returns true if there is a listMatches with the given name, false otherwise.
   *
   * @param name the name used for comparison
   * @return true if there is a listMatches with the given name, false otherwise
   */
  public boolean hasMatchWithName(Name name) {
    for (T match : matches) {
      if (match.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the number of matches.
   */
  public int size() {
    return matches.size();
  }

  /**
   * Returns how many different names the matches have. For instance, if the matches consist of two Entity objects with
   * identical names, this method will return 1.
   *
   * <p>This method will calculate how many different names are in the list of matches or use the last calculated value,
   * if the matches list did not change since the last calculation. Therefore, after adding all matches and calling this
   * method once, subsequent method calls should be substantially faster.
   */
  public int getDifferentNames() {
    if (!differentNamesUpToDate) {
      updateDifferentNamesCount();
    }
    return differentNames;
  }

  /**
   * Updates the differentNames variable after iterating over the list of matches.
   */
  private void updateDifferentNamesCount() {
    HashSet<Name> uniqueNames = new HashSet<>();
    for (T match : matches) {
      uniqueNames.add(match.getName());
    }
    differentNames = uniqueNames.size();
    differentNamesUpToDate = true;
  }

  private static class MatchResult {
    private final double similarity;
    private final boolean disjoint;

    private MatchResult(double similarity, boolean disjoint) {
      this.similarity = similarity;
      this.disjoint = disjoint;
    }
  }

}
