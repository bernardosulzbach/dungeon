package org.mafagafogigante.dungeon.game;

import java.util.List;

/**
 * Defines a single toRichStrings method that Writer uses to write text to the screen.
 */
public abstract class Writable {

  public abstract List<RichString> toRichStrings();

  /**
   * Converts all writable text of this Writable object to a plain Java String.
   */
  protected String toJavaString() {
    StringBuilder builder = new StringBuilder();
    for (RichString richString : toRichStrings()) {
      builder.append(richString.getString());
    }
    return builder.toString();
  }

}
