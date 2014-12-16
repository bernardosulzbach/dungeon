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

import org.dungeon.io.DLogger;

import java.io.Serializable;

/**
 * ID class that encapsulates the ID field.
 * <p/>
 * Created by Bernardo Sulzbach on 15/12/14.
 */
public final class ID implements Serializable {

  private final String id;

  public ID(String id) {
    if (id == null) {
      DLogger.warning("Tried to use a null String as an ID.");
      this.id = "null";
      return;
    }
    boolean invalid = false;
    char[] idChars = id.toCharArray();
    char currentChar;
    for (int i = 0; i < idChars.length; i++) {
      currentChar = idChars[i];
      if (Character.isLetter(currentChar)) {
        if (Character.isLowerCase(idChars[i])) {
          invalid = true;
          idChars[i] = Character.toUpperCase(currentChar);
        }
      } else if (!(Character.isDigit(currentChar) || idChars[i] == '_')) {
        invalid = true;
        idChars[i] = '_';
      }
    }
    if (invalid) {
      DLogger.warning("Tried to use \"" + id + "\" as an ID.");
    }
    this.id = new String(idChars);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ID oid = (ID) o;

    return id.equals(oid.id);

  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public String getId() {
    return id;
  }

}
