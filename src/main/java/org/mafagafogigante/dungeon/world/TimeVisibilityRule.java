package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.game.World;

import java.io.Serializable;

public class TimeVisibilityRule implements Serializable, VisibilityRule {

  private static final int HOURS_IN_DAY = 24;

  private final int begin;
  private final int duration;

  /**
   * Constructs a new TimeVisibilityRule that starts at begin and ends at end.
   *
   * @param begin the hour when it begins, nonnegative, smaller than the number of hours in the day
   * @param end the hour when it ends, nonnegative, smaller than the number of hours in the day, not equal to begin
   */
  public TimeVisibilityRule(final int begin, final int end) {
    if (begin == end) {
      throw new IllegalArgumentException("begin should not be equal to end");
    }
    if (end >= HOURS_IN_DAY) {
      throw new IllegalArgumentException("end should be a valid hour");
    }
    this.begin = begin;
    this.duration = Math.abs(end - begin);
  }

  @Override
  public boolean isRespected(World world) {
    final long hour = world.getWorldDate().getHour();
    long delta = hour - begin;
    if (delta < 0) {
      delta += HOURS_IN_DAY;
    }
    return delta < duration;
  }

}
