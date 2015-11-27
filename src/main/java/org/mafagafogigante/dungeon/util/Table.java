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

package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.ColoredString;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Table class that represents an arrangement of strings in rows and columns.
 *
 * <p>Only allows for data addition, you cannot query or update a Table in any way. This class is used for data
 * visualization, not organization or storage.
 */
public class Table implements Writable {

  private static final char HORIZONTAL_BAR = '-';
  private static final String VERTICAL_BAR = "|";
  private static final int MAXIMUM_COLUMNS = 6;
  private static final int MINIMUM_WIDTH = 5; // Must be positive.

  /**
   * The content of the Table.
   */
  private final List<Column> columns = new ArrayList<>();

  /**
   * A CounterMap of Integers representing how many horizontal separators should precede each row.
   */
  private CounterMap<Integer> separators;

  /**
   * Constructs a Table using the provided Strings as column headers.
   *
   * <strong>There is a hard limit of six columns</strong> in order to prevent huge wide tables that wouldn't ever fit
   * the screen.
   *
   * @param headers the headers, not empty, at most six values
   */
  public Table(String... headers) {
    if (headers.length == 0) {
      throw new IllegalArgumentException("tried to create Table with no headers.");
    } else if (headers.length > MAXIMUM_COLUMNS) {
      throw new IllegalArgumentException("tried to create Table with more than " + MAXIMUM_COLUMNS + " headers.");
    }
    for (String header : headers) {
      columns.add(new Column(header));
    }
  }

  /**
   * Creates a string of repeated characters.
   */
  private static String makeRepeatedCharacterString(int repetitions, char character) {
    StringBuilder builder = new StringBuilder(repetitions);
    for (int i = 0; i < repetitions; i++) {
      builder.append(character);
    }
    return builder.toString();
  }

  /**
   * Appends a row to a DungeonString.
   *
   * @param builder the DungeonString object
   * @param widths the widths of the columns of the table
   * @param values the values of the row
   */
  private static void appendRow(DungeonString builder, int[] widths, String... values) {
    for (int i = 0; i < values.length; i++) {
      int columnWidth = widths[i];
      String currentValue = values[i];
      if (currentValue.length() > columnWidth) {
        if (columnWidth < 4) { // This is how spreadsheet editors seem to handle it.
          builder.append(makeRepeatedCharacterString(columnWidth, '#'));
        } else {
          builder.append(currentValue.substring(0, columnWidth - 3));
          builder.append("...");
        }
      } else {
        builder.append(currentValue);
        int extraSpaces = columnWidth - currentValue.length();
        builder.append(makeRepeatedCharacterString(extraSpaces, ' '));
      }
      if (i < values.length - 1) {
        builder.append(VERTICAL_BAR);
      }
    }
    builder.append("\n");
  }

  /**
   * Append a horizontal separator made up of dashes to a DungeonString.
   *
   * @param builder the DungeonString object
   * @param columnWidths the width of the columns of the table
   */
  private static void appendHorizontalSeparator(DungeonString builder, int[] columnWidths, int columnCount) {
    String[] pseudoRow = new String[columnCount];
    for (int i = 0; i < columnWidths.length; i++) {
      pseudoRow[i] = makeRepeatedCharacterString(columnWidths[i], HORIZONTAL_BAR);
    }
    appendRow(builder, columnWidths, pseudoRow);
  }

  /**
   * Distributes a value among buckets. For instance, distributing 3 over {2, 3, 4} gives {3, 4, 5} and distributing -8
   * over {5, 10} gives {1, 6}. If the division of value by the size of buckets is not exact, the first buckets are
   * going to get more modified. For instance, distributing 3 over {2, 3} gives {4, 4} and distributing -8 over {5, 10,
   * 15} gives {2, 7, 13}.
   *
   * <p>This algorithm respects the MINIMUM_WIDTH constant.
   *
   * <p>The time complexity of this implementation is O(n) on the size of buckets
   *
   * @param value the total to be distributed
   * @param buckets the buckets, not empty, not null
   */
  private static void distribute(int value, @NotNull int[] buckets) {
    repeatModification(Math.abs(value), Integer.signum(value), buckets);
  }

  /**
   * Applies modification reps times over the bucket array.
   */
  private static void repeatModification(int reps, int modification, @NotNull int[] buckets) {
    if (buckets.length == 0) {
      throw new IllegalArgumentException("buckets must have at least one element.");
    }
    if (DungeonMath.sum(buckets) + reps * modification < MINIMUM_WIDTH * buckets.length) {
      String format = "minimum is impossible. Got %d x %d for %s for a minimum of %d.";
      String message = String.format(format, reps, modification, Arrays.toString(buckets), MINIMUM_WIDTH);
      throw new IllegalArgumentException(message);
    }
    int i = 0;
    while (reps > 0) {
      if (buckets[i] + modification >= MINIMUM_WIDTH) {
        buckets[i] += modification;
        reps--;
      }
      i = (i + 1) % buckets.length;
    }
  }

  /**
   * Inserts a row of values at the end of the table. The number of provided values should equal the number of columns.
   *
   * @param values the values to be inserted
   */
  public void insertRow(String... values) {
    if (values.length != columns.size()) {
      String expectedButGotString = "Expected " + columns.size() + ", but got " + values.length + ".";
      if (values.length < columns.size()) {
        throw new IllegalArgumentException("provided less values than there are rows. " + expectedButGotString);
      } else if (values.length > columns.size()) {
        throw new IllegalArgumentException("provided more values than there are rows. " + expectedButGotString);
      }
    }
    for (int i = 0; i < values.length; i++) {
      columns.get(i).insertValue(values[i]);
    }
  }

  /**
   * Inserts a horizontal separator at the last row of the Table.
   */
  public void insertSeparator() {
    if (separators == null) {
      separators = new CounterMap<>();
    }
    separators.incrementCounter(columns.get(0).rows.size());
  }

  private int[] calculateColumnWidths() throws IllegalArgumentException {
    int[] widths = getMaximumColumnWidths();
    int availableWidth = getAvailableWidth();
    int difference = availableWidth - DungeonMath.sum(widths);
    distribute(difference, widths);
    return widths;
  }

  private int getAvailableWidth() {
    // Subtract the number of columns to account for separators. Add one because there is not a separator at the end.
    return GameWindow.COLS - columns.size() + 1;
  }

  private int[] getMaximumColumnWidths() {
    int[] widths = new int[columns.size()];
    for (int i = 0; i < widths.length; i++) {
      widths[i] = columns.get(i).widestValue;
    }
    return widths;
  }

  @Override
  public List<ColoredString> toColoredStringList() {
    DungeonString string = new DungeonString();

    int columnCount = columns.size();

    int[] columnWidths;
    try {
      // You likely don't want toColoredStringList to be throwing exceptions, so catch them early.
      columnWidths = calculateColumnWidths();
    } catch (RuntimeException log) {
      DungeonLogger.warning(log.getMessage());
      string.append("Failed to generate a visual representation of the table.");
      return string.toColoredStringList();
    }

    String[] currentRow = new String[columnCount];

    // Insert headers
    for (int i = 0; i < columnCount; i++) {
      currentRow[i] = columns.get(i).header;
    }
    appendRow(string, columnWidths, currentRow);

    // A horizontal separator.
    appendHorizontalSeparator(string, columnWidths, columnCount);

    int rowCount = columns.get(0).rows.size();
    // Insert table body.
    for (int rowIndex = 0; rowIndex < rowCount + 1; rowIndex++) {
      if (separators != null) {
        for (int remaining = separators.getCounter(rowIndex); remaining > 0; remaining--) {
          appendHorizontalSeparator(string, columnWidths, columnCount);
        }
      }
      if (rowIndex != rowCount) {
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
          currentRow[columnIndex] = columns.get(columnIndex).rows.get(rowIndex);
        }
        appendRow(string, columnWidths, currentRow);
      }
    }
    return string.toColoredStringList();
  }

  private class Column {
    final String header;
    final List<String> rows = new ArrayList<>();
    int widestValue;

    public Column(String header) {
      this.header = header;
      widestValue = header.length();
    }

    /**
     * Inserts a new value at the end of this Column. If the provided value is null, an empty string is used.
     *
     * @param value the value to be inserted, null will be replaced by an empty string
     */
    void insertValue(String value) {
      if (value == null) {
        value = "";
      }
      rows.add(value);
      widestValue = Math.max(widestValue, value.length());
    }
  }

}
