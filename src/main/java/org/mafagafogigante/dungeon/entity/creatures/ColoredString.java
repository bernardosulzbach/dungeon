package org.mafagafogigante.dungeon.entity.creatures;

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
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    ColoredString that = (ColoredString) object;
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
