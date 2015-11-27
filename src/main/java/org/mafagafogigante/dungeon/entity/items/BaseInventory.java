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

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.items.Item.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The abstract BaseInventory class all inventories inherit from.
 */
public abstract class BaseInventory implements Serializable {

  final List<Item> items;

  BaseInventory() {
    items = new ArrayList<>();
  }

  private static boolean isDecomposed(Item item) {
    return (item.hasTag(Tag.DECOMPOSES) && item.getAge() >= item.getDecompositionPeriod());
  }

  /**
   * Returns an unmodifiable view of the list of the Items. Use removeItem(Item) to remove items.
   *
   * @return an unmodifiable view of the list of the Items
   */
  public List<Item> getItems() {
    return Collections.unmodifiableList(items);
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
  public boolean hasItem(Item item) {
    return items.contains(item);
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
    for (Item item : new ArrayList<>(items)) {
      if (isDecomposed(item)) {
        removeItem(item);
      }
    }
  }

}
