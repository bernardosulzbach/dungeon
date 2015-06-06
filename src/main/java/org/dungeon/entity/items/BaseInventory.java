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

package org.dungeon.entity.items;

import org.dungeon.entity.items.Item.Tag;

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
  public boolean hasItem(Item itemObject) {
    return items.contains(itemObject);
  }

  /**
   * Removes an item from the Inventory.
   *
   * @param item the Item to be removed
   */
  protected abstract void removeItem(Item item);

  /**
   * Iterates through the inventory, removing items that shouldn't exist anymore.
   */
  public void refreshItems() {
    for (Item item : items) {
      if (item.hasTag(Tag.DECOMPOSES) && item.getAge() > item.getDecompositionPeriod()) {
        removeItem(item);
      }
    }
  }

}
