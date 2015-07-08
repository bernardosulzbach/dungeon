/*
 * Copyright (C) 2015 Bernardo Sulzbach
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
import org.dungeon.util.AutomaticShuffledRange;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An automatically shuffled library of strings.
 */
public class AutomaticShuffledStringLibrary extends Library {

  private final String filename;
  private final List<String> strings = new ArrayList<String>();
  private AutomaticShuffledRange automaticShuffledRange;

  public AutomaticShuffledStringLibrary(@NotNull String filename) {
    this.filename = filename;
  }

  @NotNull
  public String next() {
    if (isUninitialized()) {
      initialize();
    }
    return strings.get(automaticShuffledRange.getNext());
  }

  @Override
  void load() {
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject(filename);
    for (JsonValue value : jsonObject.get("strings").asArray()) {
      strings.add(value.asString());
    }
    if (strings.isEmpty()) {
      throw new IllegalStateException("no strings were loaded.");
    }
    automaticShuffledRange = new AutomaticShuffledRange(strings.size());
    DLogger.info("Loaded " + strings.size() + " strings from " + filename + ".");
  }

}
