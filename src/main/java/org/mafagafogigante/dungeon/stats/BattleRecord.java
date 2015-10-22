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
import org.mafagafogigante.dungeon.game.PartOfDay;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A record of the output of a battle.
 */
public final class BattleRecord implements Serializable {

  private final Id id;
  private final String type;
  private final CauseOfDeath causeOfDeath;
  private final PartOfDay partOfDay;

  /**
   * Constructs a BattleRecord from the provided data.
   */
  public BattleRecord(@NotNull Id id, @NotNull String type, @NotNull CauseOfDeath causeOfDeath,
      @NotNull PartOfDay partOfDay) {
    this.id = id;
    this.type = type;
    this.causeOfDeath = causeOfDeath;
    this.partOfDay = partOfDay;
  }

  public Id getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public CauseOfDeath getCauseOfDeath() {
    return causeOfDeath;
  }

  public PartOfDay getPartOfDay() {
    return partOfDay;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    BattleRecord that = (BattleRecord) object;

    return id.equals(that.id) && type.equals(that.type) && causeOfDeath.equals(that.causeOfDeath) &&
        partOfDay == that.partOfDay;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + causeOfDeath.hashCode();
    result = 31 * result + partOfDay.hashCode();
    return result;
  }

  @Override
  public String toString() {
    String format = "BattleEntry{id=%s, type='%s', causeOfDeath=%s, partOfDay=%s}";
    return String.format(format, id, type, causeOfDeath, partOfDay);
  }

}
