package org.mafagafogigante.dungeon.game;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;

public class DungeonStringTest {

  @Test
  public void testGetLengthShouldWorkWithDefaultColor() throws Exception {
    DungeonString dungeonString = new DungeonString();
    Assert.assertEquals(0, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(1, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(2, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(3, dungeonString.getLength());
  }

  @Test
  public void testGetLengthShouldWorkWithMultipleColors() throws Exception {
    DungeonString dungeonString = new DungeonString();
    Assert.assertEquals(0, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(1, dungeonString.getLength());
    dungeonString.setColor(Color.BLUE);
    Assert.assertEquals(1, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(2, dungeonString.getLength());
    dungeonString.setColor(Color.RED);
    Assert.assertEquals(2, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(3, dungeonString.getLength());
  }

}
