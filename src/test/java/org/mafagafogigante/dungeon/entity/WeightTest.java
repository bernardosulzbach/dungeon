package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.util.Percentage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeightTest {

  private static final double[] WEIGHTS = {0.0, 0.001, 0.01, 0.1, 1.0, 10.0, 100.0};
  private static final double[] FRACTIONS = {0.0, 0.001, 0.01, 0.1, 1.0};

  @Test
  public void newInstanceShouldWorkForNonnegativeValues() throws Exception {
    for (final double value : WEIGHTS) {
      Weight.newInstance(value);
    }
  }

  @Test
  public void addingZeroShouldNotModifyWeights() throws Exception {
    for (final double value : WEIGHTS) {
      final Weight valueWeight = Weight.newInstance(value);
      Assertions.assertEquals(valueWeight, Weight.ZERO.add(valueWeight));
      Assertions.assertEquals(valueWeight, valueWeight.add(Weight.ZERO));
    }
  }

  @Test
  public void addingShouldWorkAsExpected() throws Exception {
    for (final double first : WEIGHTS) {
      for (final double second : WEIGHTS) {
        final Weight firstWeight = Weight.newInstance(first);
        final Weight secondWeight = Weight.newInstance(second);
        Assertions.assertEquals(Weight.newInstance(first + second), firstWeight.add(secondWeight));
        Assertions.assertEquals(Weight.newInstance(first + second), secondWeight.add(firstWeight));
      }
    }
  }

  @Test
  public void multiplyingShouldWorkAsExpected() throws Exception {
    for (final double weight : WEIGHTS) {
      for (final double fraction : FRACTIONS) {
        final Weight expected = Weight.newInstance(weight * fraction);
        Assertions.assertEquals(expected, Weight.newInstance(weight).multiply(new Percentage(fraction)));
      }
    }
  }

}
