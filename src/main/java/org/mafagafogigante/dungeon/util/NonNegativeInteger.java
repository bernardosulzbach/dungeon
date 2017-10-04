package org.mafagafogigante.dungeon.util;

import org.jetbrains.annotations.NotNull;

/**
 * An Integer between 0 and 2 ^ 31 - 1, inclusive.
 */
public class NonNegativeInteger {

  private final Integer integer;

  /**
   * Constructs a NonNegativeInteger from an integer. The integer must be nonnegative.
   */
  public NonNegativeInteger(@NotNull Integer integer) {
    if (integer < 0) {
      throw new IllegalArgumentException("integer must be nonnegative.");
    }
    this.integer = integer;
  }

  public Integer toInteger() {
    return integer;
  }

  @Override
  public String toString() {
    return integer.toString();
  }

}
