package org.mafagafogigante.dungeon.game;

import org.junit.Assert;
import org.junit.Test;

public class PointTest {

  @Test
  public void equalsShouldReturnTrueToEqualPoints() throws Exception {
    Assert.assertTrue(new Point(0, 0, 0).equals(new Point(0, 0, 0)));
    Assert.assertTrue(new Point(1, 0, 0).equals(new Point(1, 0, 0)));
    Assert.assertTrue(new Point(0, 1, 0).equals(new Point(0, 1, 0)));
    Assert.assertTrue(new Point(0, 0, 1).equals(new Point(0, 0, 1)));
  }

  @Test
  public void equalsShouldReturnFalseToDifferentPoints() throws Exception {
    Assert.assertFalse(new Point(1, 0, 0).equals(new Point(0, 0, 0)));
    Assert.assertFalse(new Point(0, 1, 0).equals(new Point(0, 0, 0)));
    Assert.assertFalse(new Point(0, 0, 1).equals(new Point(0, 0, 0)));
    Assert.assertFalse(new Point(0, 0, 0).equals(new Point(1, 0, 0)));
    Assert.assertFalse(new Point(0, 0, 0).equals(new Point(0, 1, 0)));
    Assert.assertFalse(new Point(0, 0, 0).equals(new Point(0, 0, 1)));
  }

}
