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

package org.dungeon.game;

import org.dungeon.io.DLogger;
import org.dungeon.util.Poem;
import org.dungeon.util.PoemBuilder;

import java.util.ArrayList;

/**
 * A component of GameData.
 * <p/>
 * Created by Bernardo Sulzbach on 15/12/14.
 */
public final class PoetryData {

  private final ArrayList<Poem> poems;
  private boolean initialized;

  public PoetryData() {
    poems = new ArrayList<Poem>();
  }

  public int getPoemCount() {
    if (!initialized) {
      initialize();
    }
    return poems.size();
  }

  public Poem getPoem(int index) {
    return poems.get(index);
  }

  private void initialize() {
    loadPoems();
    initialized = true;
  }

  private void loadPoems() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    ResourceReader reader = new ResourceReader(classLoader.getResourceAsStream("poems.txt"));
    final String IDENTIFIER_TITLE = "TITLE";
    final String IDENTIFIER_AUTHOR = "AUTHOR";
    final String IDENTIFIER_CONTENT = "CONTENT";
    while (reader.readNextElement()) {
      PoemBuilder pb = new PoemBuilder();
      pb.setTitle(reader.getValue(IDENTIFIER_TITLE));
      pb.setAuthor(reader.getValue(IDENTIFIER_AUTHOR));
      pb.setContent(reader.getValue(IDENTIFIER_CONTENT));
      if (pb.isComplete()) {
        poems.add(pb.createPoem());
      }
    }
    DLogger.info("Loaded " + poems.size() + " poems.");
    poems.trimToSize();
  }

}
