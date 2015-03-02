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

import java.io.Closeable;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ResourceReader class that eases the parsing of resource files.
 * <p/>
 * <b>Usage</b>
 * <ol>
 * <li>
 * Construct an object of this class by calling the constructor with the name of a resource file.
 * </li>
 * <li>
 * Call {@code readNextElement()} to parse the next element from the file. If an element was successfully read,
 * {@code readNextElement()} returns {@code true}, otherwise it returns {@code false}.
 * If this is the first time {@code readNextElement()} is called, a String holding the first key of the first element
 * will be stored in this {@code ResourceReader}. The first element is considered finished when the parser finds a line
 * with this same key.
 * If this is not the first time {@code readNextElement()} is called, the stored String is used to determine when the
 * next element is complete.
 * </li>
 * <li>
 * Call {@code hasValue(String)} to check if the last read element has a given property.
 * </li>
 * <li>
 * Based on the result of step 3, either ignore this property or read it using {@code getValue(String)}.
 * Note that {@code getValue()} returns a {@code String}, which you may need to to convert to whatever type you need.
 * </li>
 * <li>
 * Close the {@code ResourceReader} after reading all the elements you need by calling {@code close()}.
 * </li>
 * </ol>
 * For more information on resource files, see {@code CONTRIBUTING.md}.
 * <p/>
 * Created by Bernardo Sulzbach on 16/12/14.
 */
public class ResourceReader implements Closeable {

  private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  private final Map<String, String[]> map = new HashMap<String, String[]>();
  private final ResourceParser resourceParser;
  private final String filename;
  private String delimiterField;
  private Entry<String, String> entry;

  /**
   * Constructs a ResourceReader from a resource file's name.
   */
  public ResourceReader(String filename) {
    resourceParser = new ResourceParser(new InputStreamReader(classLoader.getResourceAsStream(filename)));
    this.filename = filename;
  }

  /**
   * Converts a String of data to an ArrayList of separate Strings.
   * <p/>
   * "[ First Item | Second Item ]" becomes {"First Item", "Second Item"}.
   */
  protected static String[] toArray(String data) {
    if (data.startsWith("[") && data.endsWith("]")) {
      data = data.substring(1, data.length() - 1).trim();
      // Zero or more whitespaces, the pipe character, and zero more or more whitespaces.
      return data.split("\\s*\\|\\s*");
    } else {
      return new String[]{data};
    }
  }

  /**
   * Reads the next element in the resource file to memory.
   *
   * @return true if a new element was read; false otherwise.
   */
  public boolean readNextElement() {
    if (delimiterField == null) {
      readNextEntry();
      if (entry != null) {
        delimiterField = entry.getKey();
        do {
          if (map.containsKey(entry.getKey())) {
            logRepeatedValue();
          } else {
            addToMap(entry.getKey(), entry.getValue());
          }
          readNextEntry();
        } while (entry != null && !delimiterField.equals(entry.getKey()));
        return true;
      } else {
        return false;
      }
    } else {
      if (entry != null) {
        map.clear();
        do {
          if (map.containsKey(entry.getKey())) {
            logRepeatedValue();
          } else {
            addToMap(entry.getKey(), entry.getValue());
          }
          readNextEntry();
        } while (entry != null && !delimiterField.equals(entry.getKey()));
        return true;
      } else {
        return false;
      }
    }
  }

  private void addToMap(String key, String value) {
    map.put(key, toArray(value));
  }

  /**
   * Reads the next Entry from the ResourceParser and assigns it to the private variable {@code entry}.
   */
  private void readNextEntry() {
    String string = resourceParser.readString();
    if (string == null) {
      entry = null;
    } else {
      entry = makeEntryFromString(string);
    }
  }

  /**
   * Makes a {@code SimpleEntry} from a String. Both key and value Strings are trimmed.
   *
   * @param string a String of the format "key: value"
   * @return a {@code SimpleEntry} of two possibly empty Strings or {@code null}
   */
  private SimpleEntry<String, String> makeEntryFromString(String string) {
    SimpleEntry<String, String> entry = null;
    int indexOfColon = string.indexOf(":");
    if (indexOfColon == -1) {
      logMissingColon();
    } else {
      String key = string.substring(0, indexOfColon).trim();
      entry = new SimpleEntry<String, String>(key, null);
      if (indexOfColon == string.length() - 1) {
        logMissingValue();
      } else {
        entry.setValue(string.substring(indexOfColon + 1).trim());
      }
    }
    return entry;
  }

  /**
   * Checks if the Reader's last element has a value for a specified key.
   *
   * @param key the key
   * @return true if the Reader has a mapping for the key.
   */
  public boolean hasValue(String key) {
    return map.containsKey(key);
  }

  /**
   * Returns the first String to which the specified key is mapped, or {@code null} if this {@code ResourceReader}
   * contains no mapping for the key.
   * If the array of Strings to which the key is mapped has more than one String, logs a warning about it.
   * {@code getArrayOfValues} should be used instead.
   *
   * @param key the key whose associated value is to be returned
   * @return the first String to which to key is mapped.
   * @see org.dungeon.io.ResourceReader#getArrayOfValues getArrayOfValues
   */
  public String getValue(String key) {
    String[] value = map.get(key);
    if (value.length > 1) {
      DLogger.warning("Used getValue with a nontrivial array.");
    }
    return value[0];
  }

  /**
   * Returns the array of Strings to which the specified key is mapped, or {@code null} if this {@code ResourceReader}
   * contains no mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the array of Strings to which this key is mapped.
   */
  public String[] getArrayOfValues(String key) {
    return map.get(key);
  }

  /**
   * Logs a warning that a line redefines an already defined property.
   */
  private void logRepeatedValue() {
    DLogger.warning(filename, resourceParser.getLineNumber(), " repeats a value of its element!");
  }

  /**
   * Logs a warning with the filename and the line number where a resource String without a colon was found.
   */
  private void logMissingColon() {
    DLogger.warning(filename, resourceParser.getLineNumber(), " does not have a colon!");
  }

  /**
   * Logs a warning that a line has a colon but nothing after it.
   */
  private void logMissingValue() {
    DLogger.warning(filename, resourceParser.getLineNumber(), " does not have a value!");
  }

  /**
   * Calls the close method of the underlying ResourceParser.
   */
  @Override
  public void close() {
    resourceParser.close();
  }

}
