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

import org.mafagafogigante.dungeon.game.Id;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * CauseOfDeath class that defines what kind of death happened and the ID of the related Item or Spell.
 */
public class CauseOfDeath implements Serializable {

  private static final CauseOfDeath UNARMED = new CauseOfDeath(TypeOfCauseOfDeath.UNARMED, new Id("UNARMED"));
  private final TypeOfCauseOfDeath type;
  private final Id id;

  /**
   * Constructs a CauseOfDeath with the specified TypeOfCauseOfDeath and ID.
   *
   * @param type a TypeOfCauseOfDeath
   * @param id an ID
   */
  public CauseOfDeath(@NotNull TypeOfCauseOfDeath type, @NotNull Id id) {
    this.type = type;
    this.id = id;
  }

  /**
   * Convenience method that returns a CauseOfDeath that represents an unarmed kill.
   */
  public static CauseOfDeath getUnarmedCauseOfDeath() {
    return UNARMED;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    CauseOfDeath that = (CauseOfDeath) object;

    return id.equals(that.id) && type == that.type;
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + id.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s : %s", type, id);
  }

}
