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

package org.dungeon.util;

import org.dungeon.io.DLogger;
import org.dungeon.io.IO;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Table class that provides table functionality for printing in-game tables.
 *
 * Currently, all data is stored as Strings.
 * <p/>
 * Created by Bernardo Sulzbach on 13/12/14.
 */
public class Table {

  private final ArrayList<Column> columns;

  /**
   * Constructs a Table using the provided Strings as column headers.
   *
   * @param headers the columns' headers.
   */
  public Table(String... headers) {
    columns = new ArrayList<Column>(headers.length);
    for (String header : headers) {
      columns.add(new Column(header));
    }
  }

  /**
   * Inserts a row of values in the end of the table. If not enough values are supplied, the remaining columns are
   * filled with empty strings.
   *
   * @param values the values to be inserted.
   */
  public void insertRow(String... values) {
    int columnCount = columns.size();
    if (values.length <= columnCount) {
      for (int i = 0; i < columnCount; i++) {
        if (i < values.length) {
          columns.get(i).insertValue(values[i]);
        } else {
          columns.get(i).insertValue("");
        }
      }
    } else {
      DLogger.warning("Tried to insert more values than columns!");
    }
  }

  /**
   * Tests if the table has a specific value.
   *
   * @param value the value.
   * @return true if the table contains the value, false otherwise.
   */
  public boolean contains(String value) {
    for (Column column : columns) {
      if (column.contains(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns a pair of the form (rows, columns) representing the table's dimensions.
   */
  public Dimensions getDimensions() {
    int columnCount = columns.size();
    if (columnCount != 0) {
      return new Dimensions(columns.get(0).size(), columnCount);
    } else {
      return new Dimensions(0, 0);
    }
  }

  /**
   * Prints the table to the game window.
   */
  public void print() {
    if (columns.size() == 0) {
      DLogger.warning("Tried to print an empty Table.");
      return;
    }

    int columnCount = columns.size();
    // Subtract columnCount to account for separators.
    // Add one because we do not add a separator at the end.
    int columnWidth = (Constants.COLS - columnCount + 1) / columnCount;

    int rowCount = columns.get(0).rows.size();

    StringBuilder sb = new StringBuilder(Constants.COLS * rowCount + 16);
    String[] currentRow = new String[columnCount];

    // Insert headers
    for (int i = 0; i < columnCount; i++) {
      currentRow[i] = columns.get(i).header;
    }
    appendRow(sb, columnWidth, currentRow);

    // A horizontal separator.
    appendHorizontalSeparator(sb, columnWidth, columnCount);

    // Insert table body.
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        currentRow[columnIndex] = columns.get(columnIndex).rows.get(rowIndex);
      }
      appendRow(sb, columnWidth, currentRow);
    }

    // Dump to the window.
    IO.writeString(sb.toString());
  }

  /**
   * Appends a row to a StringBuilder.
   *
   * @param stringBuilder the StringBuilder object.
   * @param columnWidth   the width of the columns of the table.
   * @param values        the values of the row.
   */
  private void appendRow(StringBuilder stringBuilder, int columnWidth, String... values) {
    String currentValue;
    for (int i = 0; i < values.length; i++) {
      currentValue = values[i];
      if (currentValue.length() > columnWidth) {
        stringBuilder.append(currentValue.substring(0, columnWidth - 3)).append("...");
      } else {
        stringBuilder.append(currentValue);
        int extraSpaces = columnWidth - currentValue.length();
        for (int j = 0; j < extraSpaces; j++) {
          stringBuilder.append(" ");
        }
      }
      if (i < values.length - 1) {
        stringBuilder.append('|');
      }
    }
    stringBuilder.append('\n');
  }

  /**
   * Append a horizontal separator made up of dashes to a StringBuilder.
   *
   * @param stringBuilder the StringBuilder object.
   * @param columnWidth   the width of the columns of the table.
   */
  private void appendHorizontalSeparator(StringBuilder stringBuilder, int columnWidth, int columnCount) {
    String pseudoValue = Utils.makeRepeatedCharacterString(columnWidth, '-');
    String[] pseudoRow = new String[columnCount];
    Arrays.fill(pseudoRow, pseudoValue);
    appendRow(stringBuilder, columnWidth, pseudoRow);
  }

  private class Column {
    final String header;
    int widestValue;
    final ArrayList<String> rows;

    public Column(String header) {
      rows = new ArrayList<String>();
      this.header = header;
      widestValue = header.length();
    }

    void insertValue(String value) {
      if (value == null) {
        rows.add("");
      } else {
        rows.add(value);
        int length = value.length();
        if (length > widestValue) {
          widestValue = length;
        }
      }
    }

    boolean contains(String value) {
      return rows.contains(value);
    }

    int size() {
      return rows.size();
    }

  }

}
