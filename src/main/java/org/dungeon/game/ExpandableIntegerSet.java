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

package org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * A sorted set of integers that can be expanded from both ends.
 */
class ExpandableIntegerSet implements Serializable {

  private final int MINIMUM_DIFFERENCE;
  private final int DIFFERENCE_BETWEEN_MIN_AND_MAX;

  private final NavigableSet<Integer> set = new TreeSet<Integer>();

  /**
   * Make a new ExpandableIntegerSet.
   *
   * @param minimumDifference the minimum difference between integers, positive
   * @param maximumDifference the maximum difference between integers, bigger than {@code minimumDifference}
   */
  public ExpandableIntegerSet(int minimumDifference, int maximumDifference) {
    if (minimumDifference > 0 && maximumDifference > minimumDifference) {
      this.MINIMUM_DIFFERENCE = minimumDifference;
      this.DIFFERENCE_BETWEEN_MIN_AND_MAX = maximumDifference - minimumDifference;
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
      set.add(Random.nextInteger(MINIMUM_DIFFERENCE));
    }
  }

  /**
   * Expand the set of integers towards an integer a until there is an integer bigger than or equal to a.
   *
   * @return a list with all new integers.
   */
  List<Integer> expand(int a) {
    if (set.isEmpty()) {
      throw new IllegalStateException("the set is empty.");
    }
    ArrayList<Integer> integerList = new ArrayList<Integer>();
    int integer = set.last();
    while (a >= integer) {
      integer += MINIMUM_DIFFERENCE + Random.nextInteger(DIFFERENCE_BETWEEN_MIN_AND_MAX);
      integerList.add(integer);
      set.add(integer);
    }
    integer = set.first();
    while (a <= integer) {
      integer -= MINIMUM_DIFFERENCE + Random.nextInteger(DIFFERENCE_BETWEEN_MIN_AND_MAX);
      integerList.add(integer);
      set.add(integer);
    }
    return integerList;
  }

  /**
   * @return true if {@code a} is in the set.
   */
  boolean contains(int a) {
    return set.contains(a);
  }

  @Override
  public String toString() {
    return "ExpandableIntegerSet currently of " + set;
  }

}
