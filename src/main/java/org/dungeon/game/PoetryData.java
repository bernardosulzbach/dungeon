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
import org.dungeon.util.Utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A component of GameData.
 *
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
    String IDENTIFIER_TITLE = "TITLE:";
    String IDENTIFIER_AUTHOR = "AUTHOR:";
    String IDENTIFIER_CONTENT = "CONTENT:";

    String line;
    PoemBuilder pb = new PoemBuilder();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    DBufferedReader reader = new DBufferedReader(new InputStreamReader(cl.getResourceAsStream("poems.txt")));

    try {
      while ((line = reader.readString()) != null) {
        if (line.startsWith(IDENTIFIER_TITLE)) {
          if (pb.isComplete()) {
            poems.add(pb.createPoem());
            pb = new PoemBuilder();
          }
          pb.setTitle(Utils.getAfterColon(line).trim());
        } else if (line.startsWith(IDENTIFIER_AUTHOR)) {
          pb.setAuthor(Utils.getAfterColon(line).trim());
        } else if (line.startsWith(IDENTIFIER_CONTENT)) {
          pb.setContent(Utils.getAfterColon(line).trim());
        }
      }
      reader.close();
    } catch (IOException exception) {
      DLogger.warning(exception.toString());
    }
    if (pb.isComplete()) {
      poems.add(pb.createPoem());
    }
    DLogger.info("Loaded " + poems.size() + " poems.");
    poems.trimToSize();
  }

}
