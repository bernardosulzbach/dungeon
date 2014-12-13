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

package org.dungeon.utils;

import org.dungeon.io.DLogger;

import java.util.ArrayList;

/**
 * DTable class that provides table functionality for printing in-game tables. Currently, all data is stored as Strings.
 * <p/>
 * Created by Bernardo Sulzbach on 13/12/14.
 */
public class DTable {

  private ArrayList<Column> columns;

  /**
   * Constructs a DTable using the provided Strings as column headers.
   *
   * @param headers the columns' headers.
   */
  public DTable(String... headers) {
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

  private class Column {
    String header;
    int widestValue;
    ArrayList<String> rows;

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
