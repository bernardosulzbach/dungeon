package org.mafagafogigante.dungeon.util.library;

import org.mafagafogigante.dungeon.game.RichString;
import org.mafagafogigante.dungeon.game.RichStringSequence;
import org.mafagafogigante.dungeon.game.Writable;

import java.util.List;

/**
 * Poem class that defines a poem storage data structure.
 */
public final class Poem extends Writable {

  private final String title;
  private final String author;
  private final String content;

  Poem(String title, String author, String content) {
    this.title = title;
    this.author = author;
    this.content = content;
  }

  public List<RichString> toRichStrings() {
    RichStringSequence builder = new RichStringSequence(toString());
    return builder.toRichStrings();
  }

  @Override
  public String toString() {
    return title + "\n\n" + content + "\n\n" + author;
  }

}
