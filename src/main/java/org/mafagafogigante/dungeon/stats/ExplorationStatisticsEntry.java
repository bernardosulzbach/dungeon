package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * An ExplorationLog entry that stores data relative to one Point.
 */
class ExplorationStatisticsEntry implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Id locationId;
  private Date discoveredDate;
  private int visitCount;
  private int killCount;

  public ExplorationStatisticsEntry(Id locationId, Date discoveredDate) {
    this.locationId = locationId;
    this.discoveredDate = discoveredDate;
  }

  public Id getLocationId() {
    return locationId;
  }

  public Date getDiscoveredDate() {
    return discoveredDate;
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
    String format = "ExplorationStatisticsEntry{locationId=%s, visitCount=%d, killCount=%d, discoveredDate=%s}";
    return String.format(format, locationId, visitCount, killCount, discoveredDate);
  }

}
