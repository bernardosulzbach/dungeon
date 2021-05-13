package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.util.Percentage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class RandomTest {

  @Test
  public void testRollWithDoubles() throws Exception {
    Assertions.assertFalse(Random.roll(0.0));
    Assertions.assertTrue(Random.roll(1.0));
  }

  @Test
  public void testRollWithPercentages() throws Exception {
    Assertions.assertFalse(Random.roll(new Percentage(0.0)));
    Assertions.assertTrue(Random.roll(new Percentage(1.0)));
  }

  @Test
  public void testSelect() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Random.select(Collections.emptyList());
    });
    Assertions.assertEquals(0, (int) Random.select(Collections.singletonList(0)));
  }

  @Test
  public void testRandomIntegerRespectsMinimum() throws Exception {
    for (int i = 0; i < 1000; i++) {
      Assertions.assertEquals(i, Random.nextInteger(i, i + 1));
    }
  }

}
