package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.entity.creatures.Observer;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

public class TimeVisibilityCriterion implements Serializable, VisibilityCriterion {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int HOURS_IN_DAY = 24;

  private final int begin;
  private final int duration;

  /**
   * Constructs a new TimeVisibilityCriterion that starts at begin and ends at end.
   *
   * @param begin the hour when it begins, nonnegative, smaller than the number of hours in the day
   * @param end the hour when it ends, nonnegative, smaller than the number of hours in the day, not equal to begin
   */
  public TimeVisibilityCriterion(final int begin, final int end) {
    if (end >= HOURS_IN_DAY) {
      throw new IllegalArgumentException("end should be a valid hour");
    }
    this.begin = begin;
    if (begin == end) {
      throw new IllegalArgumentException("begin should not be equal to end");
    } else if (begin < end) {
      this.duration = end - begin;
    } else {
      this.duration = HOURS_IN_DAY + end - begin;
    }
  }

  @Override
  public boolean isMetBy(Observer observer) {
    final long hour = observer.getObserverLocation().getWorld().getWorldDate().getHour();
    long delta = hour - begin;
    if (delta < 0) {
      delta += HOURS_IN_DAY;
    }
    return delta < duration;
  }

}
