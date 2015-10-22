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

package org.mafagafogigante.dungeon.stats;

import java.io.Serializable;

/**
 * A numeric Record that represents either a minimum or a maximum.
 */
class Record implements Serializable {

  private final Type type;
  private Integer value; // Use the boxed type to start with null.

  /**
   * Creates a Record of the specified Type.
   *
   * @param type a Type value
   */
  public Record(Type type) {
    this.type = type;
  }

  /**
   * Updates the Record by presenting it a new value. If it is better than the current value, or if there is not an
   * established record, it becomes the new record.
   *
   * @param value a numeric value
   */
  public void update(int value) {
    if (this.value == null) {
      this.value = value;
    } else {
      if (type == Type.MAXIMUM) {
        this.value = Math.max(this.value, value);
      } else if (type == Type.MINIMUM) {
        this.value = Math.min(this.value, value);
      }
    }
  }

  /**
   * Returns the current record, or null if there is not an established record.
   *
   * @return an Integer, or null
   */
  public Integer getValue() {
    return value;
  }

  /**
   * Returns a String representation of this Record. If the record was not established, {@code "N/A"} is returned.
   *
   * @return a String representation of this Record
   */
  @Override
  public String toString() {
    if (getValue() == null) {
      return "N/A";
    } else {
      return getValue().toString();
    }
  }

  public enum Type {MAXIMUM, MINIMUM}

}
