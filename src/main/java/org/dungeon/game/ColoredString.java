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

/**
 * A colored string, this is, a pair of a string and a color.
 */
public class ColoredString {

  private final String string;
  private final Color color;

  public ColoredString(@NotNull String string) {
    this(string, Constants.FORE_COLOR_NORMAL);
  }

  public ColoredString(@NotNull String string, @NotNull Color color) {
    this.string = string;
    this.color = color;
  }

  @NotNull
  public String getString() {
    return string;
  }

  @NotNull
  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "ColoredString{" +
        "string='" + string + '\'' +
        ", color=" + color +
        '}';
  }

}
