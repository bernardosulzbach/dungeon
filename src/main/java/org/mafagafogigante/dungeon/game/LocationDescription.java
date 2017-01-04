package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.io.Serializable;

/**
 * The description of a Location object.
 */
public class LocationDescription implements Examinable, Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final char symbol;
  private final Color color;
  private String info = "You don't discover anything.";

  public LocationDescription(char symbol, Color color) {
    this.symbol = symbol;
    this.color = color;
  }

  public char getSymbol() {
    return symbol;
  }

  public Color getColor() {
    return color;
  }

  @NotNull
  @Override
  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  @Override
  public String toString() {
    return "LocationDescription{" +
        "symbol=" + symbol +
        ", color=" + color +
        ", info='" + info + '\'' +
        '}';
  }

}
