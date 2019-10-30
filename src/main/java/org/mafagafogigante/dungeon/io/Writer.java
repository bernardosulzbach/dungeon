package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.game.RichStringSequence;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.gui.WritingSpecifications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Writer class that encapsulates all Input/Output operations. This is the only class that should call the writing
 * methods of the game window.
 */
public final class Writer {

  /**
   * For how many milliseconds the game sleeps after writing a string of battle output.
   */
  private static final int DEFAULT_WAIT_INTERVAL = 300;
  private static final Writer defaultWriter = new Writer();
  private final List<Writable> writtenStrings = new ArrayList<>();
  private final Set<GameWindow> subscribers = new HashSet<>();
  private boolean forwarding = true;

  public static Writer getDefaultWriter() {
    return defaultWriter;
  }

  /**
   * Writes a string of text using the default output color.
   *
   * @param text the string of text to be written.
   */
  public void write(String text) {
    RichStringSequence string = new RichStringSequence(text);
    string.append("\n");
    write(string);
  }

  /**
   * The preferred way to write text to the text pane of the window.
   *
   * @param writable a Writable object, not empty
   */
  public void write(Writable writable) {
    write(writable, new WritingSpecifications(true, 0));
  }

  /**
   * The preferred way to write text to the text pane of the window.
   *
   * @param writable a Writable object, not empty
   * @param specifications a WritingSpecifications object
   */
  public void write(Writable writable, WritingSpecifications specifications) {
    writtenStrings.add(writable);
    if (!forwarding) {
      return;
    }
    for (GameWindow window : subscribers) {
      window.scheduleWriteToTextPane(writable, specifications);
      if (specifications.shouldWait()) {
        Sleeper.sleep(specifications.getWait());
      }
    }
  }

  /**
   * Writes a Writable and waits for the default waiting interval.
   */
  public void writeAndWait(Writable writable) {
    write(writable, new WritingSpecifications(true, DEFAULT_WAIT_INTERVAL));
  }

  public void subscribe(GameWindow window) {
    if (!subscribers.contains(window)) {
      subscribers.add(window);
    }
  }

  public void enableForwarding() {
    forwarding = true;
  }

  public void disableForwarding() {
    forwarding = false;
  }

  public void clearWrittenStrings() {
    writtenStrings.clear();
  }

  public List<Writable> getWrittenStrings() {
    return Collections.unmodifiableList(writtenStrings);
  }

}
