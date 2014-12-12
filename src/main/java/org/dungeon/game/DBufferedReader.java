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
public class DBufferedReader implements Closeable {

  // The last line retrieved by the readLine method.
  private String line;
  // True if line ends with the LINE_BREAK character.
  private boolean continued;

  // A line breaking character.
  private static final char LINE_BREAK = '\\';

  // The wrapped BufferedReader.
  private final BufferedReader br;

  /**
   * Creates a convenient buffered reader for reading Dungeon resource files.
   *
   * @param in the Reader.
   */
  public DBufferedReader(Reader in) {
    br = new BufferedReader(in);
  }

  /**
   * Read the next line from the BufferedReader to the private variable <code>line</code>.
   */
  private void readLine() {
    try {
      line = br.readLine();
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
   * @return true if the line ends with the line break character. False otherwise.
   */
  private boolean isContinued() {
    return !line.isEmpty() && line.charAt(line.length() - 1) == LINE_BREAK;
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
