/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.dungeon.io;

import org.dungeon.game.DungeonString;
import org.dungeon.game.Game;
import org.dungeon.game.Writable;
import org.dungeon.gui.WritingSpecifications;

import java.awt.Color;

/**
 * Writer class that encapsulates all Input/Output operations. This is the only class that should call the writing
 * methods of the game window.
 */
public final class Writer {

  /**
   * For how many milliseconds the game sleeps after writing a string of battle output.
   */
  private static final int DEFAULT_WAIT_INTERVAL = 300;

  private Writer() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Writes a string of text using the default output color.
   *
   * @param text the string of text to be written.
   */
  public static void write(String text) {
    write(text, null);
  }

  /**
   * Writes a string of text using a specific color.
   *
   * @param text the string of text to be written.
   * @param color the color of the text.
   */
  public static void write(String text, Color color) {
    DungeonString string = new DungeonString();
    if (color != null) {
      string.setColor(color);
    }
    string.append(text);
    string.append("\n");
    write(string);
  }

  /**
   * Writes a Writable and waits for the default waiting interval.
   */
  public static void writeAndWait(Writable writable) {
    write(writable, new WritingSpecifications(true, DEFAULT_WAIT_INTERVAL));
  }

  /**
   * The preferred way to write text to the text pane of the window.
   *
   * @param writable a Writable object, not empty
   */
  public static void write(Writable writable) {
    write(writable, new WritingSpecifications(true, 0));
  }

  /**
   * The preferred way to write text to the text pane of the window.
   *
   * @param writable a Writable object, not empty
   * @param specifications a WritingSpecifications object
   */
  public static void write(Writable writable, WritingSpecifications specifications) {
    Game.getGameWindow().scheduleWriteToTextPane(writable, specifications);
    if (specifications.shouldWait()) {
      Sleeper.sleep(specifications.getWait());
    }
  }

}
