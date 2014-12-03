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
package org.dungeon.core.game;

import org.dungeon.core.creatures.Creature;

import java.io.Serializable;

/**
 * Spawner class that repopulates locations.
 */
public class Spawner implements Serializable {

    private final String id;
    private final int populationLimit;
    private final int spawnDelay;
    private final Location location;
    // A change can be either the spawn of a creature or the end of the population limit.
    private long lastChange;

    public Spawner(SpawnerPreset preset, Location location) {
        id = preset.id;
        populationLimit = preset.population;
        spawnDelay = preset.spawnDelay;
        this.location = location;
        lastChange = getWorldCreationTime();
    }

    /**
     * Refresh the spawner, spawning all creatures that should have spawned since the last spawn.
     * <p/>
     * Only spawners in locations whose creatures are visible to the player should be refreshed.
     */
    public void refresh() {
        long worldTime = getWorldTime();
        while (worldTime - lastChange >= spawnDelay && location.getCreatureCount(id) < populationLimit) {
            location.getWorld().getSpawnCounter().incrementCounter(id);
            location.addCreature(new Creature(GameData.CREATURE_BLUEPRINTS.get(id)));
            // Simulate that the creature was spawned just when it should have been.
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
     * @return the time, in milliseconds, of the World of the Location that this Spawner is in.
     */
    private long getWorldTime() {
        return location.getWorld().getWorldDate().getMillis();
    }

    /**
     * @return the time, in milliseconds, of the creation of the World of the Location that this Spawner is in.
     */
    private long getWorldCreationTime() {
        return location.getWorld().getWorldCreationDate().getMillis();
    }

}
