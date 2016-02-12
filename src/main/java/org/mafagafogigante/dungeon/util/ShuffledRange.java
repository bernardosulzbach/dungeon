package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * ShuffledRange class that provides a List of shuffled non-repeating integers from a lower bound up to an upper bound.
 */
public final class ShuffledRange {

  private ArrayList<Integer> integers;

  /**
   * Constructs a new {@code ShuffledRange} given a lower bound and an upper bound.
   *
   * <p>This constructor populates a {@code List} with the integers {@code {start, start + 1, ..., end - 1}} and calls
   * {@code shuffle}.
   *
   * @param start the lower bound (inclusive)
   * @param end the higher bound (exclusive)
   */
  public ShuffledRange(int start, int end) {
    if (start >= end) {
      DungeonLogger.warning("Tried to create a ShuffledRange of negative or zero length.");
    } else {
      integers = new ArrayList<>(end - start);
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
   * Shuffles the underlying List.
   *
   * <p>If the List has more than one element, ensures that the next first integer is not the integer currently at the
   * end.
   */
  public void shuffle() {
    int lastIntegerBeforeShuffling = integers.get(integers.size() - 1);
    Collections.shuffle(integers);
    if (getSize() > 1 && get(0) == lastIntegerBeforeShuffling) {
      // Swap a random integer that is not the first into the first position.
      Collections.swap(integers, 0, 1 + Random.nextInteger(getSize() - 1));
    }
  }

}
