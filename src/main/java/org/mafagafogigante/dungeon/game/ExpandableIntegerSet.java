package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * A sorted set of integers that can be expanded from both ends.
 */
class ExpandableIntegerSet implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final int minimumDifference;
  private final int maximumDifference;

  private final NavigableSet<Integer> set = new TreeSet<>();

  /**
   * Make a new ExpandableIntegerSet.
   *
   * @param minimumDifference the minimum difference between integers, positive
   * @param maximumDifference the maximum difference between integers, bigger than {@code minimumDifference}
   */
  public ExpandableIntegerSet(int minimumDifference, int maximumDifference) {
    if (minimumDifference < 1) {
      throw new IllegalArgumentException("minimumDifference must be positive");
    } else if (minimumDifference >= maximumDifference) {
      throw new IllegalArgumentException("maximumDifference must be bigger than minimumDifference");
    } else {
      this.minimumDifference = minimumDifference;
      this.maximumDifference = maximumDifference;
    }
    initialize();
  }

  /**
   * Generate the first integer of the set. This method should not be invoked twice.
   */
  private void initialize() {
    if (!set.isEmpty()) {
      throw new IllegalStateException("set already has an element.");
    } else {
      set.add(Random.nextInteger(minimumDifference));
    }
  }

  /**
   * Expand the set of integers towards an integer a until there is an integer bigger than or equal to value.
   *
   * @return a list with all new integers.
   */
  List<Integer> expand(int value) {
    if (set.isEmpty()) {
      throw new IllegalStateException("the set is empty.");
    }
    ArrayList<Integer> integerList = new ArrayList<>();
    int integer = set.last();
    while (value >= integer) {
      integer += Random.nextInteger(minimumDifference, maximumDifference);
      integerList.add(integer);
      set.add(integer);
    }
    integer = set.first();
    while (value <= integer) {
      integer -= Random.nextInteger(minimumDifference, maximumDifference);
      integerList.add(integer);
      set.add(integer);
    }
    return integerList;
  }

  /**
   * @return true if {@code value} is in the set.
   */
  boolean contains(int value) {
    return set.contains(value);
  }

  @Override
  public String toString() {
    return "ExpandableIntegerSet currently of " + set;
  }

}
