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
import org.dungeon.utils.SelectionResult;
import org.dungeon.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The base inventory class.
 * <p/>
 * Created by Bernardo Sulzbach on 15/11/14.
 */
public abstract class BaseInventory implements Serializable {

    final List<Item> items;

    BaseInventory() {
        items = new ArrayList<Item>();
    }

    public List<Item> getItems() {
        return items;
    }

    /**
     * Convenience method that returns the number of items in the inventory.
     *
     * @return the number of items in the inventory.
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Checks if an item is already in the inventory.
     */
    boolean hasItem(Item itemObject) {
        return items.contains(itemObject);
    }

    public abstract void addItem(Item item);

    public abstract void removeItem(Item item);

    /**
     * Attempts to find an item by its name.
     *
     * @return an Item object if there is a match. null otherwise.
     */
    public Item findItem(String[] tokens) {
        SelectionResult<Item> selectionResult = Utils.selectFromList(items, tokens);
        if (selectionResult.size() == 0) {
            IO.writeString("Item not found.");
        } else if (selectionResult.size() == 1 || selectionResult.getDifferentNames() == 1) {
            return selectionResult.getMatch(0);
        } else {
            Utils.printAmbiguousSelectionMessage();
        }
        return null;
    }

}
