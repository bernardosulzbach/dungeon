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
import java.util.ArrayList;
import java.util.List;
import org.dungeon.core.counter.CreatureCounter;
import org.dungeon.utils.Constants;

public final class World implements Serializable {

    private final List<Location> locations;
    private final CreatureCounter spawnCounter;

    /**
     * @param startingLocation
     * @param campaignPlayer
     */
    public World(Location startingLocation, Hero campaignPlayer) {
        spawnCounter = new CreatureCounter();

        locations = new ArrayList<>();
        locations.add(startingLocation);
        addCreature(campaignPlayer, 0);
    }

    /**
     * Add a creature to a specific location.
     *
     * @param creature
     * @param locationIndex
     */
    public final void addCreature(Creature creature, int locationIndex) {
        if (-1 < locationIndex && locationIndex < locations.size()) {
            spawnCounter.incrementCreatureCount(creature.getId());
            locations.get(locationIndex).addCreature(creature);
            creature.setLocation(locations.get(locationIndex));
        }
    }

    /**
     * Add an item to a specific location.
     *
     * @param item
     * @param locationIndex
     */
    public void addItem(Item item, int locationIndex) {
        if (-1 < locationIndex && locationIndex < locations.size()) {
            locations.get(locationIndex).addItem(item);
        }
    }

    public Location getLocation(int locationIndex) {
        if (-1 < locationIndex && locationIndex < locations.size()) {
            return locations.get(locationIndex);
        } else {
            return null;
        }
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
