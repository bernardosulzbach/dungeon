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

/**
 * An automated ShuffledRange.
 * <p/>
 * Objects of this class wrap a {@code ShuffledRange} and allow infinitely many calls to {@code getNext}.
 * When the end of the underlying {@code ShuffledRange} is reached, the index is reset and {@code shuffle} is called on
 * the {@code ShuffledRange}.
 * <p/>
 * Created by Bernardo on 19/01/2015.
 */
public class AutomaticShuffledRange {

  ShuffledRange shuffledRange;
  private int index;

  /**
   * Constructs a new {@code AutomaticShuffledRange} given a lower bound and an upper bound.
   *
   * @param start the lower bound (inclusive)
   * @param end   the higher bound (exclusive)
   */
  public AutomaticShuffledRange(int start, int end) {
    shuffledRange = new ShuffledRange(start, end);
  }

  /**
   * Returns the next integer in the {@code ShuffledRange}.
   *
   * @return the next integer in the {@code ShuffledRange}
   */
  public int getNext() {
    int value = shuffledRange.get(index);
    index++;
    if (index == shuffledRange.getSize()) {
      index = 0;
      shuffledRange.shuffle();
    }
    return value;
  }

}
