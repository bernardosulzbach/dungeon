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

package org.dungeon.game;

import org.dungeon.io.DLogger;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Name immutable class that stores the singular and plural forms of a name.
 */
public final class Name implements Serializable {

  private final String singular;
  private final String plural;

  /**
   * Constructs a name based on the specified forms.
   * Names should be created through NameFactory and not through this constructor.
   *
   * @param singular the singular form, not null
   * @param plural   the plural form, not null
   */
  Name(@NotNull String singular, @NotNull String plural) {
    this.singular = singular;
    this.plural = plural;
  }

  /**
   * Returns the singular form of this Name.
   */
  public String getSingular() {
    return singular;
  }

  /**
   * Returns a string representing a quantified name using words for quantifiers.
   * e.g.: {@code "One Sword", "Two Bears", "Three Elephants", "A few Cows"}
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
   * @param mode     a QuantificationMode constant
   * @return a String
   */
  public String getQuantifiedName(int quantity, QuantificationMode mode) {
    String name;
    if (quantity < 0) {
      DLogger.warning("Called getQuantifiedName with nonpositive quantity.");
      throw new AssertionError("Negative quantity passed to getQuantifiedName()!");
    } else if (quantity == 1) {
      name = singular;
    } else {
      name = plural;
    }
    String number;
    if (mode == QuantificationMode.NUMBER) {
      number = String.valueOf(quantity);
    } else {
      Numeral correspondingNumeral = Numeral.getCorrespondingNumeral(quantity);
      if (correspondingNumeral == null) {
        throw new AssertionError("Numeral.getCorrespondingNumeral() returned null!");
      } else {
        number = correspondingNumeral.toString().toLowerCase();
      }
    }
    return number + " " + name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Name name = (Name) o;

    return singular.equals(name.singular) && plural.equals(name.plural);
  }

  @Override
  public int hashCode() {
    int result = singular.hashCode();
    result = 31 * result + plural.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return getSingular();
  }

}
