package org.mafagafogigante.dungeon.entity.creatures;

/**
 * HealthState enum that defines the six stages of healthiness.
 */
public enum HealthState {

  UNINJURED("Uninjured"),
  BARELY_INJURED("Barely Injured"),
  INJURED("Injured"),
  BADLY_INJURED("Badly Injured"),
  NEAR_DEATH("Near Death"),
  DEAD("Dead");

  private final String stringRepresentation;

  HealthState(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
