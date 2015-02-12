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

import org.dungeon.game.ID;
import org.dungeon.game.Point;

import java.io.Serializable;
import java.util.HashMap;

/**
 * ExplorationLog class that enables tracking visited locations.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
public class ExplorationStatistics implements Serializable {

  // Hold data for each point (and not for each Location ID - what should suffice for now) so that in the future more
  // complex achievements can be made.
  private final HashMap<Point, ExplorationStatisticsEntry> entries;

  public ExplorationStatistics() {
    this.entries = new HashMap<Point, ExplorationStatisticsEntry>();
  }

  /**
   * Records a visit to a specified Point.
   *
   * @param point the visited Point.
   */
  public void addVisit(Point point, ID locationID) {
    if (entries.containsKey(point)) {
      entries.get(point).addVisit();
    } else {
      entries.put(point, new ExplorationStatisticsEntry(locationID, 1, 0));
    }
  }

  /**
   * Records a kill in a specified Point.
   *
   * @param point the Point where the Hero just killed something.
   */
  public void addKill(Point point) {
    // No longer checks for a not existing point as the player needs to have visited (and thus created) a given
    // point before killing anything in it.
    entries.get(point).addKill();
  }

  /**
   * Returns how many Creatures the Hero killed in Locations with a specified ID.
   *
   * @param locationID the ID of the Locations.
   * @return a nonnegative integer.
   */
  public int getKillCount(ID locationID) {
    int count = 0;
    for (ExplorationStatisticsEntry entry : entries.values()) {
      if (entry.getLocationID().equals(locationID)) {
        count += entry.getKillCount();
      }
    }
    return count;
  }

  /**
   * Returns how many Locations with the specified ID the Hero discovered.
   *
   * @param locationID the ID of the Locations.
   * @return a nonnegative integer.
   */
  public int getDiscoveredLocations(ID locationID) {
    int count = 0;
    for (ExplorationStatisticsEntry entry : entries.values()) {
      if (entry.getLocationID().equals(locationID)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns the maximum number of visits for any Location with a specified ID so far.
   *
   * @param locationID the ID of the Locations.
   * @return a nonnegative integer.
   */
  public int getMaximumNumberOfVisits(ID locationID) {
    int maximumVisitsToLocationWithThisID = 0;
    for (ExplorationStatisticsEntry entry : entries.values()) {
      if (entry.getLocationID().equals(locationID)) {
        if (entry.getVisitCount() > maximumVisitsToLocationWithThisID) {
          maximumVisitsToLocationWithThisID = entry.getVisitCount();
        }
      }
    }
    return maximumVisitsToLocationWithThisID;
  }

}
