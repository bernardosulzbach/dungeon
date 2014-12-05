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
package org.dungeon.game;

import java.util.ArrayList;
import java.util.List;

/**
 * The LocationPreset class that serves as a recipe for Locations.
 */
final class LocationPreset extends Preset {

    private String name;
    private BlockedEntrances blockedEntrances;
    private ArrayList<SpawnerPreset> spawners;
    private ArrayList<ItemFrequencyPair> items;
    private double lightPermittivity;

    LocationPreset(String name) {
        this.name = name;
        blockedEntrances = new BlockedEntrances();
        spawners = new ArrayList<SpawnerPreset>();
        items = new ArrayList<ItemFrequencyPair>();
    }

    public LocationPreset addSpawner(SpawnerPreset spawner) {
        if (!isLocked()) {
            this.spawners.add(spawner);
        }
        return this;
    }

    public LocationPreset addItem(String id, Double likelihood) {
        if (!isLocked()) {
            this.items.add(new ItemFrequencyPair(id, likelihood));
        }
        return this;
    }

    public LocationPreset setLightPermittivity(double lightPermittivity) {
        if (!isLocked()) {
            this.lightPermittivity = lightPermittivity;
        }
        return this;
    }

    /**
     * Block exiting and entering into the location by a given direction.
     *
     * @param direction a Direction to be blocked.
     */
    public LocationPreset block(Direction direction) {
        if (!isLocked()) {
            blockedEntrances.block(direction);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public BlockedEntrances getBlockedEntrances() {
        return new BlockedEntrances(blockedEntrances);
    }

    public List<SpawnerPreset> getSpawners() {
        return spawners;
    }

    public List<ItemFrequencyPair> getItems() {
        return items;
    }

    public double getLightPermittivity() {
        return lightPermittivity;
    }

    void finish() {
        if (!isLocked()) {
            spawners.trimToSize();
            items.trimToSize();
            lock();
        }
    }

}
