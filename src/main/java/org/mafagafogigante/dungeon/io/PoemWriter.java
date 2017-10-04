package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.gui.WritingSpecifications;
import org.mafagafogigante.dungeon.util.library.Libraries;
import org.mafagafogigante.dungeon.util.library.Poem;

/**
 * The main purpose of this class is to provide a public static parsePoemCommand method that parses poem commands.
 */
public final class PoemWriter {

  private PoemWriter() {
    throw new AssertionError();
  }

  /**
   * Parses the issued command and tries to print a poem.
   *
   * <p>If the command has arguments, the game attempts to use the first one as the poem's index (one-based).
   *
   * <p>Otherwise, the next poem is based on a behind-the-scenes poem index. d command
   */
  public static void parsePoemCommand(String[] arguments) {
    if (Libraries.getPoetryLibrary().getPoemCount() == 0) {
      Writer.write("No poems were loaded.");
    } else {
      if (arguments.length != 0) {
        try {
          // Indexing is zero-based to the implementation, but one-based to the player.
          int index = Integer.parseInt(arguments[0]) - 1;
          if (index >= 0 && index < Libraries.getPoetryLibrary().getPoemCount()) {
            writePoem(Libraries.getPoetryLibrary().getPoem(index));
            return;
          }
        } catch (NumberFormatException ignore) {
          // This exception reproduces the same error message an invalid index does.
        }
        Writer.write("Invalid poem index.");
      } else {
        writePoem(Libraries.getPoetryLibrary().getNextPoem());
      }
    }
  }

  private static void writePoem(Poem poem) {
    Writer.write(poem, new WritingSpecifications(false, 0));
  }

}
