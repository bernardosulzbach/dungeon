/*
 * Copyright (C) 2015 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.map;

import org.dungeon.game.Game;
import org.dungeon.gui.SharedConstants;

import org.jetbrains.annotations.NotNull;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public final class WorldMapWriter {

  private WorldMapWriter() {
    throw new AssertionError();
  }

  /**
   * Write a WorldMap to the screen. This erases all the content currently on the screen.
   *
   * @param map a WorldMap, not null
   */
  public static void writeMap(@NotNull WorldMap map) {
    Document document = new DefaultStyledDocument();
    SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    StyleConstants.setBackground(attributeSet, SharedConstants.getInsideColor());
    for (WorldMapSymbol[] line : map.getSymbolMatrix()) {
      for (WorldMapSymbol symbol : line) {
        StyleConstants.setForeground(attributeSet, symbol.getColor());
        appendToDocument(document, symbol.getCharacterAsString(), attributeSet);
      }
      appendToDocument(document, "\n", attributeSet);
    }
    Game.getGameWindow().writeMapToTextPane(document);
  }

  private static void appendToDocument(Document document, String string, AttributeSet attributeSet) {
    try {
      document.insertString(document.getLength(), string, attributeSet);
    } catch (BadLocationException fatal) { // Shouldn't happen.
      throw new RuntimeException(fatal); // Kill the game.
    }
  }

}
