/*
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.util.library;

import org.dungeon.io.DLogger;
import org.dungeon.io.JsonObjectFactory;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;

/**
 * A Library of Poems.
 */
public final class PoetryLibrary extends Library {

  private final ArrayList<Poem> poems = new ArrayList<Poem>();

  private AutomaticShuffledRange automaticShuffledRange;

  /**
   * Returns how many poems the library has.
   * <p/>
   * This should be the first method called in this Library, as it triggers its initialization if it has not happened
   * yet.
   */
  public int getPoemCount() {
    if (isUninitialized()) {
      initialize();
    }
    return poems.size();
  }

  /**
   * Returns the poem at the specified index.
   */
  public Poem getPoem(int index) {
    return poems.get(index);
  }

  /**
   * Returns the next poem according to the underlying {@code AutomaticShuffledRange}.
   */
  public Poem getNextPoem() {
    return poems.get(automaticShuffledRange.getNext());
  }

  @Override
  void load() {
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject("poems.json");
    for (JsonValue poem : jsonObject.get("poems").asArray()) {
      JsonObject poemObject = poem.asObject();
      String title = poemObject.get("title").asString();
      String author = poemObject.get("author").asString();
      String content = poemObject.get("content").asString();
      poems.add(new Poem(title, author, content));
    }
    poems.trimToSize();
    automaticShuffledRange = new AutomaticShuffledRange(poems.size());
    DLogger.info("Loaded " + poems.size() + " poems.");
  }

}
