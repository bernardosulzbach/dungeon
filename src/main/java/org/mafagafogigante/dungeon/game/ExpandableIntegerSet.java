package org.mafagafogigante.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * A sorted set of integers that can be expanded from both ends.
 */
class ExpandableIntegerSet implements Serializable {

  private final int minimumDifference;
  private final int differenceBetweenMinAndMax;

  private final NavigableSet<Integer> set = new TreeSet<>();

  /**
   * Make a new ExpandableIntegerSet.
   *
   * @param minimumDifference the minimum difference between integers, positive
   * @param maximumDifference the maximum difference between integers, bigger than {@code minimumDifference}
   */
  public ExpandableIntegerSet(int minimumDifference, int maximumDifference) {
    if (minimumDifference > 0 && maximumDifference > minimumDifference) {
      this.minimumDifference = minimumDifference;
      this.differenceBetweenMinAndMax = maximumDifference - minimumDifference;
    } else {
      String message = "illegal values for minimumDifference or maximumDifference";
      throw new IllegalArgumentException(message);
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
      integer += minimumDifference + Random.nextInteger(differenceBetweenMinAndMax);
      integerList.add(integer);
      set.add(integer);
    }
    integer = set.first();
    while (value <= integer) {
      integer -= minimumDifference + Random.nextInteger(differenceBetweenMinAndMax);
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
