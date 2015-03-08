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

import java.io.Serializable;

/**
 * Name Final class that stores a singular and a pluralized name for an Entity.
 * <p/>
 * Created by Bernardo on 02/03/2015.
 */
public final class Name implements Serializable {

  private final String singular;
  private final String plural;

  /**
   * Constructs a name based on the specified forms.
   *
   * @param singular the singular form
   * @param plural   the plural form
   */
  private Name(String singular, String plural) {
    this.singular = singular;
    this.plural = plural;
  }

  /**
   * Creates a new Name from a singular form.
   *
   * @param singular the singular form
   * @return a Name constructed using the provided singular form and this form concatenated with an 's'
   */
  public static Name newInstance(String singular) {
    return newInstance(singular, singular + 's');
  }

  /**
   * Creates a new Name from a singular and a plural form.
   *
   * @param singular the singular form
   * @param plural   the plural form
   * @return a Name constructed using the provided singular and plural forms
   */
  public static Name newInstance(String singular, String plural) {
    return new Name(singular, plural);
  }

  public String getName() {
    return singular;
  }

  /**
   * Returns a String representing a quantified name.
   * <p/>
   * {@code e.g.: "Two Bears", "8 Tigers", "One Elephant"}
   *
   * @param quantity the quantity, must be positive
   * @param mode     a QuantificationMode constant
   * @return a String
   */
  public String getQuantifiedName(int quantity, QuantificationMode mode) {
    String name = null;
    if (quantity < 0) {
      DLogger.warning("Called getQuantifiedName with nonpositive quantity.");
    } else if (quantity == 1) {
      name = singular;
    } else {
      name = plural;
    }
    String number;
    if (mode == QuantificationMode.NUMBER) {
      number = String.valueOf(quantity);
    } else {
      number = Numeral.getCorrespondingNumeral(quantity).toString().toLowerCase();
    }
    return number + " " + name;
  }

}
