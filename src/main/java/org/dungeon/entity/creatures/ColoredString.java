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

package org.dungeon.entity.creatures;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * A wrapper for a String and a Color.
 */
final class ColoredString {

  private final String string;
  private final Color color;

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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ColoredString that = (ColoredString) o;
    return string.equals(that.string) && color.equals(that.color);
  }

  @Override
  public int hashCode() {
    return 31 * string.hashCode() + (color.hashCode());
  }

  @Override
  public String toString() {
    return String.format("'%s' written in %s.", string, color);
  }

}
