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

package org.dungeon.achievements;

import org.dungeon.game.Id;
import org.dungeon.game.PartOfDay;
import org.dungeon.stats.BattleRecord;
import org.dungeon.stats.CauseOfDeath;

import org.jetbrains.annotations.NotNull;

public class BattleStatisticsQuery {

  private Id id;
  private String type;
  private CauseOfDeath causeOfDeath;
  private PartOfDay partOfDay;

  public BattleStatisticsQuery() {
  }

  public void setId(Id id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setCauseOfDeath(CauseOfDeath causeOfDeath) {
    this.causeOfDeath = causeOfDeath;
  }

  public void setPartOfDay(PartOfDay partOfDay) {
    this.partOfDay = partOfDay;
  }

  /**
   * Returns whether or not a given BattleRecord matches this query.
   */
  public boolean matches(@NotNull BattleRecord record) {
    return (id == null || id.equals(record.getId())) && (type == null || type.equals(record.getType())) &&
        (causeOfDeath == null || causeOfDeath.equals(record.getCauseOfDeath())) &&
        (partOfDay == null || partOfDay == record.getPartOfDay());
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    BattleStatisticsQuery that = (BattleStatisticsQuery) object;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }
    if (causeOfDeath != null ? !causeOfDeath.equals(that.causeOfDeath) : that.causeOfDeath != null) {
      return false;
    }
    return partOfDay == that.partOfDay;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (causeOfDeath != null ? causeOfDeath.hashCode() : 0);
    result = 31 * result + (partOfDay != null ? partOfDay.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    String format = "BattleStatisticsQuery{id=%s, type='%s', causeOfDeath=%s, partOfDay=%s}";
    return String.format(format, id, type, causeOfDeath, partOfDay);
  }

}
