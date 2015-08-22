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

package org.dungeon.map;

import org.dungeon.game.Location;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * A symbol in a WorldMap.
 */
class WorldMapSymbol {

  private static final WorldMapSymbol HERO_SYMBOL = new WorldMapSymbol('@', Color.WHITE);
  private static final WorldMapSymbol NOT_YET_GENERATED_SYMBOL = new WorldMapSymbol('~', Color.GRAY);

  private final String character;
  private final Color color;

  private WorldMapSymbol(char character, @NotNull Color color) {
    this.character = String.valueOf(character);
    this.color = color;
  }

  public static WorldMapSymbol makeSymbol(@NotNull Location location) {
    return new WorldMapSymbol(location.getDescription().getSymbol(), location.getDescription().getColor());
  }

  public static WorldMapSymbol getHeroSymbol() {
    return HERO_SYMBOL;
  }

  public static WorldMapSymbol getNotYetGeneratedSymbol() {
    return NOT_YET_GENERATED_SYMBOL;
  }

  public String getCharacterAsString() {
    return character;
  }

  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "WorldMapSymbol{" +
        "character='" + character + '\'' +
        ", color=" + color +
        '}';
  }

}
