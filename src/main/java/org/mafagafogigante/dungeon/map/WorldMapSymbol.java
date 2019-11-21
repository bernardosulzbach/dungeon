package org.mafagafogigante.dungeon.map;

import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.LocationDescription;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Objects;

/**
 * A symbol in a WorldMap.
 */
class WorldMapSymbol {

  private static final WorldMapSymbol HERO_SYMBOL = new WorldMapSymbol("You", '@', Color.WHITE);
  private static final WorldMapSymbol NOT_YET_GENERATED_SYMBOL = new WorldMapSymbol("Unknown", '~', Color.GRAY);

  private final String name;
  private final String character;
  private final Color color;

  private WorldMapSymbol(String name, char character, @NotNull Color color) {
    this.name = name;
    this.character = String.valueOf(character);
    this.color = color;
  }

  public static WorldMapSymbol makeSymbol(@NotNull Location location) {
    String singular = location.getName().getSingular();
    LocationDescription description = location.getDescription();
    final char symbol = description.getSymbol();
    Color color = description.getColor();
    return new WorldMapSymbol(singular, symbol, color);
  }

  public static WorldMapSymbol getHeroSymbol() {
    return HERO_SYMBOL;
  }

  public static WorldMapSymbol getNotYetGeneratedSymbol() {
    return NOT_YET_GENERATED_SYMBOL;
  }

  public String getName() {
    return name;
  }

  public String getCharacterAsString() {
    return character;
  }

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
    WorldMapSymbol that = (WorldMapSymbol) o;
    final boolean nameEquals = Objects.equals(name, that.name);
    final boolean characterEquals = Objects.equals(character, that.character);
    final boolean colorEquals = Objects.equals(color, that.color);
    return nameEquals && characterEquals && colorEquals;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, character, color);
  }

  @Override
  public String toString() {
    return "WorldMapSymbol{" +
            "name='" + name + '\'' +
            ", character='" + character + '\'' +
            ", color=" + color +
            '}';
  }

}
