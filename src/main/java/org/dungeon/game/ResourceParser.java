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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * Dungeon custom buffered reader. Allow easier input of text-based data formatted according to the Dungeon convention.
 * <p/>
 * Created on 05/12/14.
 */
class ResourceParser implements Closeable {

  // A line breaking character.
  private static final char LINE_BREAK = '\\';
  // The wrapped BufferedReader.
  private final BufferedReader br;
  // The last line retrieved by the readLine method.
  // TODO: make a class for line - with isComment and isContinued as methods
  //       consider also making an isValid method that checks if the line of text is a valid line and logs if it is not.
  private String line;
  // True if line ends with the LINE_BREAK character.
  private boolean continued;

  /**
   * Creates a convenient buffered reader for reading Dungeon resource files.
   *
   * @param in the Reader.
   */
  public ResourceParser(Reader in) {
    br = new BufferedReader(in);
  }

  /**
   * Read the next line from the BufferedReader to the private variable {@code line}.
   */
  private void readLine() {
    try {
      do {
        line = br.readLine();
      } while (line != null && (line.isEmpty() || isComment()));
      if (line != null) {
        continued = isContinued();
        if (continued) {
          line = line.substring(0, line.length() - 1);
        }
        line = line.trim();
      } else {
        continued = false;
      }
    } catch (IOException e) {
      DLogger.warning(e.getMessage());
    }
  }

  /**
   * Preconditions: line != null and line.isEmpty() == false
   *
   * @return true if the line ends with the line break character. False otherwise.
   */
  private boolean isContinued() {
    return line.charAt(line.length() - 1) == LINE_BREAK;
  }

  /**
   * Preconditions: line != null and line.isEmpty() == false
   *
   * @return true if the line starts with a valid comment starting sequence.
   */
  private boolean isComment() {
    return line.trim().startsWith("//") || line.trim().startsWith("#");
  }

  /**
   * Read a String of text formatted according to the Dungeon convention.
   */
  public String readString() {
    readLine();
    if (continued) {
      StringBuilder sb = new StringBuilder(line);
      while (continued) {
        readLine();
        if (line != null) {
          sb.append('\n').append(line);
        }
      }
      return sb.toString();
    }
    return line;
  }

  /**
   * Closes the underlying BufferedReader.
   *
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    br.close();
  }

}
