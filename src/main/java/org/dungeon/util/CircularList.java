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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CircularList class that provides a list with a maximum capacity.
 *
 * <p>After the specified maximum capacity is reached, when a new element is added, the oldest element on the list is
 * removed.
 */
public final class CircularList<T> implements Serializable {

  private final int capacity;
  private final List<T> list;
  /**
   * The index where the element that should be at 0 actually is. Initially 0.
   */
  private int zeroIndex;

  /**
   * Constructs an empty CircularList of the specified capacity.
   */
  public CircularList(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("capacity must be positive.");
    }
    this.capacity = capacity;
    this.list = new ArrayList<T>(capacity);
  }

  /**
   * Add an element to this CircularList.
   *
   * @param element the element to be added
   */
  public void add(T element) {
    if (isFull()) {
      list.set(zeroIndex, element);
      incrementZeroIndex();
    } else {
      list.add(element);
    }
  }

  /**
   * Increments the zeroIndex variable, setting it to 0 if it is equal to the capacity.
   */
  private void incrementZeroIndex() {
    zeroIndex = (zeroIndex + 1) % capacity;
  }

  /**
   * Returns the number of elements in this CircularList.
   *
   * @return the number of elements in this CircularList
   */
  public int size() {
    return list.size();
  }

  /**
   * Returns true if this CircularList is at its maximum capacity. False otherwise.
   *
   * @return true if this CircularList is full
   */
  boolean isFull() {
    return size() == capacity;
  }

  /**
   * Returns true if this CircularList contains no elements.
   *
   * @return true if this CircularList contains no elements
   */
  public boolean isEmpty() {
    return list.isEmpty();
  }

  /**
   * Returns the element at the specified position in this list.
   *
   * @param index index of the element to return
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException if the index is bigger than the size of this CircularList
   */
  public T get(final int index) {
    if (index > size()) {
      throw new IndexOutOfBoundsException();
    }
    return list.get((index + zeroIndex) % capacity);
  }

}
