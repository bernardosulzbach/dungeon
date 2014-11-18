package org.dungeon.core.game;

import org.dungeon.core.creatures.Creature;

import java.io.Serializable;

public class Spawner implements Serializable {

    private final String id;
    private final int population;
    private final int spawnDelay;
    private final Location location;
    private long lastSpawn;

    public Spawner(SpawnerPreset preset, Location location) {
        id = preset.id;
        population = preset.population;
        spawnDelay = preset.spawnDelay;
        this.location = location;
        this.lastSpawn = location.getWorld().getWorldCreationDate().getMillis();
    }

    public void refresh() {
        long worldTime = location.getWorld().getWorldDate().getMillis();
        if (worldTime - lastSpawn >= spawnDelay && location.getCreatureCount(id) < population) {
            location.getWorld().getSpawnCounter().incrementCounter(id);
            location.addCreature(new Creature(GameData.CREATURE_BLUEPRINTS.get(id)));
            lastSpawn = worldTime;
        }
    }

}
