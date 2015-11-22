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

package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.game.DungeonString;

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
    swappingStyledDocument.write(new DungeonString("A"), new WritingSpecifications(false, 0));
    Document secondDocument = jTextPane.getStyledDocument();
    swappingStyledDocument.write(new DungeonString("B"), new WritingSpecifications(false, 0));
    Document thirdDocument = jTextPane.getStyledDocument();
    Assert.assertNotEquals(firstDocument, secondDocument);
    Assert.assertEquals(firstDocument, thirdDocument);
  }

  @Test
  public void testClearShouldEraseEverythingInTheTextPane() throws Exception {
    JTextPane jTextPane = new JTextPane();
    SwappingStyledDocument swappingStyledDocument = new SwappingStyledDocument(jTextPane);
    swappingStyledDocument.write(new DungeonString("."), new WritingSpecifications(false, 0));
    Assert.assertNotEquals(jTextPane.getText().length(), 0);
    swappingStyledDocument.clear();
    Assert.assertEquals(jTextPane.getText().length(), 0);
  }

}