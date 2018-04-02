package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.gui.WritingSpecifications;
import org.mafagafogigante.dungeon.util.Poem;
import org.mafagafogigante.dungeon.util.PoetryLibrary;

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
    PoetryLibrary poetryLibrary = Game.getGameState().getPoetryLibrary();
    if (poetryLibrary.getPoemCount() == 0) {
      Writer.getDefaultWriter().write("No poems were loaded.");
    } else {
      if (arguments.length != 0) {
        try {
          // Indexing is zero-based to the implementation, but one-based to the player.
          int index = Integer.parseInt(arguments[0]) - 1;
          if (index >= 0 && index < poetryLibrary.getPoemCount()) {
            writePoem(poetryLibrary.getPoem(index));
            return;
          }
        } catch (NumberFormatException ignore) {
          // This exception reproduces the same error message an invalid index does.
        }
        Writer.getDefaultWriter().write("Invalid poem index.");
      } else {
        writePoem(poetryLibrary.getNextPoem());
      }
    }
  }

  private static void writePoem(Poem poem) {
    Writer.getDefaultWriter().write(poem, new WritingSpecifications(false, 0));
  }

}
