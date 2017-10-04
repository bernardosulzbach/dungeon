package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.date.DungeonTimeUnit;

class SpawnerPreset {

  public final Id id;
  public final int minimumPopulation;
  public final int maximumPopulation;
  public final int spawnDelay;

  /**
   * Default SpawnerPreset constructor.
   *
   * @param id the creature ID string
   * @param minimumPopulation the minimum maximum population for spawners
   * @param maximumPopulation the maximum maximum population for spawners
   * @param delayInHours the spawn delay, in hours
   */
  public SpawnerPreset(String id, int minimumPopulation, int maximumPopulation, int delayInHours) {
    if (minimumPopulation < 1) {
      throw new IllegalArgumentException("minimumPopulation must be positive");
    }
    if (minimumPopulation > maximumPopulation) {
      throw new IllegalArgumentException("minimumPopulation must be smaller than or equal to maximumPopulation");
    }
    this.id = new Id(id);
    this.minimumPopulation = minimumPopulation;
    this.maximumPopulation = maximumPopulation;
    this.spawnDelay = delayInHours * (int) DungeonTimeUnit.HOUR.milliseconds;
  }

}
