package org.mafagafogigante.dungeon.entity.items;

import java.awt.Color;

public enum Rarity {

  POOR("Poor", Palette.POOR), COMMON("Common", Palette.COMMON), UNCOMMON("Uncommon", Palette.UNCOMMON),
  RARE("Rare", Palette.RARE), LEGENDARY("Legendary", Palette.LEGENDARY), UNIQUE("Unique", Palette.UNIQUE);

  private final String name;
  private final Color color;

  Rarity(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  private static class Palette {
    static final Color POOR = new Color(112, 112, 112);
    static final Color COMMON = new Color(192, 192, 192);
    static final Color UNCOMMON = new Color(30, 255, 0);
    static final Color RARE = new Color(0, 112, 255);
    static final Color LEGENDARY = new Color(163, 53, 238);
    static final Color UNIQUE = new Color(255, 128, 0);
  }

}
