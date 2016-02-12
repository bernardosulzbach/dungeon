package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.date.DungeonTimeUnit;

class SpawnerPreset {

  public final Id id;
  public final int population;
  public final int spawnDelay;

  /**
   * Default SpawnerPreset constructor.
   *
   * @param id the creature ID string
   * @param population the maximum population
   * @param delayInHours the spawn delay, in hours
   */
  public SpawnerPreset(String id, int population, int delayInHours) {
    this.id = new Id(id);
    this.population = population;
    this.spawnDelay = delayInHours * (int) DungeonTimeUnit.HOUR.milliseconds;
  }

}
