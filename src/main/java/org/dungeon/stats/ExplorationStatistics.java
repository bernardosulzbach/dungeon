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
import org.dungeon.game.Point;

import java.io.Serializable;
import java.util.HashMap;

/**
 * ExplorationStatistics class that tracks the Hero's exploration progress.
 */
public class ExplorationStatistics implements Serializable {

  private final HashMap<Point, ExplorationStatisticsEntry> entries;

  public ExplorationStatistics() {
    this.entries = new HashMap<Point, ExplorationStatisticsEntry>();
  }

  /**
   * Creates an ExplorationStatisticsEntry for a Point if one does not exist yet. Should be called whenever the Hero
   * sees a Point, as the current criteria for "seen" locations on the in-game map is having an Entry in the
   * ExplorationStatistics.
   *
   * @param point the Point object
   * @param locationId the ID of the Location at the specified Point
   */
  public void createEntryIfNotExists(Point point, Id locationId) {
    if (!hasBeenSeen(point)) {
      entries.put(point, new ExplorationStatisticsEntry(locationId));
    }
  }

  /**
   * Records a visit to a specified Point.
   *
   * @param point the visited Point
   */
  public void addVisit(Point point, Id locationId) {
    createEntryIfNotExists(point, locationId);
    entries.get(point).addVisit();
  }

  /**
   * Records a kill in a specified Point.
   *
   * @param point the Point where the Hero just killed something
   */
  public void addKill(Point point) {
    // Don't call createEntryIfNotExists as the player needs to visit a Point before killing anything in it.
    entries.get(point).addKill();
  }

  /**
   * Returns whether or not a specific Point has already been seen by the Hero.
   *
   * @param point a Point object
   * @return true if the Hero visited this Point at least once
   */
  public boolean hasBeenSeen(Point point) {
    return entries.containsKey(point);
  }

  /**
   * Returns how many Locations with the specified ID the Hero visited.
   *
   * @param locationId the ID of the Locations
   * @return a nonnegative integer
   */
  public int getVisitedLocations(Id locationId) {
    int count = 0;
    for (ExplorationStatisticsEntry entry : entries.values()) {
      if (entry.getLocationId().equals(locationId) && entry.getVisitCount() > 0) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns how many Creatures the Hero killed in Locations with a specified ID.
   *
   * @param locationId the ID of the Locations
   * @return a nonnegative integer
   */
  public int getKillCount(Id locationId) {
    int count = 0;
    for (ExplorationStatisticsEntry entry : entries.values()) {
      if (entry.getLocationId().equals(locationId)) {
        count += entry.getKillCount();
      }
    }
    return count;
  }

  /**
   * Returns the maximum number of visits for any Location with a specified ID so far.
   *
   * @param locationId the ID of the Locations
   * @return a nonnegative integer
   */
  public int getMaximumNumberOfVisits(Id locationId) {
    int maximumVisitsToLocationWithThisID = 0;
    for (ExplorationStatisticsEntry entry : entries.values()) {
      if (entry.getLocationId().equals(locationId)) {
        if (entry.getVisitCount() > maximumVisitsToLocationWithThisID) {
          maximumVisitsToLocationWithThisID = entry.getVisitCount();
        }
      }
    }
    return maximumVisitsToLocationWithThisID;
  }

}
