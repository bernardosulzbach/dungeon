package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.util.RichText;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

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
    RichText richTextA = new StandardRichTextBuilder().append("A").toRichText();
    swappingStyledDocument.write(richTextA, new WritingSpecifications(false, 0));
    Document secondDocument = jTextPane.getStyledDocument();
    RichText richTextB = new StandardRichTextBuilder().append("B").toRichText();
    swappingStyledDocument.write(richTextB, new WritingSpecifications(false, 0));
    Document thirdDocument = jTextPane.getStyledDocument();
    Assert.assertNotEquals(firstDocument, secondDocument);
    Assert.assertEquals(firstDocument, thirdDocument);
  }

  @Test
  public void testClearShouldEraseEverythingInTheTextPane() throws Exception {
    JTextPane jTextPane = new JTextPane();
    SwappingStyledDocument swappingStyledDocument = new SwappingStyledDocument(jTextPane);
    RichText text = new StandardRichTextBuilder().append(".").toRichText();
    swappingStyledDocument.write(text, new WritingSpecifications(false, 0));
    Assert.assertNotEquals(jTextPane.getText().length(), 0);
    swappingStyledDocument.clear();
    Assert.assertEquals(jTextPane.getText().length(), 0);
  }

}
