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

package org.dungeon.game;

import org.dungeon.util.Constants;

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
public final class DungeonStringBuilder implements Writable {

  /**
   * A list of ColoredStrings. No string from this list is empty. Adjacent strings may have the same color.
   *
   * <p>Should only be accessed through the getter (except for addBuilderContentToList).
   */
  private final List<ColoredString> coloredStringList = new ArrayList<ColoredString>();
  private final StringBuilder builder = new StringBuilder();
  private Color currentColor = Constants.FORE_COLOR_NORMAL;

  /**
   * Returns an unmodifiable list of ColoredStrings that are equivalent to the contents of this builder.
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
   * Appends a string to this builder.
   *
   * @param string a String object
   */
  public void append(@NotNull String string) {
    builder.append(string);
  }

  /**
   * Changes the current color of this builder. This will only impact future calls to append.
   *
   * <p>Passing the current color of the builder to this method does no harm.
   *
   * @param color a Color object
   */
  public void setColor(@NotNull Color color) {
    if (currentColor != color) {
      addBuilderContentToList();
      currentColor = color;
    }
  }

  @Override
  public String toString() {
    return "DungeonStringBuilder{" +
        "coloredStringList=" + toColoredStringList() +
        ", currentColor=" + currentColor +
        '}';
  }

}
