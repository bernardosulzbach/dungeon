package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.ColoredString;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Table class that represents an arrangement of strings in rows and columns.
 *
 * <p>Only allows for data addition, you cannot query or update a Table in any way. This class is used for data
 * visualization, not organization or storage.
 */
public class Table extends Writable {

  private static final char HORIZONTAL_BAR = '-';
  private static final String VERTICAL_BAR = "|";
  private static final int MAXIMUM_COLUMNS = 6;
  private static final int MINIMUM_WIDTH = 5; // Must be positive.

  /**
   * The content of the Table.
   */
  private final List<Column> columns = new ArrayList<>();
  private final List<ColumnAlignment> columnAlignments = new ArrayList<>();

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
      columnAlignments.add(ColumnAlignment.LEFT);
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
   * Distributes a value among buckets. For instance, distributing 3 over {2, 3, 4} gives {3, 4, 5} and distributing -8
   * over {5, 10} gives {1, 6}. If the division of value by the size of buckets is not exact, the first buckets are
   * going to get more modified. For instance, distributing 3 over {2, 3} gives {4, 4} and distributing -8 over {5, 10,
   * 15} gives {2, 7, 13}.
   *
   * <p>This algorithm respects the MINIMUM_WIDTH constant.
   *
   * <p>The time complexity of this implementation is O(n) on the size of buckets.
   */
  private static void distribute(int value, List<Integer> buckets) {
    repeatModification(Math.abs(value), Integer.signum(value), buckets);
  }

  /**
   * Applies modification repetitions times over the bucket array.
   */
  private static void repeatModification(int repetitions, int modification, List<Integer> buckets) {
    if (buckets.isEmpty()) {
      throw new IllegalArgumentException("buckets must have at least one element.");
    }
    if (DungeonMath.sum(buckets) + repetitions * modification < MINIMUM_WIDTH * buckets.size()) {
      String format = "minimum is impossible. Got %d x %d for %s for a minimum of %d.";
      String bucketsString = Arrays.toString(buckets.toArray());
      String message = String.format(format, repetitions, modification, bucketsString, MINIMUM_WIDTH);
      throw new IllegalArgumentException(message);
    }
    int i = 0;
    while (repetitions > 0) {
      if (buckets.get(i) + modification >= MINIMUM_WIDTH) {
        buckets.set(i, buckets.get(i) + modification);
        repetitions--;
      }
      i = (i + 1) % buckets.size();
    }
  }

  /**
   * Appends a row to a DungeonString.
   */
  private void appendRow(DungeonString builder, boolean header, List<Integer> widths, String... values) {
    for (int i = 0; i < values.length; i++) {
      int columnWidth = widths.get(i);
      String currentValue = values[i];
      if (currentValue.length() > columnWidth) {
        if (columnWidth < 4) { // This is how spreadsheet editors seem to handle it.
          builder.append(makeRepeatedCharacterString(columnWidth, '#'));
        } else {
          builder.append(currentValue.substring(0, columnWidth - 3));
          builder.append("...");
        }
      } else {
        // Headers are always left-aligned.
        ColumnAlignment alignment = header ? ColumnAlignment.LEFT : columnAlignments.get(i);
        int extraSpaces = columnWidth - currentValue.length();
        if (alignment == ColumnAlignment.LEFT) {
          builder.append(currentValue);
          builder.append(makeRepeatedCharacterString(extraSpaces, ' '));
        } else if (alignment == ColumnAlignment.RIGHT) {
          builder.append(makeRepeatedCharacterString(extraSpaces, ' '));
          builder.append(currentValue);
        } else {
          throw new IllegalArgumentException("Cannot handle ColumnAlignment: " + alignment.toString());
        }
      }
      if (i < values.length - 1) {
        builder.append(VERTICAL_BAR);
      }
    }
    builder.append("\n");
  }

  /**
   * Append a horizontal separator made up of dashes to a DungeonString.
   */
  private void appendHorizontalSeparator(DungeonString builder, List<Integer> columnWidths, int columnCount) {
    String[] pseudoRow = new String[columnCount];
    for (int i = 0; i < columnWidths.size(); i++) {
      pseudoRow[i] = makeRepeatedCharacterString(columnWidths.get(i), HORIZONTAL_BAR);
    }
    appendRow(builder, false, columnWidths, pseudoRow);
  }

  /**
   * Defines how each column should be aligned.
   */
  public void setColumnAlignments(List<ColumnAlignment> columnAlignments) {
    if (columnAlignments.size() != columns.size()) {
      String expectedButGotString = "Expected " + columns.size() + ", but got " + columnAlignments.size();
      throw new IllegalArgumentException(expectedButGotString);
    }
    this.columnAlignments.clear();
    this.columnAlignments.addAll(columnAlignments);
  }

  /**
   * Inserts a row of values at the end of the table. The number of provided values should equal the number of columns.
   *
   * @param values the values to be inserted
   */
  public void insertRow(String... values) {
    if (values.length != columns.size()) {
      String expectedButGotString = "Expected " + columns.size() + ", but got " + values.length;
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

  private List<Integer> calculateColumnWidths() throws IllegalArgumentException {
    List<Integer> widths = getMaximumColumnWidths();
    int availableWidth = getAvailableWidth();
    int difference = availableWidth - DungeonMath.sum(widths);
    distribute(difference, widths);
    return widths;
  }

  private int getAvailableWidth() {
    // Subtract the number of columns to account for separators. Add one because there is not a separator at the end.
    return GameWindow.getColumns() - columns.size() + 1;
  }

  private List<Integer> getMaximumColumnWidths() {
    List<Integer> widths = new ArrayList<>();
    for (Column column : columns) {
      widths.add(column.widestValue);
    }
    return widths;
  }

  @Override
  public List<ColoredString> toColoredStringList() {
    DungeonString string = new DungeonString();
    List<Integer> columnWidths;
    try {
      // You likely don't want toColoredStringList to be throwing exceptions, so catch them early.
      columnWidths = calculateColumnWidths();
    } catch (RuntimeException log) {
      DungeonLogger.warning(log.getMessage());
      string.append("Failed to generate a visual representation of the table.");
      return string.toColoredStringList();
    }
    String[] currentRow = new String[columns.size()];
    // Insert headers
    for (int i = 0; i < columns.size(); i++) {
      currentRow[i] = columns.get(i).header;
    }
    appendRow(string, true, columnWidths, currentRow);
    // A horizontal separator.
    appendHorizontalSeparator(string, columnWidths, columns.size());
    int rowCount = columns.get(0).rows.size();
    // Insert table body.
    for (int rowIndex = 0; rowIndex < rowCount + 1; rowIndex++) {
      if (separators != null) {
        for (int remaining = separators.getCounter(rowIndex); remaining > 0; remaining--) {
          appendHorizontalSeparator(string, columnWidths, columns.size());
        }
      }
      if (rowIndex != rowCount) {
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
          currentRow[columnIndex] = columns.get(columnIndex).rows.get(rowIndex);
        }
        appendRow(string, false, columnWidths, currentRow);
      }
    }
    return string.toColoredStringList();
  }

  private static class Column {
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
