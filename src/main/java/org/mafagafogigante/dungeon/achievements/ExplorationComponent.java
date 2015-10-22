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

package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;
import org.mafagafogigante.dungeon.util.CounterMap;

/**
 * The exploration component of the achievements.
 */
final class ExplorationComponent {

  /**
   * Stores how many kills in Locations with a specified ID the Hero must have.
   */
  private final CounterMap<Id> killsByLocationId;

  /**
   * Stores how many distinct Locations with a specified ID the Hero must visit.
   */
  private final CounterMap<Id> visitedLocations;

  /**
   * Stores how many times the Hero must visit the same Location with a specified ID.
   */
  private final CounterMap<Id> maximumNumberOfVisits;

  ExplorationComponent(CounterMap<Id> killsByLocationId, CounterMap<Id> visitedLocations,
      CounterMap<Id> maximumNumberOfVisits) {
    this.killsByLocationId = killsByLocationId;
    this.visitedLocations = visitedLocations;
    this.maximumNumberOfVisits = maximumNumberOfVisits;
  }

  /**
   * Checks if this component of the Achievement is fulfilled or not.
   */
  public boolean isFulfilled() {
    ExplorationStatistics statistics = Game.getGameState().getStatistics().getExplorationStatistics();
    if (killsByLocationId != null) {
      for (Id locationId : killsByLocationId.keySet()) {
        if (statistics.getKillCount(locationId) < killsByLocationId.getCounter(locationId)) {
          return false;
        }
      }
    }
    if (visitedLocations != null) {
      for (Id locationId : visitedLocations.keySet()) {
        if (statistics.getVisitedLocations(locationId) < visitedLocations.getCounter(locationId)) {
          return false;
        }
      }
    }
    if (maximumNumberOfVisits != null) {
      for (Id locationId : maximumNumberOfVisits.keySet()) {
        if (statistics.getMaximumNumberOfVisits(locationId) < maximumNumberOfVisits.getCounter(locationId)) {
          return false;
        }
      }
    }
    return true;
  }

}
