package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.game.ColoredString;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Controls writing to a JTextPane in order to maximize performance by avoiding unnecessary renderings.
 *
 * <p>This is done by holding two different StyledDocuments, one of which is updated while the other is exhibited, and
 * swapping them when the update is completed.
 *
 * <p>If an object of this class is being used to update a JTextPane, the document currently assigned to that JTextPane
 * should never be modified directly, but always through the write method of the SwappingStyledDocument object.
 */
final class SwappingStyledDocument {

  private final JTextPane textPane;
  private StyledDocument activeDocument = new DefaultStyledDocument();
  private StyledDocument inactiveDocument = new DefaultStyledDocument();

  /**
   * Constructs a new SwappingStyleDocument for the provided JTextPane.
   */
  public SwappingStyledDocument(JTextPane textPane) {
    this.textPane = textPane;
    textPane.setDocument(activeDocument);
  }

  void write(Writable writable, WritingSpecifications specifications) {
    updateInactiveDocument(writable);
    swapDocuments(specifications);
    updateInactiveDocument(writable);
  }

  private void updateInactiveDocument(Writable writable) {
    writeToDocument(inactiveDocument, writable);
  }

  private void writeToDocument(StyledDocument inactiveDocument, Writable writable) {
    for (ColoredString coloredString : writable.toColoredStringList()) {
      MutableAttributeSet attributeSet = new SimpleAttributeSet();
      StyleConstants.setForeground(attributeSet, coloredString.getColor());
      try {
        inactiveDocument.insertString(inactiveDocument.getLength(), coloredString.getString(), attributeSet);
      } catch (BadLocationException warn) {
        DungeonLogger.warning("insertString resulted in a BadLocationException.");
      }
    }
  }

  private void swapDocuments(WritingSpecifications specifications) {
    final StyledDocument oldActiveDocument = activeDocument;
    activeDocument = inactiveDocument;
    inactiveDocument = oldActiveDocument;
    textPane.setDocument(activeDocument);
    textPane.setCaretPosition(specifications.shouldScrollDown() ? activeDocument.getLength() : 0);
  }

  /**
   * Clears this document.
   */
  void clear() {
    // It doesn't matter which document we erase first. This should only cause one rendering.
    clear(activeDocument);
    clear(inactiveDocument);
  }

  private void clear(StyledDocument document) {
    try {
      document.remove(0, document.getLength());
    } catch (BadLocationException ignored) { // Never happens.
    }
  }

}
