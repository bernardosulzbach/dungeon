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
import org.dungeon.io.DLogger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * ShuffledRange class that provides a List of shuffled non-repeating integers from a lower bound up to an upper bound.
 */
public class ShuffledRange {

  private ArrayList<Integer> integers;

  /**
   * Constructs a new {@code ShuffledRange} given a lower bound and an upper bound.
   * <p/>
   * This constructor populates a {@code List} with the integers {@code {start, start + 1, ..., end - 1}} and calls
   * {@code shuffle}.
   *
   * @param start the lower bound (inclusive)
   * @param end   the higher bound (exclusive)
   */
  public ShuffledRange(int start, int end) {
    if (start >= end) {
      DLogger.warning("Tried to create a ShuffledRange of negative or zero length.");
    } else {
      integers = new ArrayList<Integer>(end - start);
      for (int i = start; i < end; i++) {
        integers.add(i);
      }
      shuffle();
    }
  }

  /**
   * Returns the element at the specified position.
   *
   * @param index the index of the element
   * @return the element at the specified position
   */
  public int get(int index) {
    return integers.get(index);
  }

  /**
   * Returns the number of integers in this range.
   *
   * @return the number of integers in this range
   */
  public int getSize() {
    return integers.size();
  }

  /**
   * Returns the last integer of the range according to its current ordering.
   */
  public int getLast() {
    return integers.get(integers.size() - 1);
  }

  /**
   * Shuffles the underlying List.
   * <p/>
   * If the List has more than one element, ensures that the next first integer is not the integer currently at the end.
   */
  public void shuffle() {
    int lastInteger = getLast();
    Collections.shuffle(integers);
    if (getSize() > 1 && get(0) == lastInteger) {
      // Swap a random integer that is not the first into the first position.
      Collections.swap(integers, 0, 1 + Engine.RANDOM.nextInt(getSize() - 1));
    }
  }

}
