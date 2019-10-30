package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.game.RichStringSequence;

import org.junit.Assert;
import org.junit.Test;

import javax.swing.JTextPane;
import javax.swing.text.Document;

public class SwappingStyledDocumentTest {

  @Test
  public void testWriteShouldSwapBetweenTwoDocuments() throws Exception {
    JTextPane jTextPane = new JTextPane();
    SwappingStyledDocument swappingStyledDocument = new SwappingStyledDocument(jTextPane);
    Document firstDocument = jTextPane.getStyledDocument();
    swappingStyledDocument.write(new RichStringSequence("A"), new WritingSpecifications(false, 0));
    Document secondDocument = jTextPane.getStyledDocument();
    swappingStyledDocument.write(new RichStringSequence("B"), new WritingSpecifications(false, 0));
    Document thirdDocument = jTextPane.getStyledDocument();
    Assert.assertNotEquals(firstDocument, secondDocument);
    Assert.assertEquals(firstDocument, thirdDocument);
  }

  @Test
  public void testClearShouldEraseEverythingInTheTextPane() throws Exception {
    JTextPane jTextPane = new JTextPane();
    SwappingStyledDocument swappingStyledDocument = new SwappingStyledDocument(jTextPane);
    swappingStyledDocument.write(new RichStringSequence("."), new WritingSpecifications(false, 0));
    Assert.assertNotEquals(jTextPane.getText().length(), 0);
    swappingStyledDocument.clear();
    Assert.assertEquals(jTextPane.getText().length(), 0);
  }

}
