package org.mafagafogigante.dungeon.date;

/**
 * The time units of the Dungeon calendar.
 */
public enum DungeonTimeUnit {

  MILLISECOND(1), SECOND(1000 * MILLISECOND.milliseconds), MINUTE(60 * SECOND.milliseconds),
  HOUR(60 * MINUTE.milliseconds), DAY(24 * HOUR.milliseconds), MONTH(10 * DAY.milliseconds),
  YEAR(10 * MONTH.milliseconds);

  public final long milliseconds;

  DungeonTimeUnit(long milliseconds) {
    this.milliseconds = milliseconds;
  }

  /**
   * Returns how many of the specified unit are equivalent to one of this unit.
   *
   * @param unit a DungeonTimeUnit that is smaller than this one, not null
   * @return a long bigger than one
   */
  public long as(DungeonTimeUnit unit) {
    if (this.milliseconds <= unit.milliseconds) {
      throw new IllegalArgumentException("unit is bigger than or equal to the caller.");
    } else {
      return this.milliseconds / unit.milliseconds;
    }
  }

}
