package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.DungeonResource;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.io.ResourceNameResolver;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Library of Poems.
 */
public final class PoetryLibrary implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final ArrayList<Poem> poems = new ArrayList<>();
  private final InfiniteShuffledRange infiniteShuffledRange;

  public PoetryLibrary() {
    loadPoems();
    infiniteShuffledRange = new InfiniteShuffledRange(poems.size());
  }

  private void loadPoems() {
    String filename = ResourceNameResolver.resolveName(DungeonResource.POEMS);
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject(filename);
    for (JsonValue poem : jsonObject.get("poems").asArray()) {
      JsonObject poemObject = poem.asObject();
      String title = poemObject.get("title").asString();
      String author = poemObject.get("author").asString();
      String content = poemObject.get("content").asString();
      poems.add(new Poem(title, author, content));
    }
    poems.trimToSize();
    DungeonLogger.info("Loaded " + poems.size() + " poems.");
  }

  /**
   * Returns how many poems the library has.
   *
   * <p>This should be the first method called in this Library, as it triggers its initialization if it has not happened
   * yet.
   */
  public int getPoemCount() {
    return poems.size();
  }

  /**
   * Returns the poem at the specified index.
   */
  public Poem getPoem(int index) {
    return poems.get(index);
  }

  /**
   * Returns the next poem according to the underlying {@code InfiniteShuffledRange}.
   */
  public Poem getNextPoem() {
    return poems.get(infiniteShuffledRange.getNext());
  }

}
