package org.mafagafogigante.dungeon.util.library;

import org.mafagafogigante.dungeon.util.RichString;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;
import org.mafagafogigante.dungeon.util.Writable;

import java.util.List;

/**
 * Poem class that defines a poem storage data structure.
 */
public final class Poem implements Writable {

  private final String title;
  private final String author;
  private final String content;

  Poem(String title, String author, String content) {
    this.title = title;
    this.author = author;
    this.content = content;
  }

  public List<RichString> toRichStrings() {
    return new StandardRichTextBuilder().append(toString()).toRichText().toRichStrings();
  }

  @Override
  public String toString() {
    return title + "\n\n" + content + "\n\n" + author;
  }

}
