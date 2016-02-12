package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.gui.WritingSpecifications;

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
    DungeonString string = new DungeonString(text);
    string.append("\n");
    write(string);
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
    if (Game.getGameWindow() != null) { // There will be no window when running the tests, so check to prevent a NPE.
      Game.getGameWindow().scheduleWriteToTextPane(writable, specifications);
      if (specifications.shouldWait()) {
        Sleeper.sleep(specifications.getWait());
      }
    }
  }

  /**
   * Writes a Writable and waits for the default waiting interval.
   */
  public static void writeAndWait(Writable writable) {
    write(writable, new WritingSpecifications(true, DEFAULT_WAIT_INTERVAL));
  }

}
