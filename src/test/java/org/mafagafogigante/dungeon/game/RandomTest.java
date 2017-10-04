package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.util.Percentage;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTest {

  @Test
  public void testRollWithDoubles() throws Exception {
    Assert.assertFalse(Random.roll(0.0));
    Assert.assertTrue(Random.roll(1.0));
  }

  @Test
  public void testRollWithPercentages() throws Exception {
    Assert.assertFalse(Random.roll(new Percentage(0.0)));
    Assert.assertTrue(Random.roll(new Percentage(1.0)));
  }

  @Test
  public void testSelect() throws Exception {
    try {
      Random.select(Collections.emptyList());
      Assert.fail("expected an exception.");
    } catch (IllegalArgumentException expected) {
      // Dungeon Code Style does not require a comment on exceptions named expected in tests.
    }
    List<Integer> integerList = new ArrayList<>();
    integerList.add(0);
    Assert.assertTrue(Random.select(integerList).equals(0));
  }

  @Test
  public void testRandomIntegerRespectsMinimum() throws Exception {
    for (int i = 0; i < 1000; i++) {
      Assert.assertEquals(i, Random.nextInteger(i, i + 1));
    }
  }

}
