package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

/**
 * Numeral enumerated type.
 */
public enum Numeral {

  ONE("One"), TWO("Two"), THREE("Three"), FOUR("Four"), FIVE("Five"), MORE_THAN_FIVE("A few");

  final String stringRepresentation;

  Numeral(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  /**
   * Returns a corresponding Numeral of an integer or null if there is not such Numeral.
   */
  public static Numeral getCorrespondingNumeral(int integer) {
    if (integer < 1) {
      DungeonLogger.warning("Tried to get nonpositive numeral.");
      return null;
    } else if (integer >= values().length) {
      return values()[values().length - 1];
    } else {
      return values()[integer - 1];
    }
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
