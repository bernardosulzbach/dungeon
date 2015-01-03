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

package org.dungeon.counters;

import org.dungeon.game.ID;
import org.dungeon.game.Point;

import java.io.Serializable;
import java.util.HashMap;

/**
 * ExplorationLog class that enables tracking visited locations.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
public class ExplorationLog implements Serializable {

  // Hold data for each point (and not for each Location ID - what should suffice for now) so that in the future more
  // complex achievements can be made.
  private final HashMap<Point, ExplorationData> entries;

  public ExplorationLog() {
    this.entries = new HashMap<Point, ExplorationData>();
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
      entries.put(point, new ExplorationData(locationID, 1, 0));
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
   * Returns how many different Locations with a specified ID the Hero visited.
   *
   * @param locationID the ID of the Locations.
   * @return a nonnegative integer.
   */
  public int getDistinctVisitCount(ID locationID) {
    int count = 0;
    for (ExplorationData entry : entries.values()) {
      if (entry.getLocationID().equals(locationID)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns how many time the Hero visited the same Locations with a specified ID.
   *
   * @param locationID the ID of the Locations.
   * @return a nonnegative integer.
   */
  public int getSameLocationVisitCount(ID locationID) {
    int maximumVisitsToLocationWithThisID = 0;
    for (ExplorationData entry : entries.values()) {
      if (entry.getLocationID().equals(locationID)) {
        if (entry.getVisitCount() > maximumVisitsToLocationWithThisID) {
          maximumVisitsToLocationWithThisID = entry.getVisitCount();
        }
        maximumVisitsToLocationWithThisID += entry.getVisitCount();
      }
    }
    return maximumVisitsToLocationWithThisID;
  }

  /**
   * Returns how many Creatures the Hero killed in Locations with a specified ID.
   *
   * @param locationID the ID of the Locations.
   * @return a nonnegative integer.
   */
  public int getKillCount(ID locationID) {
    int count = 0;
    for (ExplorationData entry : entries.values()) {
      if (entry.getLocationID().equals(locationID)) {
        count += entry.getKillCount();
      }
    }
    return count;
  }

  /**
   * @return a String representation of all the content of the log.
   */
  public String toString() {
    StringBuilder builder = new StringBuilder();
    ExplorationData explorationData;
    for (Point point : entries.keySet()) {
      explorationData = entries.get(point);
      builder.append(point.toString());
      builder.append(" Visits: ").append(explorationData.getVisitCount());
      builder.append(" Kills: ").append(explorationData.getKillCount());
      builder.append("\n");
    }
    return builder.toString();
  }

}
