package org.dungeon.core.game;

// Class that spawns creatures after a given period of time in a given location in order to keep the world populated.
public class SpawnerPreset {

    public final String id;
    public final int population;
    public final int spawnDelay;

    /**
     * Default SpawnerPreset constructor.
     * @param id the creature ID string.
     * @param population the maximum population.
     * @param spawnsPerDay how many spawns should take place in a day. 24 spawns per day means that, at most, one
     *                     creature will spawn every hour.
     */
    public SpawnerPreset(String id, int population, int spawnsPerDay) {
        this.id = id;
        this.population = population;
        this.spawnDelay = 24 * 60 * 60 * 1000 / spawnsPerDay;
    }

}
