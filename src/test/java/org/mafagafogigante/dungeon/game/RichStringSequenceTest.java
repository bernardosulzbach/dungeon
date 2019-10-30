package org.mafagafogigante.dungeon.game;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;

public class RichStringSequenceTest {

  @Test
  public void testGetLengthShouldWorkWithDefaultColor() throws Exception {
    RichStringSequence richStringSequence = new RichStringSequence();
    Assert.assertEquals(0, richStringSequence.getLength());
    richStringSequence.append("A");
    Assert.assertEquals(1, richStringSequence.getLength());
    richStringSequence.append("A");
    Assert.assertEquals(2, richStringSequence.getLength());
    richStringSequence.append("A");
    Assert.assertEquals(3, richStringSequence.getLength());
  }

  @Test
  public void testGetLengthShouldWorkWithMultipleColors() throws Exception {
    RichStringSequence richStringSequence = new RichStringSequence();
    Assert.assertEquals(0, richStringSequence.getLength());
    richStringSequence.append("A");
    Assert.assertEquals(1, richStringSequence.getLength());
    richStringSequence.setColor(Color.BLUE);
    Assert.assertEquals(1, richStringSequence.getLength());
    richStringSequence.append("A");
    Assert.assertEquals(2, richStringSequence.getLength());
    richStringSequence.setColor(Color.RED);
    Assert.assertEquals(2, richStringSequence.getLength());
    richStringSequence.append("A");
    Assert.assertEquals(3, richStringSequence.getLength());
  }

}
