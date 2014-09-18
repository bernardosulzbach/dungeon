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

import org.dungeon.core.items.Item;
import java.io.Serializable;
import java.util.HashMap;
import org.dungeon.core.counters.CounterMap;

import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;

public class World implements Serializable {

    private static final long serialVersionUID = 1L;

    private final HashMap<Point, Location> locations;
    private final CounterMap<CreatureID> spawnCounter;

    public World() {
        spawnCounter = new CounterMap<CreatureID>();
        locations = new HashMap<Point, Location>();
    }

    public void addLocation(Location locationObject, Point coordinates) {
        locations.put(coordinates, locationObject);
    }

    /**
     * Add a creature to a specific location.
     */
    public void addCreature(Creature creature, Point coordinates) {
        locations.get(coordinates).addCreature(creature);
        creature.setLocation(getLocation(coordinates));
        spawnCounter.incrementCounter(creature.getId());
    }

    public void addCreatureArray(Creature[] creatures, Point coordinates) {
        locations.get(coordinates).addCreatureArray(creatures);
        for (Creature creature : creatures) {
            creature.setLocation(getLocation(coordinates));
        }
        spawnCounter.incrementCounter(creatures[0].getId(), creatures.length);
    }

    /**
     * Add an item to a specific location.
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
            System.out.printf("You arrive at %s.\n", locations.get(destination).getName());
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
        for (CreatureID id : spawnCounter.keySet()) {
            sb.append(Constants.MARGIN).append(String.format("%-20s%10d\n", id, spawnCounter.getCounter(id)));
        }
        IO.writeString(sb.toString());
    }
}
