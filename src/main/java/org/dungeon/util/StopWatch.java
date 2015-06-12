/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.dungeon.util;

import org.dungeon.io.DLogger;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A StopWatch class used to measure time distance between two or more instants.
 * <p/>
 * The precision of the measurements performed with a StopWatch are dependent on System.nanoTime() precision.
 */
public class StopWatch {

  private static final Map<TimeUnit, String> ABBREVIATIONS = new EnumMap<TimeUnit, String>(TimeUnit.class);

  static {
    ABBREVIATIONS.put(TimeUnit.NANOSECONDS, "ns");
    ABBREVIATIONS.put(TimeUnit.MICROSECONDS, "μs");
    ABBREVIATIONS.put(TimeUnit.MILLISECONDS, "ms");
    ABBREVIATIONS.put(TimeUnit.SECONDS, "s");
  }

  private final long time;

  public StopWatch() {
    time = System.nanoTime();
  }

  private long calculateTimeDifference() {
    return System.nanoTime() - time;
  }

  /**
   * Returns a String representation of the time difference between this method call and the creation of this StopWatch.
   * If the provided TimeUnit is not mapped to an abbreviation, null is returned.
   *
   * @param unit the TimeUnit to be used
   * @return a String composed of an integer followed by the abbreviation of the specified unit or null
   */
  public String toString(TimeUnit unit) {
    long timeDifference = calculateTimeDifference(); // Time difference should be calculated as soon as possible.
    if (ABBREVIATIONS.containsKey(unit)) {
      return unit.convert(timeDifference, TimeUnit.NANOSECONDS) + " " + ABBREVIATIONS.get(unit);
    } else {
      DLogger.warning("Passed a TimeUnit that does not have a defined abbreviation to StopWatch.toString(TimeUnit)!");
      return null;
    }
  }

  /**
   * Returns a String representation of the time difference in milliseconds between this method call and the creation
   * of this StopWatch. If the TimeUnit.MILLISECOND is not mapped to an abbreviation, null is returned.
   *
   * @return a String composed of an integer followed by the abbreviation for millisecond or null
   */
  @Override
  public String toString() {
    return toString(TimeUnit.MILLISECONDS);
  }

}
