package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;

public class StandardRichTextTest {

  @Test
  public void testGetLengthShouldWorkWithDefaultColor() throws Exception {
    StandardRichTextBuilder standardRichText = new StandardRichTextBuilder();
    Assert.assertEquals(0, standardRichText.getLength());
    standardRichText.append("A");
    Assert.assertEquals(1, standardRichText.getLength());
    standardRichText.append("A");
    Assert.assertEquals(2, standardRichText.getLength());
    standardRichText.append("A");
    Assert.assertEquals(3, standardRichText.getLength());
  }

  @Test
  public void testGetLengthShouldWorkWithMultipleColors() throws Exception {
    StandardRichTextBuilder standardRichText = new StandardRichTextBuilder();
    Assert.assertEquals(0, standardRichText.getLength());
    standardRichText.append("A");
    Assert.assertEquals(1, standardRichText.getLength());
    standardRichText.setColor(Color.BLUE);
    Assert.assertEquals(1, standardRichText.getLength());
    standardRichText.append("A");
    Assert.assertEquals(2, standardRichText.getLength());
    standardRichText.setColor(Color.RED);
    Assert.assertEquals(2, standardRichText.getLength());
    standardRichText.append("A");
    Assert.assertEquals(3, standardRichText.getLength());
  }

}
