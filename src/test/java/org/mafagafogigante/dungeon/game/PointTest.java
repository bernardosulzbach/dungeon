package org.mafagafogigante.dungeon.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointTest {

  @Test
  public void equalsShouldReturnTrueToEqualPoints() throws Exception {
    Assertions.assertEquals(new Point(0, 0, 0), new Point(0, 0, 0));
    Assertions.assertEquals(new Point(1, 0, 0), new Point(1, 0, 0));
    Assertions.assertEquals(new Point(0, 1, 0), new Point(0, 1, 0));
    Assertions.assertEquals(new Point(0, 0, 1), new Point(0, 0, 1));
  }

  @Test
  public void equalsShouldReturnFalseToDifferentPoints() throws Exception {
    Assertions.assertNotEquals(new Point(1, 0, 0), new Point(0, 0, 0));
    Assertions.assertNotEquals(new Point(0, 1, 0), new Point(0, 0, 0));
    Assertions.assertNotEquals(new Point(0, 0, 1), new Point(0, 0, 0));
    Assertions.assertNotEquals(new Point(0, 0, 0), new Point(1, 0, 0));
    Assertions.assertNotEquals(new Point(0, 0, 0), new Point(0, 1, 0));
    Assertions.assertNotEquals(new Point(0, 0, 0), new Point(0, 0, 1));
  }

}
