package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.Version;

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

  private static final long serialVersionUID = Version.MAJOR;
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
    this.list = new ArrayList<>(capacity);
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
