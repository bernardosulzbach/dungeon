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
package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Location implements Serializable {

    private final String name;
    private final List<Creature> creatures;
    private final List<Item> items;

    public Location(String name) {
        creatures = new ArrayList<>();
        items = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addCreature(Creature creature) {
        creatures.add(creature);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Attempts to find an item by its name.
     *
     * @param name
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

    public Creature findCreature(String name) {
        for (Creature creature : creatures) {
            if (creature.getName().equalsIgnoreCase(name)) {
                return creature;
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

    /**
     * Remove all dead creatures in the current location.
     */
    public void removeAllDeadCreatures() {
        // To safely remove from a collection we must use an iterator.
        Iterator<Creature> i = creatures.iterator();
        while (i.hasNext()) {
            Creature creature = i.next();
            if (!creature.isAlive()) {
                i.remove();
            }
        }
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public int getCreatureCount() {
        return creatures.size();
    }

    public List<Item> getItems() {
        return items;
    }

    public int getItemCount() {
        return items.size();
    }

    /**
     * Get a list of all visible weapons to the observer.
     *
     * @return a list of weapons.
     */
    public List<Weapon> getVisibleWeapons() {
        List<Weapon> visibleWeapons = new ArrayList<>();
        for (Item visibleItem : items) {
            if (visibleItem instanceof Weapon) {
                visibleWeapons.add((Weapon) visibleItem);
            }
        }
        return visibleWeapons;
    }

}
