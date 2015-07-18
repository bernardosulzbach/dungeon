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

package org.dungeon.stats;

import org.dungeon.game.Id;

import java.io.Serializable;

/**
 * An ExplorationLog entry that stores data relative to one Point.
 */
class ExplorationStatisticsEntry implements Serializable {

  private final Id locationId;
  private int visitCount;
  private int killCount;

  public ExplorationStatisticsEntry(Id locationId) {
    this.locationId = locationId;
  }

  public Id getLocationId() {
    return locationId;
  }

  /**
   * Returns how many times the Hero visited this Point.
   */
  public int getVisitCount() {
    return visitCount;
  }

  public void addVisit() {
    this.visitCount++;
  }

  /**
   * Returns how many times the Hero killed in this Point.
   */
  public int getKillCount() {
    return killCount;
  }

  public void addKill() {
    this.killCount++;
  }

  @Override
  public String toString() {
    String format = "ExplorationStatisticsEntry{locationId=%s, visitCount=%d, killCount=%d}";
    return String.format(format, locationId, visitCount, killCount);
  }

}
