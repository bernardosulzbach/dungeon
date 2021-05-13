package org.mafagafogigante.dungeon.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DungeonMathTest {

  @Test
  public void weightedAverageShouldWorkAsExpected() throws Exception {
    Assertions.assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(0.0, 1.0, new Percentage(0.0))));
    Assertions.assertEquals(0, Double.compare(1.0, DungeonMath.weightedAverage(1.0, 0.0, new Percentage(0.0))));

    Assertions.assertEquals(0, Double.compare(1.0, DungeonMath.weightedAverage(0.0, 1.0, new Percentage(1.0))));
    Assertions.assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(1.0, 0.0, new Percentage(1.0))));

    Assertions.assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.0))));
    Assertions.assertEquals(0, Double.compare(0.1, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.2))));
    Assertions.assertEquals(0, Double.compare(0.2, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.4))));
    Assertions.assertEquals(0, Double.compare(0.3, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.6))));
    Assertions.assertEquals(0, Double.compare(0.4, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.8))));
    Assertions.assertEquals(0, Double.compare(0.5, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(1.0))));
  }

  @Test
  public void safeCastLongToIntegerShouldNotChangeValue() throws Exception {
    Assertions.assertEquals(-1, DungeonMath.safeCastLongToInteger(-1L));
    Assertions.assertEquals(0, DungeonMath.safeCastLongToInteger(0L));
    Assertions.assertEquals(1, DungeonMath.safeCastLongToInteger(1L));
    Assertions.assertEquals(Integer.MIN_VALUE, DungeonMath.safeCastLongToInteger(Integer.MIN_VALUE));
    Assertions.assertEquals(Integer.MAX_VALUE, DungeonMath.safeCastLongToInteger(Integer.MAX_VALUE));
  }

  @Test
  public void safeCastLongToIntegerShouldThrowExceptionOnUnderflow() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonMath.safeCastLongToInteger(((long) Integer.MIN_VALUE) - 1);
    });
  }

  @Test
  public void safeCastLongToIntegerShouldThrowExceptionOnOverflow() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DungeonMath.safeCastLongToInteger(((long) Integer.MAX_VALUE) + 1);
    });
  }

}
