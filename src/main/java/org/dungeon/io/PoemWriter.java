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

package org.dungeon.io;

import org.dungeon.commands.IssuedCommand;
import org.dungeon.gui.TextPaneWritingSpecifications;
import org.dungeon.util.library.Libraries;
import org.dungeon.util.library.Poem;

import org.jetbrains.annotations.NotNull;

public final class PoemWriter {

  private PoemWriter() {
    throw new AssertionError();
  }

  /**
   * Parses the issued command and tries to print a poem.
   *
   * <p>If the command has arguments, the game attempts to use the first one as the poem's index (one-based).
   *
   * <p>Otherwise, the next poem is based on a behind-the-scenes poem index.
   *
   * @param command the issued command
   */
  public static void parsePoemCommand(@NotNull IssuedCommand command) {
    if (Libraries.getPoetryLibrary().getPoemCount() == 0) {
      Writer.writeString("No poems were loaded.");
    } else {
      if (command.hasArguments()) {
        try {
          // Indexing is zero-based to the implementation, but one-based to the player.
          int index = Integer.parseInt(command.getFirstArgument()) - 1;
          if (index >= 0 && index < Libraries.getPoetryLibrary().getPoemCount()) {
            writePoem(Libraries.getPoetryLibrary().getPoem(index));
            return;
          }
        } catch (NumberFormatException ignore) {
          // This exception reproduces the same error message an invalid index does.
        }
        Writer.writeString("Invalid poem index.");
      } else {
        writePoem(Libraries.getPoetryLibrary().getNextPoem());
      }
    }
  }

  private static void writePoem(Poem poem) {
    Writer.write(poem, new TextPaneWritingSpecifications(false));
  }

}
