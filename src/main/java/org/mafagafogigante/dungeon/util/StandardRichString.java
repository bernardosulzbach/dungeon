package org.mafagafogigante.dungeon.util;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class StandardRichString implements RichString {

  private final String string;
  private final Color color;

  public StandardRichString(@NotNull String string, @NotNull Color color) {
    this.string = string;
    this.color = color;
  }

  @Override
  @NotNull
  public String getString() {
    return string;
  }

  @Override
  @NotNull
  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return string;
  }

}
