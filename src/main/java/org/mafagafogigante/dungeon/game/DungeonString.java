/*
 * Copyright (C) 2015 Bernardo Sulzbach
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
public final class DungeonString implements Writable {

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

  /**
   * Converts the text of this DungeonString to a plain Java String.
   */
  public String toJavaString() {
    StringBuilder builder = new StringBuilder();
    for (ColoredString coloredString : toColoredStringList()) {
      builder.append(coloredString.getString());
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return "DungeonString{" +
        "coloredStringList=" + toColoredStringList() +
        ", currentColor=" + currentColor +
        '}';
  }

}
