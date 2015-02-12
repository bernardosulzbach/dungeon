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

import java.util.ArrayList;

/**
 * ShuffledRange class that provides a List of shuffled non-repeating integers from a lower bound up to an upper bound.
 */
public class ShuffledRange {

  private ArrayList<Integer> integers;

  /**
   * Constructs a new {@code ShuffledRange} given a lower bound and an upper bound.
   * <p/>
   * This constructor populates a {@code List} with the integers {@code {start, start + 1, ..., end}} and calls
   * {@code shuffle}.
   *
   * @param start the lower bound (inclusive)
   * @param end   the higher bound (exclusive)
   */
  public ShuffledRange(int start, int end) {
    integers = new ArrayList<Integer>(end - start - 1);
    for (int i = start; i < end; i++) {
      integers.add(i);
    }
    shuffle();
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
   * Shuffles the underlying List.
   * <p/>
   * If the List has more than one element, ensures that the next first integer is not the integer currently at the end.
   */
  public void shuffle() {
    if (integers.size() > 1) {
      ArrayList<Integer> old = new ArrayList<Integer>(integers);
      integers.clear();
      integers.add(old.remove(Engine.RANDOM.nextInt(old.size() - 1)));
      for (int i = old.size(); i > 0; i--) {
        integers.add(old.remove(Engine.RANDOM.nextInt(i)));
      }
    }
  }

}
