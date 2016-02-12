package org.mafagafogigante.dungeon.entity.items;

/**
 * IntegrityState enumerated type that defines some constants to humanize the representation of the state of an Item.
 */
public enum IntegrityState {

  PERFECT(""),
  SLIGHTLY_DAMAGED("Slightly Damaged"),
  DAMAGED("Damaged"),
  SEVERELY_DAMAGED("Severely Damaged"),
  BROKEN("Broken");

  private final String stringRepresentation;

  IntegrityState(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  /**
   * Returns the IntegrityState that corresponds to the specified fraction.
   */
  public static IntegrityState getIntegrityState(int curIntegrity, int maxIntegrity) {
    if (curIntegrity > maxIntegrity) {
      throw new IllegalArgumentException("curIntegrity is greater than maxIntegrity.");
    } else if (curIntegrity == maxIntegrity) {
      return PERFECT;
    } else if (curIntegrity >= maxIntegrity * 0.65) {
      return SLIGHTLY_DAMAGED;
    } else if (curIntegrity >= maxIntegrity * 0.3) {
      return DAMAGED;
    } else if (curIntegrity > 0) {
      return SEVERELY_DAMAGED;
    } else {
      return BROKEN;
    }
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
