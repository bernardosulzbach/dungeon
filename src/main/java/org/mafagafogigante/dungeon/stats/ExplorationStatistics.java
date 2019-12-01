package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ExplorationStatistics class that tracks the Hero's exploration progress.
 */
public class ExplorationStatistics implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final HashMap<Point, ExplorationStatisticsEntry> entries;
  private final HashMap<Point, ExplorationStatisticsEntry> distinctLocations;

  public ExplorationStatistics() {
    this.entries = new HashMap<>();
    this.distinctLocations = new HashMap<>();
  }

  /**
   * Creates an ExplorationStatisticsEntry for a Point if one does not exist yet. Should be called whenever the Hero
   * sees a Point, as the current criteria for "seen" locations on the in-game map is having an Entry in the
   * ExplorationStatistics.
   *
   * @param point the Point object
   * @param locationId the ID of the Location at the specified Point
   */
  public void createEntryIfNotExists(Point point, Id locationId, Date discoveredDate) {
    if (!hasBeenSeen(point)) {
      entries.put(point, new ExplorationStatisticsEntry(locationId, discoveredDate));
    }
  }

  /**
   * Records a visit to a specified Point.
   *
   * @param point the visited Point
   */
  public void addVisit(Point point, Id locationId, Date discoveredDate) {
    createEntryIfNotExists(point, locationId, discoveredDate);
    entries.get(point).addVisit();
    addDistinctLocationByPartOfDay(point);
  }

  /**
   * Records a distinct location corresponding to partOfDay.
   *
   * @param point the visited Point
   */
  public void addDistinctLocationByPartOfDay(Point point) {
    boolean distinct = true;
    for (ExplorationStatisticsEntry location : distinctLocations.values()) {
      PartOfDay day = PartOfDay.getCorrespondingConstant(entries.get(point).getDiscoveredDate());
      String entryName = day.getName().getSingular();
      String distinctName = PartOfDay.getCorrespondingConstant(location.getDiscoveredDate()).toString();
      if (location.getLocationId() == entries.get(point).getLocationId() && entryName.equalsIgnoreCase(distinctName)) {
        distinct = false;
        break;
      }
    }

    if (distinct) {
      distinctLocations.put(point, entries.get(point));
    }
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
   * Returns how many Locations with the specified Id the Hero visited.
   *
   * @param locationId the Id of the Locations
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
    int maximumVisitsToLocationWithThisId = 0;
    for (ExplorationStatisticsEntry entry : entries.values()) {
      if (entry.getLocationId().equals(locationId)) {
        if (entry.getVisitCount() > maximumVisitsToLocationWithThisId) {
          maximumVisitsToLocationWithThisId = entry.getVisitCount();
        }
      }
    }
    return maximumVisitsToLocationWithThisId;
  }

  /**
   * Returns the number of visits for distinct Locations from a specific part of day.
   *
   * @param partOfDay the PartOfDay of when the location was discovered
   * @return a nonnegative integer
   */
  public int getVisitsByPartOfDay(PartOfDay partOfDay) {
    int count = 0;
    for (ExplorationStatisticsEntry location : distinctLocations.values()) {
      PartOfDay day = PartOfDay.getCorrespondingConstant(location.getDiscoveredDate());
      String locationName = day.getName().getSingular();
      if (locationName.equalsIgnoreCase(partOfDay.getName().getSingular())) {
        count++;
      }
    }
    return count;
  }

}
