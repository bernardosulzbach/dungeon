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
import org.dungeon.io.IO;
import org.dungeon.io.WriteStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Creature> creatures;
    private final List<Item> inventory;

    private double lightPermittivity;
    private double visibility;

    private World world;

    public Location(String name, double lightPermittivity) {
        this.name = name;
        this.lightPermittivity = lightPermittivity;
        this.creatures = new ArrayList<Creature>();
        this.inventory = new ArrayList<Item>();
    }

    public String getName() {
        return name;
    }

    public double getLightPermittivity() { return lightPermittivity; }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Item> getItems() {
        return inventory;
    }

    public int getCreatureCount() {
        return creatures.size();
    }

    public int getItemCount() {
        return inventory.size();
    }

    public void addCreature(Creature creature) {
        creatures.add(creature);
    }

    /**
     * Adds all the creatures in an array of creatures to this Location object.
     *
     * @param creatureArray the array of creatures.
     */
    public void addCreatureArray(Creature[] creatureArray) {
        creatures.addAll(Arrays.asList(creatureArray));
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public boolean hasCreature(Creature creature) {
        for (Creature localCreature : creatures) {
            if (localCreature.equals(creature)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(Item item) {
        for (Item localItem : inventory) {
            if (localItem.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to find a creature by its name.
     */
    public Creature findCreature(String name) {
        for (Creature creature : creatures) {
            if (creature.getName().equalsIgnoreCase(name)) {
                return creature;
            }
        }
        IO.writeString("Creature not found.", WriteStyle.MARGIN);
        return null;
    }

    /**
     * Attempts to find an item by its name.
     *
     * @return an Item object if there is a match. null otherwise.
     */
    public Item findItem(String name) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        inventory.remove(item);
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
