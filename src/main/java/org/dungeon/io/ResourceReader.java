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

package org.dungeon.io;

import org.dungeon.game.Pair;

import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * ResourceReader class that eases the parsing of resource files.
 * <p/>
 * Created by Bernardo Sulzbach on 16/12/14.
 */
public class ResourceReader implements Closeable {

  private final HashMap<String, String> map = new HashMap<String, String>();
  private final ResourceParser resourceParser;
  private final String filename;

  private Pair<String, String> lastPair;

  /**
   * Constructs a ResourceReader from an InputStream and the respective resource file's name.
   *
   * @param inputStream the InputStream
   * @param filename    the name of the resource file
   */
  public ResourceReader(InputStream inputStream, String filename) {
    resourceParser = new ResourceParser(new InputStreamReader(inputStream));
    this.filename = filename;
  }

  /**
   * Makes a Pair from a String. Both Strings of the Pair are trimmed.
   *
   * @param string a String of the format "key: value".
   * @return a Pair of Strings, with the guarantee that none of them will be {@code null}.
   */
  private Pair<String, String> makePairFromString(String string) {
    String[] parts = {"", ""};
    // TODO: make a static final variable for the colon.
    int indexOfColon = string.indexOf(":");
    if (indexOfColon == -1) {
      logResourceStringWithoutColon();
    } else {
      parts[0] = string.substring(0, indexOfColon).trim();
      if (indexOfColon == string.length() - 1) {
        DLogger.warning("Resource String with nothing after the colon!");
      } else {
        parts[1] = string.substring(indexOfColon + 1).trim();
      }
    }
    return new Pair<String, String>(parts[0], parts[1]);
  }

  /**
   * Logs a warning with the filename and the line number where a resource String without a colon was found.
   */
  private void logResourceStringWithoutColon() {
    String location = "Line " + resourceParser.getLineNumber() + " of " + filename;
    DLogger.warning(location + " does not have a colon!");
  }

  /**
   * Checks if the Reader's last element has a value for a specified key.
   *
   * @param key the Key.
   * @return true if the Reader has the key, false otherwise.
   */
  public boolean hasValue(String key) {
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
    if (lastPair != null) {
      map.put(lastPair.a, lastPair.b);
    }
    while (true) {
      lastPair = readNextPair();
      if (map.containsKey("ID")) {
        if (lastPair == null || lastPair.a.equals("ID")) {
          return true;
        }
      } else {
        if (lastPair == null) {
          return false;
        }
      }
      map.put(lastPair.a, lastPair.b);
    }
  }

  /**
   * Reads the next key-value pair from the ResourceParser.
   */
  private Pair<String, String> readNextPair() {
    String string = resourceParser.readString();
    if (string != null) {
      return makePairFromString(string);
    }
    return null;
  }

  /**
   * Calls the close method of the underlying ResourceParser.
   */
  @Override
  public void close() {
    resourceParser.close();
  }

}
