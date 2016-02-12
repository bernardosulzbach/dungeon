package org.mafagafogigante.dungeon.util;

/**
 * Array utilities.
 */
final class ArrayUtils {

  private ArrayUtils() {
    throw new AssertionError();
  }

  /**
   * Finds the first occurrence of a value in an array. Returns the length of the array if the element could not be
   * found.
   */
  public static <T> int findFirstOccurrence(T[] array, T element) {
    int index = 0;
    for (T token : array) {
      if (token.equals(element)) {
        return index;
      }
      index++;
    }
    return index;
  }

}
