package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.items.Item.Tag;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The abstract BaseInventory class all inventories inherit from.
 */
public abstract class BaseInventory implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
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
