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
package org.dungeon.items;

import org.dungeon.creatures.Creature;
import org.dungeon.game.Engine;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

import java.awt.*;

/**
 * Inventory class that defines a common general-purpose Item storage and query structure.
 * <p/>
 * Change log Created by Bernardo on 19/09/2014.
 */
public class CreatureInventory extends BaseInventory implements LimitedInventory {

    private static final long serialVersionUID = 1L;
    private final Creature owner;
    private int limit;

    public CreatureInventory(Creature owner, int limit) {
        this.owner = owner;
        this.limit = limit;
    }

    public void printItems() {
        if (items.size() == 0) {
            if (Engine.RANDOM.nextBoolean()) {
                IO.writeString("Inventory is empty.");
            } else {
                IO.writeString("There are no items in the inventory.");
            }
        } else {
            for (Item item : items) {
                printItem(item);
            }
        }
    }

    private void printItem(Item item) {
        Color color = item.isEquipped() ? Color.MAGENTA : Constants.FORE_COLOR_NORMAL;
        int typeTagLength = 10;
        String typeString = "[" + item.getType() + "]";
        String extraSpace = Utils.makeRepeatedCharacterString(typeTagLength - typeString.length(), ' ');
        IO.writeString(typeString, color, false);
        if (item.isPerfect()) {
            IO.writeString(extraSpace + item.getName(), color);
        } else {
            IO.writeString(extraSpace + item.getIntegrityString(), Constants.FORE_COLOR_DARKER, false);
            IO.writeString(" " + item.getName(), color);
        }
    }

    /**
     * Attempts to add an item object to the inventory.
     */
    public boolean addItem(Item newItem) {
        // Check that the new item is not already in the inventory.
        if (hasItem(newItem)) {
            throw new IllegalArgumentException("newItem is already in the inventory.");
        }
        if (isFull()) {
            // Print the default inventory full message.
            IO.writeString(Constants.INVENTORY_FULL);
        } else {
            items.add(newItem);
            newItem.setOwner(owner);
            IO.writeString("Added " + newItem.getName() + " to the inventory.");
            return true;
        }
        return false;
    }

    public void removeItem(Item item) {
        if (owner.getWeapon() == item) {
            owner.setWeapon(null);
        }
        items.remove(item);
        item.setOwner(null);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isFull() {
        return this.items.size() == this.limit;
    }

}
