package org.mafagafogigante.dungeon.stats;

/**
 * TypeOfCauseOfDeath enumerated type that defines the possible types of causes of death.
 */
public enum TypeOfCauseOfDeath {

  UNARMED("Unarmed"), WEAPON("Weapon"), BROKEN_WEAPON("Broken Weapon"), SPELL("Spell");

  private final String stringRepresentation;

  TypeOfCauseOfDeath(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
