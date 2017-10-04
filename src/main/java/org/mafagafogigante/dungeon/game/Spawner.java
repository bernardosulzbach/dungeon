package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import java.io.Serializable;

/**
 * Spawner class that repopulates locations.
 */
class Spawner implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Id id;
  private final int populationLimit;
  private final int spawnDelay;
  private final Location location;
  // A change can be either the spawn of a creature or the end of the population limit.
  private long lastChange;

  public Spawner(SpawnerPreset preset, Location location) {
    id = preset.id;
    populationLimit = Random.nextInteger(preset.minimumPopulation, preset.maximumPopulation + 1);
    spawnDelay = preset.spawnDelay;
    this.location = location;
    lastChange = getWorldCreationTime();
  }

  /**
   * Refresh the spawner, spawning all creatures that should have spawned since the last spawn.
   *
   * <p>Only spawners in locations whose creatures are visible to the player should be refreshed.
   */
  public void refresh() {
    long worldTime = getWorldTime();
    while (worldTime - lastChange >= spawnDelay && location.getCreatureCount(id) < populationLimit) {
      World world = location.getWorld();
      Creature creature = world.getCreatureFactory().makeCreature(id, world);
      if (creature != null) {
        location.addCreature(creature);
      } else {
        DungeonLogger.warning("Could not find the creature preset for " + id + ".");
      }
      // Simulate that the creature was spawned just when it should have been.
      // Do not prevent this modification if making the creature was unsuccessful to avoid an infinite loop.
      lastChange += spawnDelay;
    }
  }

  /**
   * Notify the killing of a creature in the location of the spawner. This is necessary in order to record a possible
   * end of the population limit.
   */
  public void notifyKill(Creature creature) {
    if (id.equals(creature.getId()) && location.getCreatureCount(id) == populationLimit) {
      lastChange = getWorldTime();
    }
  }

  /**
   * Returns the time, in milliseconds, of the World of the Location that this Spawner is in.
   */
  private long getWorldTime() {
    return location.getWorld().getWorldDate().getTime();
  }

  /**
   * Returns the time, in milliseconds, of the creation of the World of the Location that this Spawner is in.
   */
  private long getWorldCreationTime() {
    return location.getWorld().getWorldCreationDate().getTime();
  }

  @Override
  public String toString() {
    return "Spawner{" +
        "id=" + id +
        ", populationLimit=" + populationLimit +
        ", spawnDelay=" + spawnDelay +
        ", location=" + location +
        ", lastChange=" + lastChange +
        '}';
  }

}
