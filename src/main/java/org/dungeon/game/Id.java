/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

import java.io.Serializable;

/**
 * Id class that wraps an identification String.
 *
 * <p>The wrapped String is guaranteed to only contain valid characters.
 *
 * <p>Valid characters are: uppercase ASCII letters, digits, and the underscore.
 */
public final class Id implements Serializable {

  private final String id;

  /**
   * Constructs an Id from a String.
   *
   * <p>Valid characters are: uppercase ASCII letters, digits, and the underscore.
   *
   * @param id the Id String
   */
  public Id(String id) {
    if (id == null) {
      throw new IllegalArgumentException("tried to create an Id with null.");
    } else if (id.isEmpty()) {
      throw new IllegalArgumentException("tried to create an Id with the empty string.");
    } else {
      for (char character : id.toCharArray()) {
        if (isInvalidCharacter(character)) {
          throw new IllegalArgumentException("got invalid Id string: " + id);
        }
      }
      this.id = id;
    }
  }

  /**
   * Check if the specified character is an invalid Id character.
   *
   * <p>Valid characters are: uppercase ASCII letters, digits, and the underscore.
   *
   * @param character a candidate character
   * @return true if the character cannot be used in an Id string, false otherwise
   */
  private static boolean isInvalidCharacter(char character) {
    if (Character.isLetter(character)) {
      if (Character.isUpperCase(character)) {
        return false;
      }
    }
    return !Character.isDigit(character) && character != '_';
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    Id oid = (Id) object;

    return id.equals(oid.id);

  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return id;
  }

}
