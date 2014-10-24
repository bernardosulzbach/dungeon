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
import org.dungeon.core.items.ItemPreset;
import org.dungeon.io.IO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Creature> creatures;
    private final List<Item> items;

    private final double lightPermittivity;

    private World world;

    public Location(LocationPreset preset) {
        this.name = preset.getName();
        this.lightPermittivity = preset.getLightPermittivity();
        this.creatures = new ArrayList<Creature>();
        for (String creatureID : preset.getCreatures()) {
            Creature creature = new Creature(GameData.CREATURE_BLUEPRINTS.get(creatureID));
            creature.setLocation(this);
            this.addCreature(creature);
        }
        this.items = new ArrayList<Item>();
        for (ItemPreset itemPreset : preset.getItems()) {
            this.addItem(Item.createItem(itemPreset));
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
        return getLightPermittivity() * getWorld().getDayPart().getLuminosity();
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getCreatureCount() {
        return creatures.size();
    }

    public int getItemCount() {
        return items.size();
    }

    public void addCreature(Creature creature) {
        creature.setLocation(this);
        creatures.add(creature);
    }

    public void addItem(Item item) {
        items.add(item);
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
     * Attempts to find a creature by its name.
     */
    public Creature findCreature(String name) {
        for (Creature creature : creatures) {
            if (creature.getName().equalsIgnoreCase(name)) {
                return creature;
            }
        }
        IO.writeString("Creature not found.");
        return null;
    }

    /**
     * Attempts to find an item by its name.
     *
     * @return an Item object if there is a match. null otherwise.
     */
    public Item findItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        items.remove(item);
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
