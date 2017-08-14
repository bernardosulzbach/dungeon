package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.io.Version;

public enum DamageType {

  SLASHING("Slashing"), PIERCING("Piercing"), BLUDGEONING("Bludgeoning"), CRUSHING("Crushing"), ACID("Acid"),
  COLD("Cold"), ELECTRICITY("Electricity"), FIRE("Fire"), POISON("Poison"), SONIC("Sonic"), DIVINE("Divine"),
  MAGIC("Magic"), NEGATIVE("Negative"), Positive("Positive");

  private static final long serialVersionUID = Version.MAJOR;
  private final String name;

  DamageType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
