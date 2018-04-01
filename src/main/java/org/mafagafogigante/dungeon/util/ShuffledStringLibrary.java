package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An automatically shuffled library of strings.
 */
public class ShuffledStringLibrary implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final String filename;
  private final List<String> strings = new ArrayList<>();
  private InfiniteShuffledRange infiniteShuffledRange;

  public ShuffledStringLibrary(@NotNull String filename) {
    this.filename = filename;
    initialize();
  }

  private void initialize() {
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject(filename);
    for (JsonValue value : jsonObject.get("strings").asArray()) {
      strings.add(value.asString());
    }
    if (strings.isEmpty()) {
      throw new IllegalStateException("No strings were found.");
    }
    infiniteShuffledRange = new InfiniteShuffledRange(strings.size());
    DungeonLogger.info("Loaded " + strings.size() + " strings from " + filename + ".");
  }

  /**
   * Retrieves the next String in the library.
   */
  public String next() {
    return strings.get(infiniteShuffledRange.getNext());
  }

}
