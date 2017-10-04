package org.mafagafogigante.dungeon.game;

import org.junit.Assert;
import org.junit.Test;

public class DirectionTest {

  @Test
  public void invertShouldCorrectlyInvertTheDirection() throws Exception {
    Assert.assertEquals(Direction.UP, Direction.DOWN.invert());
    Assert.assertEquals(Direction.NORTH, Direction.SOUTH.invert());
    Assert.assertEquals(Direction.EAST, Direction.WEST.invert());
    Assert.assertEquals(Direction.DOWN, Direction.UP.invert());
    Assert.assertEquals(Direction.SOUTH, Direction.NORTH.invert());
    Assert.assertEquals(Direction.WEST, Direction.EAST.invert());
  }

  @Test
  public void invertTwiceShouldNotModifyTheDirection() throws Exception {
    for (Direction direction : Direction.values()) {
      Assert.assertEquals(direction, direction.invert().invert());
    }
  }

}
