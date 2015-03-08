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
import org.dungeon.game.Weight;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.util.Constants;
import org.dungeon.util.Utils;

import java.awt.Color;

/**
 * Inventory class that defines a common general-purpose Item storage and query structure.
 * <p/>
 * Change log Created by Bernardo on 19/09/2014.
 */
public class CreatureInventory extends BaseInventory implements LimitedInventory {

  private final Creature owner;
  private final int itemLimit;
  private final Weight weightLimit;

  public CreatureInventory(Creature owner, int itemLimit, double weightLimit) {
    this.owner = owner;
    this.itemLimit = itemLimit;
    this.weightLimit = Weight.newInstance(weightLimit);
  }

  @Override
  public int getItemLimit() {
    return itemLimit;
  }

  @Override
  public Weight getWeightLimit() {
    return weightLimit;
  }

  public Weight getWeight() {
    Weight sum = Weight.ZERO;
    for (Item item : getItems()) {
      sum = sum.add(item.getWeight());
    }
    return sum;
  }

  /**
   * Prints all items in this {@code CreatureInventory}.
   */
  public void printItems() {
    // TODO: enumerate items instead.
    for (Item item : items) {
      printItem(item);
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
   *
   * @param item the Item to be added
   * @return true if successful, false otherwise
   */
  public boolean addItem(Item item) {
    if (hasItem(item)) { // Check that the new item is not already in the inventory.
      DLogger.warning("Tried to add an item to a CreatureInventory that already has it.");
      return false;
    }
    if (isFull()) {
      IO.writeString("Your inventory is full.");
    } else if (willExceedWeightLimitAfterAdding(item)) {
      IO.writeString("You can't carry more weight.");
    } else {
      items.add(item);
      item.setOwner(owner);
      IO.writeString("Added " + item.getName() + " to the inventory.");
      return true;
    }
    return false;
  }

  private boolean isFull() {
    return getItemCount() == getItemLimit();
  }

  private boolean willExceedWeightLimitAfterAdding(Item item) {
    return getWeight().add(item.getWeight()).compareTo(getWeightLimit()) > 0;
  }

  public void removeItem(Item item) {
    if (owner.getWeapon() == item) {
      owner.setWeapon(null);
    }
    items.remove(item);
    item.setOwner(null);
  }

}
