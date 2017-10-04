package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.stats.BattleRecord;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;

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
