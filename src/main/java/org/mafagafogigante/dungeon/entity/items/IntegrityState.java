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
