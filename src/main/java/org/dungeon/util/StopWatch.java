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

/**
 * A StopWatch class used to measure time distance between two or more instants.
 * <p/>
 * The precision of the measurements performed with a StopWatch are dependent on System.nanoTime() precision.
 */
public class StopWatch {

  private static final int MILLISECOND_IN_NANOSECONDS = 1000000;
  private final long time;

  public StopWatch() {
    time = System.nanoTime();
  }

  /**
   * Returns a String representation of the time difference between this method call and the creation of this StopWatch.
   *
   * @return a String of the form "x ms" where x is an integer.
   */
  public String toString() {
    final long difference = System.nanoTime() - time;
    return String.format("%d ms", difference / MILLISECOND_IN_NANOSECONDS);
  }

}
