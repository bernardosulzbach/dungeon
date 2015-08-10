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

package org.dungeon.game;

import org.dungeon.date.EarthTimeUnit;
import org.dungeon.date.TimeStringBuilder;
import org.dungeon.util.DungeonMath;

/**
 * Information about an instance of the game.
 */
class InstanceInformation {

  private final long startingTimeMillis;
  private long acceptedCommandCount;

  public InstanceInformation() {
    this.startingTimeMillis = System.currentTimeMillis();
  }

  /**
   * Returns a string representing the duration of this instance.
   */
  public String getDurationString() {
    TimeStringBuilder timeStringBuilder = new TimeStringBuilder();
    timeStringBuilder.set(EarthTimeUnit.SECOND, DungeonMath.safeCastLongToInteger(getDurationInMillis() / 1000));
    return timeStringBuilder.toString(2); // Use the two most significant non-zero fields.
  }

  private long getDurationInMillis() {
    return System.currentTimeMillis() - startingTimeMillis;
  }

  public long getAcceptedCommandCount() {
    return acceptedCommandCount;
  }

  public void incrementAcceptedCommandCount() {
    this.acceptedCommandCount++;
  }

}
