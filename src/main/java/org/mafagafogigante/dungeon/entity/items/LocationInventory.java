package org.mafagafogigante.dungeon.entity.items;

/**
 * The inventory used by Location objects.
 */
public class LocationInventory extends BaseInventory {

  public void addItem(Item item) {
    items.add(item);
    item.setInventory(this);
  }

  public void removeItem(Item item) {
    items.remove(item);
    item.setInventory(null);
  }

}
