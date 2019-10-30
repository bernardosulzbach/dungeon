package org.mafagafogigante.dungeon.game;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class RichString {

  private final String string;
  private final Color color;

  public RichString(@NotNull String string, @NotNull Color color) {
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
    return "RichString{" + "string='" + string + '\'' + ", color=" + color + '}';
  }

}
