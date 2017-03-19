package org.mafagafogigante.dungeon.date;

import org.mafagafogigante.dungeon.util.DungeonMath;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Represents a nonnegative amount of time.
 */
public class Duration implements Comparable<Duration>, Serializable {

  /**
   * The duration, in milliseconds.
   */
  private final long duration;

  /**
   * Constructs a Duration from a specified start date to an end date.
   *
   * @param start the starting instance
   * @param end the ending instance
   */
  public Duration(@NotNull Date start, @NotNull Date end) {
    long difference = end.getTime() - start.getTime();
    if (difference >= 0) {
      duration = difference;
    } else {
      throw new IllegalArgumentException("End is before start");
    }
  }

  /**
   * Constructs a Duration from the specified duration, in milliseconds.
   *
   * @param duration the duration, in milliseconds
   */
  Duration(long duration) {
    this.duration = duration;
  }

  public long getSeconds() {
    return duration / 1000;
  }

  @Override
  public int compareTo(@NotNull Duration duration) {
    return Long.compare(this.duration, duration.duration);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    Duration duration = (Duration) object;

    return this.duration == duration.duration;
  }

  @Override
  public int hashCode() {
    return (int) (duration ^ (duration >>> 32));
  }

  @Override
  public String toString() {
    if (duration < DungeonTimeUnit.DAY.milliseconds) {
      return "Less than a day";
    }
    TimeStringBuilder builder = new TimeStringBuilder();
    int years = DungeonMath.safeCastLongToInteger(duration / DungeonTimeUnit.YEAR.milliseconds);
    long monthsLong = (duration % DungeonTimeUnit.YEAR.milliseconds) / DungeonTimeUnit.MONTH.milliseconds;
    int months = DungeonMath.safeCastLongToInteger(monthsLong);
    long daysLong = (duration % DungeonTimeUnit.MONTH.milliseconds) / DungeonTimeUnit.DAY.milliseconds;
    int days = DungeonMath.safeCastLongToInteger(daysLong);
    builder.set(DungeonTimeUnit.YEAR, years);
    builder.set(DungeonTimeUnit.MONTH, months);
    builder.set(DungeonTimeUnit.DAY, days);
    return builder.toString();
  }

}
