package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import java.util.Locale;

public abstract class Name {

  /**
   * Returns the singular form of this Name.
   */
  public abstract String getSingular();

  /**
   * Returns the plural form of this Name.
   */
  public abstract String getPlural();

  /**
   * Returns a string representing a quantified name using words for quantifiers. e.g.: {@code "One Sword", "Two Bears",
   * "Three Elephants", "A few Cows"}
   *
   * @param quantity the quantity, must be positive
   * @return a String
   */
  public String getQuantifiedName(int quantity) {
    return getQuantifiedName(quantity, QuantificationMode.WORD);
  }

  /**
   * Returns a string representing a quantified name using the specified quantification mode.
   *
   * @param quantity the quantity, must be positive
   * @param mode a QuantificationMode constant
   * @return a String
   */
  public String getQuantifiedName(int quantity, QuantificationMode mode) {
    String name;
    if (quantity < 0) {
      DungeonLogger.warning("Called getQuantifiedName with nonpositive quantity.");
      throw new AssertionError("Negative quantity passed to getQuantifiedName()!");
    } else if (quantity == 1) {
      name = getSingular();
    } else {
      name = getPlural();
    }
    String number;
    if (mode == QuantificationMode.NUMBER) {
      number = String.valueOf(quantity);
    } else {
      Numeral correspondingNumeral = Numeral.getCorrespondingNumeral(quantity);
      if (correspondingNumeral == null) {
        throw new AssertionError("Numeral.getCorrespondingNumeral() returned null!");
      } else {
        number = correspondingNumeral.toString().toLowerCase(Locale.ENGLISH);
      }
    }
    return number + " " + name;
  }

}
