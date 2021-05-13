package org.mafagafogigante.dungeon.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;

public class DungeonStringTest {

  @Test
  public void testGetLengthShouldWorkWithDefaultColor() throws Exception {
    DungeonString dungeonString = new DungeonString();
    Assertions.assertEquals(0, dungeonString.getLength());
    for (int i = 0; i < 10; i++) {
      dungeonString.append("A");
      Assertions.assertEquals(i + 1, dungeonString.getLength());
    }
  }

  @Test
  public void testGetLengthShouldWorkWithMultipleColors() throws Exception {
    DungeonString dungeonString = new DungeonString();
    Assertions.assertEquals(0, dungeonString.getLength());
    dungeonString.append("A");
    Assertions.assertEquals(1, dungeonString.getLength());
    dungeonString.setColor(Color.BLUE);
    Assertions.assertEquals(1, dungeonString.getLength());
    dungeonString.append("A");
    Assertions.assertEquals(2, dungeonString.getLength());
    dungeonString.setColor(Color.RED);
    Assertions.assertEquals(2, dungeonString.getLength());
    dungeonString.append("A");
    Assertions.assertEquals(3, dungeonString.getLength());
  }

}
