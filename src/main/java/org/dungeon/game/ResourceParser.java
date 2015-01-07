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

  // The wrapped BufferedReader.
  private final BufferedReader br;

  // The last line retrieved by the readLine method, wrapped in a ResourceLine object.
  private ResourceLine line;

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
    do {
      try {
        String text = br.readLine();
        // Do not create a ResourceLine with a null String.
        if (text == null) {
          line = null;
          return;
        }
        line = new ResourceLine(text);
      } catch (IOException e) {
        DLogger.warning(e.getMessage());
      }
    } while (!line.isValid() || line.isComment());
  }

  /**
   * Read a String of text formatted according to the Dungeon convention.
   * <p/>
   * Returns {@code null} if the end of the file has been reached.
   */
  public String readString() {
    readLine();
    if (line == null) {
      return null;
    }
    if (line.isContinued()) {
      StringBuilder sb = new StringBuilder();
      sb.append(line.toString()).append('\n');
      while (line.isContinued()) {
        readLine();
        if (line != null) {
          sb.append(line.toString()).append('\n');
        } else {
          break;
        }
      }
      return sb.toString();
    }
    return line.toString();
  }

  /**
   * Closes the underlying BufferedReader.
   */
  @Override
  public void close() {
    try {
      br.close();
    } catch (IOException e) {
      DLogger.warning(e.getMessage());
    }
  }

}
