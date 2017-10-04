package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The preferred way to represent multicolored text in Dungeon.
 *
 * <p>By calling setColor and append multiple times it is possible to generate long, multicolored strings.
 */
public final class DungeonString extends Writable {

  private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;

  /**
   * A list of ColoredStrings. No string from this list is empty. Adjacent strings may have the same color.
   *
   * <p>Should only be accessed through the getter (except for addBuilderContentToList).
   */
  private final List<ColoredString> coloredStringList = new ArrayList<>();
  private final StringBuilder builder = new StringBuilder();
  private Color currentColor = DEFAULT_COLOR;

  /**
   * Constructs an empty DungeonString.
   */
  public DungeonString() {
  }

  /**
   * Constructs a DungeonString that starts with the specified text.
   */
  public DungeonString(String text) {
    append(text);
  }

  /**
   * Constructs a DungeonString that starts with the specified text and color.
   */
  public DungeonString(String text, Color color) {
    setColor(color);
    append(text);
    resetColor();
  }

  /**
   * Returns the total length of the string.
   */
  public int getLength() {
    int sum = 0;
    for (ColoredString coloredString : coloredStringList) {
      sum += coloredString.getString().length();
    }
    sum += builder.length();
    return sum;
  }

  /**
   * Returns an unmodifiable list of ColoredStrings that are equivalent to the contents of this DungeonString.
   *
   * @return an unmodifiable list of ColoredStrings
   */
  public List<ColoredString> toColoredStringList() {
    addBuilderContentToList();
    return Collections.unmodifiableList(coloredStringList);
  }

  private void addBuilderContentToList() {
    if (builder.length() != 0) {
      coloredStringList.add(new ColoredString(builder.toString(), currentColor));
      builder.setLength(0);
    }
  }

  /**
   * Appends one or more strings to this DungeonString.
   *
   * <p>Logs a warning if called with an empty array.
   *
   * @param strings one or more Strings
   */
  public void append(@NotNull String... strings) {
    if (strings.length == 0) {
      DungeonLogger.warning("Called DungeonString.append with empty vararg");
    }
    for (String string : strings) {
      builder.append(string);
    }
  }

  /**
   * Changes the current color of this DungeonString. This will only impact future calls to <code>append</code>.
   *
   * <p>Passing the current color of this DungeonString is a no-op.
   *
   * @param color a Color object
   */
  public void setColor(@NotNull Color color) {
    if (currentColor != color) {
      addBuilderContentToList();
      currentColor = color;
    }
  }

  /**
   * Resets the color of this DungeonString to the default color.
   */
  public void resetColor() {
    setColor(DEFAULT_COLOR);
  }

  @Override
  public String toString() {
    return "DungeonString{" +
        "coloredStringList=" + toColoredStringList() +
        ", currentColor=" + currentColor +
        '}';
  }

}
