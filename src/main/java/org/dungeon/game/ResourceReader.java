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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * ResourceReader class that eases the parsing of resource files.
 * <p/>
 * Created by Bernardo Sulzbach on 16/12/14.
 */
class ResourceReader implements Closeable {

  private final HashMap<String, String> map;
  private final DBufferedReader dBufferedReader;

  public ResourceReader(InputStream inputStream) {
    map = new HashMap<String, String>();
    dBufferedReader = new DBufferedReader(new InputStreamReader(inputStream));
  }

  public boolean contains(String key) {
    return map.containsKey(key);
  }

  public String getValue(String identifier) {
    return map.get(identifier);
  }

  /**
   * Reads the next element in the resource file to memory.
   *
   * @return true if a new element was read; false otherwise.
   */
  public boolean readNextElement() {
    // TODO: all elements should start with an ID field and be considered complete when the parser hits the next id.
    map.clear();
    boolean readNewValue = true;
    while (readNewValue) {
      readNewValue = readNextValue();
    }
    return !map.isEmpty();
  }

  /**
   * Reads the next ID: value pair from the BufferedReader.
   *
   * Does not put a new value in the HashMap if the HashMap already contains a key that matches the value's ID.
   *
   * @return true if a new value was read; false otherwise.
   */
  private boolean readNextValue() {
    String string = dBufferedReader.readString();
    if (string != null && !string.isEmpty()) {
      int colonIndex = string.indexOf(':');
      String valueId = string.substring(0, colonIndex).trim();
      if (!map.containsKey(valueId)) {
        map.put(valueId, string.substring(colonIndex + 1).trim());
        return true;
      }
    }
    return false;
  }

  @Override
  public void close() throws IOException {
    dBufferedReader.close();
  }

}
