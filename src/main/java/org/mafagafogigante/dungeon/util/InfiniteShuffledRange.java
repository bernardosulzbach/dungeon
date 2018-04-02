package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * Objects of this class wrap a {@code ShuffledRange} and allow infinitely many calls to {@code getNext}. When the
 * end of the underlying {@code ShuffledRange} is reached, the index is reset and {@code shuffle} is called on the
 * {@code ShuffledRange}.
 */
class InfiniteShuffledRange implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final ShuffledRange shuffledRange;
  private int index;

  /**
   * Constructs a new {@code InfiniteShuffledRange} of lower bound 0 given an upper bound.
   *
   * @param end the higher bound (exclusive)
   */
  InfiniteShuffledRange(int end) {
    shuffledRange = new ShuffledRange(0, end);
  }

  /**
   * Returns the next integer in the {@code ShuffledRange}.
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
