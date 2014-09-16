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

import org.dungeon.core.counters.CreatureCounter;
import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.CreatureID;
import org.dungeon.utils.Constants;

public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Creature> creatures;
    private final List<Item> inventory;

    public Location(String name) {
        this.name = name;
        this.creatures = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

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
        return null;
    }

    /**
     * Attempts to find an item by its name.
     *
     * @param name
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

    /**
     * Get a list of all visible weapons to the observer.
     *
     * @return a list of weapons.
     */
    public List<Weapon> getVisibleWeapons() {
        List<Weapon> visibleWeapons = new ArrayList<>();
        for (Item visibleItem : inventory) {
            if (visibleItem instanceof Weapon) {
                visibleWeapons.add((Weapon) visibleItem);
            }
        }
        return visibleWeapons;
    }

    public String getCreaturesString() {
        CreatureCounter counter = new CreatureCounter();
        for (Creature creature : getCreatures()) {
            counter.incrementCreatureCount(creature.getId());
        }
        StringBuilder builder = new StringBuilder();
        for (CreatureID id : counter.getKeySet()) {
            builder.append('\n').append(Constants.MARGIN).append(id.toString());
            builder.append(" (").append(counter.getCreatureCount(id)).append(")");
        }
        if (builder.length() == 0) {
            return Constants.NO_CREATURES;
        } else {
            return builder.toString();
        }
    }

}
