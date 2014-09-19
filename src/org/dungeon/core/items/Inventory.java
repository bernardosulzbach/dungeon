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

package org.dungeon.core.items;

import org.dungeon.io.IO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class that defines a common general-purpose Item storage and query structure.
 * <p/>
 * Change log
 * Created by Bernardo on 19/09/2014.
 */
public class Inventory implements Serializable {

    private int itemLimit;
    private final List<Item> itemList;

    public Inventory(int itemLimit) {
        this.itemLimit = itemLimit;
        this.itemList = new ArrayList<Item>();
    }

    public Inventory(int itemLimit, List<Item> itemList) {
        if (itemLimit < itemList.size()) {
            throw new IllegalArgumentException("itemLimit is smaller than the size of the itemList.");
        }
        this.itemLimit = itemLimit;
        this.itemList = itemList;
    }

    public List<Item> getItems() {
        return itemList;
    }

    public int getItemLimit() {
        return itemLimit;
    }

    public void printItems() {
        if (itemList.size() == 0) {
            IO.writeString("Inventory is empty.");
        } else {
            StringBuilder builder = new StringBuilder();
            for (Item itemInInventory : itemList) {
                builder.append(itemInInventory.toSelectionEntry()).append('\n');
            }
            IO.writeString(builder.toString());
        }
    }

    /**
     * Checks if an item is already in the inventory.
     */
    public boolean hasItem(Item itemObject) {
        return itemList.contains(itemObject);
    }

    public Item findItem(String itemName) {
        for (Item itemInInventory : itemList) {
            if (itemInInventory.getName().equalsIgnoreCase(itemName)) {
                return itemInInventory;
            }
        }
        return null;
    }

    /**
     * Attempts to add an item object to the inventory.
     *
     * @return true if the item was successfully added to the inventory; false otherwise.
     */
    public boolean addItem(Item newItem) {
        // Check that the new item is not already in the inventory.
        if (hasItem(newItem)) {
            throw new IllegalArgumentException("newItem is already in the inventory.");
        }
        if (itemLimit == itemList.size()) {
            IO.writeString("Inventory full.");
            return false;
        } else {
            itemList.add(newItem);
            IO.writeString("Added " + newItem.getName() + " to the inventory.");
            return true;
        }
    }

    public void removeItem(Item itemObject) {
        itemList.remove(itemObject);
    }

}
