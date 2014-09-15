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

import java.io.Serializable;
import java.util.HashMap;
import org.dungeon.core.counter.CreatureCounter;
import org.dungeon.utils.Constants;

public class World implements Serializable {

    private final HashMap<Point, Location> locations;
    private final CreatureCounter spawnCounter;

    /**
     * @param startingLocation
     * @param campaignPlayer
     */
    public World() {
        spawnCounter = new CreatureCounter();
        locations = new HashMap<>();
    }

    public void addLocation(Location locationObject, Point coordinates) {
        locations.put(coordinates, locationObject);
    }

    /**
     * Add a creature to a specific location.
     *
     * @param creature
     * @param locationIndex
     */
    public void addCreature(Creature creature, Point coordinates) {
        locations.get(coordinates).addCreature(creature);
        spawnCounter.incrementCreatureCount(creature.getId());
    }

    /**
     * Add an item to a specific location.
     *
     * @param item
     * @param locationIndex
     */
    public void addItem(Item item, Point locationPoint) {
        locations.get(locationPoint).addItem(item);
    }

    /**
     * Move a Creature from origin to destination.
     *
     * @param creature
     * @param origin
     * @param destination
     */
    public void moveCreature(Creature creature, Point origin, Point destination) {
        if (locations.get(origin).hasCreature(creature)) {
            locations.get(origin).removeCreature(creature);
            locations.get(destination).addCreature(creature);
        } else {
            throw new IllegalArgumentException("Creature is not in the origin.");
        }
    }

    public boolean hasLocation(Point point) {
        return locations.containsKey(point);
    }

    public Location getLocation(Point point) {
        return locations.get(point);
    }

    /**
     * Prints all the spawn counters.
     */
    public void printSpawnCounters() {
        StringBuilder sb = new StringBuilder();
        for (CreatureID id : spawnCounter.getKeySet()) {
            sb.append(Constants.MARGIN).append(String.format("%-20s%10d\n", id, spawnCounter.getCreatureCount(id)));
        }
        IO.writeString(sb.toString());
    }
}
