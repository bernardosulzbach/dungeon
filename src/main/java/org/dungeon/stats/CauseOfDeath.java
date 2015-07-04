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

package org.dungeon.stats;

import org.dungeon.game.ID;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * CauseOfDeath class that defines what kind of death happened and the ID of the related Item or Skill.
 */
public class CauseOfDeath implements Serializable {

  private final TypeOfCauseOfDeath type;
  private final ID id;

  /**
   * Gets a CauseOfDeath with the specified TypeOfCauseOfDeath and ID.
   *
   * @param type a TypeOfCauseOfDeath
   * @param id   an ID
   */
  public CauseOfDeath(@NotNull TypeOfCauseOfDeath type, @NotNull ID id) {
    this.type = type;
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CauseOfDeath that = (CauseOfDeath) o;

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
