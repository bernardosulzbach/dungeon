package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.game.DungeonString;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JTextPane;
import javax.swing.text.Document;

public class SwappingStyledDocumentTest {

  @Test
  public void testWriteShouldSwapBetweenTwoDocuments() throws Exception {
    JTextPane jTextPane = new JTextPane();
    SwappingStyledDocument swappingStyledDocument = new SwappingStyledDocument(jTextPane);
    Document firstDocument = jTextPane.getStyledDocument();
    swappingStyledDocument.write(new DungeonString("A"), new WritingSpecifications(false, 0));
    Document secondDocument = jTextPane.getStyledDocument();
    swappingStyledDocument.write(new DungeonString("B"), new WritingSpecifications(false, 0));
    Document thirdDocument = jTextPane.getStyledDocument();
    Assertions.assertNotEquals(firstDocument, secondDocument);
    Assertions.assertEquals(firstDocument, thirdDocument);
  }

  @Test
  public void testClearShouldEraseEverythingInTheTextPane() throws Exception {
    JTextPane jTextPane = new JTextPane();
    SwappingStyledDocument swappingStyledDocument = new SwappingStyledDocument(jTextPane);
    swappingStyledDocument.write(new DungeonString("."), new WritingSpecifications(false, 0));
    Assertions.assertNotEquals(jTextPane.getText().length(), 0);
    swappingStyledDocument.clear();
    Assertions.assertEquals(jTextPane.getText().length(), 0);
  }

}
