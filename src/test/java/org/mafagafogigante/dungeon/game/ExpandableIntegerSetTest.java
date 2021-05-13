package org.mafagafogigante.dungeon.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExpandableIntegerSetTest {

  @Test
  public void testConstructorThrowsIllegalArgumentExceptionIfMinimumDifferenceIsNegative() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new ExpandableIntegerSet(-1, 1);
    });
  }

  @Test
  public void testConstructorThrowsIllegalArgumentExceptionIfMinimumDifferenceIsZero() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new ExpandableIntegerSet(0, 1);
    });
  }

  @Test
  public void testConstructorThrowsIllegalArgumentExceptionOnMaximumDifferenceNotBiggerThanMinimumDifference() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new ExpandableIntegerSet(1, 1);
    });
  }

}
