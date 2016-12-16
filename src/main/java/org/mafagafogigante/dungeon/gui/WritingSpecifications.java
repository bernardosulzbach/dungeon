package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.util.NonNegativeInteger;

/**
 * A series of specifications for a text pane write.
 */
public final class WritingSpecifications {

  private final boolean scrollDown;
  private final NonNegativeInteger wait;

  /**
   * Constructs a new WritingSpecifications object.
   *
   * @param scrollDown if the pane should scroll down
   * @param wait how many milliseconds the application should wait before returning, nonnegative
   */
  public WritingSpecifications(boolean scrollDown, int wait) {
    this.scrollDown = scrollDown;
    this.wait = new NonNegativeInteger(wait);
  }

  boolean shouldScrollDown() {
    return scrollDown;
  }

  public boolean shouldWait() {
    return getWait() != 0;
  }

  public int getWait() {
    return wait.toInteger();
  }

  @Override
  public String toString() {
    return "WritingSpecifications{scrollDown=" + scrollDown + "}";
  }

}
