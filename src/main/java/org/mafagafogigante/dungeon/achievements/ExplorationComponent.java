package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;
import org.mafagafogigante.dungeon.util.CounterMap;

/**
 * The exploration component of an achievement.
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
  public boolean isFulfilled(ExplorationStatistics explorationStatistics) {
    if (killsByLocationId != null) {
      for (Id locationId : killsByLocationId.keySet()) {
        if (explorationStatistics.getKillCount(locationId) < killsByLocationId.getCounter(locationId)) {
          return false;
        }
      }
    }
    if (visitedLocations != null) {
      for (Id locationId : visitedLocations.keySet()) {
        if (explorationStatistics.getVisitedLocations(locationId) < visitedLocations.getCounter(locationId)) {
          return false;
        }
      }
    }
    if (maximumNumberOfVisits != null) {
      for (Id locationId : maximumNumberOfVisits.keySet()) {
        if (explorationStatistics.getMaximumNumberOfVisits(locationId) < maximumNumberOfVisits.getCounter(locationId)) {
          return false;
        }
      }
    }
    return true;
  }

}
