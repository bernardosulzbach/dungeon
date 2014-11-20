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
import org.dungeon.core.items.Item;
import org.dungeon.core.items.LocationInventory;
import org.dungeon.io.IO;
import org.dungeon.utils.SelectionResult;
import org.dungeon.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable {

    private World world;

    private final String name;
    private final List<Creature> creatures;
    private final List<Spawner> spawners;
    private final LocationInventory items;

    private final double lightPermittivity;

    public Location(LocationPreset preset, World world) {
        this.world = world;
        this.name = preset.getName();
        this.lightPermittivity = preset.getLightPermittivity();
        this.creatures = new ArrayList<Creature>();
        spawners = new ArrayList<Spawner>(preset.getSpawners().length);
        for (SpawnerPreset spawnerPreset : preset.getSpawners()) {
            spawners.add(new Spawner(spawnerPreset, this));
        }
        this.items = new LocationInventory();
        for (ItemFrequencyPair pair : preset.getItems()) {
            if (Game.RANDOM.nextDouble() < pair.getFrequency()) {
                this.addItem(new Item(GameData.ITEM_BLUEPRINTS.get(pair.getId())));
            }
        }
    }

    public void refreshSpawners() {
        for (Spawner spawner : spawners) {
            spawner.refresh();
        }
    }

    public String getName() {
        return name;
    }

    double getLightPermittivity() {
        return lightPermittivity;
    }

    /**
     * Returns the luminosity of the Location. This value depends on the World luminosity and the Location's specific
     * light permittivity.
     */
    public double getLuminosity() {
        return getLightPermittivity() * getWorld().getPartOfDay().getLuminosity();
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public LocationInventory getInventory() {
        return items;
    }

    public List<Item> getItemList() {
        return items.getItems();
    }

    public int getCreatureCount() {
        return creatures.size();
    }

    public int getCreatureCount(String id) {
        int count = 0;
        for (Creature creature : creatures) {
            if (creature.getId().equals(id)) {
                count++;
            }
        }
        return count;
    }

    public int getItemCount() {
        return items.getItems().size();
    }

    public void addCreature(Creature creature) {
        creature.setLocation(this);
        creatures.add(creature);
    }

    public void addItem(Item item) {
        items.addItem(item);
    }

    public boolean hasCreature(Creature creature) {
        for (Creature localCreature : creatures) {
            if (localCreature.equals(creature)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to find a creature in the location by part of its name. If the given name matches multiple creatures,
     * null is returned.
     */
    public Creature findCreature(String[] tokens) {
        SelectionResult<Creature> selectionResult = Utils.selectFromList(creatures, tokens);
        if (selectionResult.size() == 0) {
            IO.writeString("Creature not found.");
        } else if (selectionResult.size() == 1 || selectionResult.getDifferentNames() == 1) {
            return selectionResult.getMatch(0);
        } else {
            Utils.printAmbiguousSelectionMessage();
        }
        return null;
    }

    public void removeItem(Item item) {
        items.removeItem(item);
    }

    public void removeCreature(Creature creatureToRemove) {
        creatures.remove(creatureToRemove);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
