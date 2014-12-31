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

import java.io.Serializable;

/**
 * WorldStatistics class that tracks world statistics.
 * <p/>
 * Created by Bernardo Sulzbach on 12/31/14.
 */
public final class WorldStatistics implements Serializable {

  private int locations;
  private int creatures;

  // TODO: start to use location and creature name

  /**
   * Adds the creation of a new Location to the statistics.
   */
  public void addLocation(String location) {
    locations++;
  }

  /**
   * Adds the spawn of a new Creature to the statistics.
   */
  public void addSpawn(String creature) {
    creatures++;
  }

  /**
   * Returns the Location count.
   *
   * @return the Location count.
   */
  public int getLocationCount() {
    return locations;
  }

  /**
   * Returns the Creature count.
   *
   * @return the Creature count.
   */
  public int getCreatureCount() {
    return creatures;
  }

}
