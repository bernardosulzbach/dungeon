package org.mafagafogigante.dungeon.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DirectionTest {

  @Test
  public void invertShouldCorrectlyInvertTheDirection() throws Exception {
    Assertions.assertEquals(Direction.UP, Direction.DOWN.invert());
    Assertions.assertEquals(Direction.NORTH, Direction.SOUTH.invert());
    Assertions.assertEquals(Direction.EAST, Direction.WEST.invert());
    Assertions.assertEquals(Direction.DOWN, Direction.UP.invert());
    Assertions.assertEquals(Direction.SOUTH, Direction.NORTH.invert());
    Assertions.assertEquals(Direction.WEST, Direction.EAST.invert());
  }

  @Test
  public void invertTwiceShouldNotModifyTheDirection() throws Exception {
    for (Direction direction : Direction.values()) {
      Assertions.assertEquals(direction, direction.invert().invert());
    }
  }

}
