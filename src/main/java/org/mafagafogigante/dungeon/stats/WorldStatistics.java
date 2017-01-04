package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.CounterMap;

import java.io.Serializable;

/**
 * WorldStatistics class that tracks world statistics.
 */
public final class WorldStatistics implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final CounterMap<String> spawnCounter = new CounterMap<>();
  private final CounterMap<String> locationCounter = new CounterMap<>();
  private int spawnCount;
  private int locationCount;

  /**
   * Adds the spawn of a new Creature to the statistics.
   */
  public void addSpawn(String creature) {
    spawnCount++;
    spawnCounter.incrementCounter(creature);
  }

  /**
   * Adds the creation of a new Location to the statistics.
   */
  public void addLocation(String location) {
    locationCount++;
    locationCounter.incrementCounter(location);
  }

  /**
   * Returns the Creature count.
   *
   * @return the Creature count.
   */
  public int getSpawnCount() {
    return spawnCount;
  }

  /**
   * Returns the Location count.
   *
   * @return the Location count.
   */
  public int getLocationCount() {
    return locationCount;
  }

  /**
   * Returns the CounterMap that relate Creature name to spawn count.
   *
   * @return a CounterMap of String.
   */
  public CounterMap<String> getSpawnCounter() {
    return spawnCounter;
  }

  public CounterMap<String> getLocationCounter() {
    return locationCounter;
  }

}
