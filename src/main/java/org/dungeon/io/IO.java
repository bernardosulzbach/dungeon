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

import org.dungeon.game.Game;
import org.dungeon.util.Constants;
import org.dungeon.util.library.Poem;

import java.awt.Color;

/**
 * IO class that encapsulates all Input/Output operations. This is the only class that should call the writing methods
 * of the game window.
 */
public final class IO {

  // How many milliseconds the game 'lags' after writing a string of battle output.
  private static final int WRITE_BATTLE_STRING_WAIT = 300;

  private IO() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Writes a string of text using the default output color.
   *
   * @param string the string of text to be written.
   */
  public static void writeString(String string) {
    writeString(string, Constants.FORE_COLOR_NORMAL);
  }

  /**
   * Writes a string of text using a specific color.
   *
   * @param string the string of text to be written.
   * @param color  the color of the text.
   */
  public static void writeString(String string, Color color) {
    writeString(string, color, true);
  }

  /**
   * Writes a string of text using a specific color.
   *
   * @param string  the string of text to be written.
   * @param color   the color of the text.
   * @param newLine if true, a newline will be added to the end of the string after its end is cleared.
   */
  public static void writeString(String string, Color color, boolean newLine) {
    writeString(string, color, newLine, true, 0);
  }

  /**
   * Writes a string of text using a specific color and waiting for a given amount of milliseconds.
   *
   * @param string     the string of text to be written.
   * @param color      the color of the text.
   * @param newLine    if true, a newline will be added to the end of the string after its end is cleared.
   * @param scrollDown if true, the TextPane will be scrolled down after writing.
   * @param wait       how many milliseconds the application should sleep after writing the string.
   */
  private static void writeString(String string, Color color, boolean newLine, boolean scrollDown, int wait) {
    if (color == null) {
      DLogger.warning("Passed null as a Color to writeString.");
    }
    if (newLine) {
      string += '\n';
    }
    Game.getGameWindow().writeToTextPane(string, color, scrollDown);
    if (wait > 0) {
      Sleeper.sleep(wait);
    }
  }

  /**
   * Writes a string of text using a specific color and waits for the default battle wait interval.
   *
   * @param string the string of text to be written.
   * @param color  the color of the text.
   */
  public static void writeBattleString(String string, Color color) {
    writeString(string, color, true, true, WRITE_BATTLE_STRING_WAIT);
  }

  /**
   * Writes a line separator, terminating a line or leaving one blank line.
   */
  public static void writeNewLine() {
    writeString("");
  }

  public static void writePoem(Poem poem) {
    writeString(poem.toString(), Constants.FORE_COLOR_NORMAL, false, false, 0);
  }

}
