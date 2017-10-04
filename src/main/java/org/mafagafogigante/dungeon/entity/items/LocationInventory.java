package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.io.Version;

/**
 * The inventory used by Location objects.
 */
public class LocationInventory extends  BaseInventory {

  private static final long serialVersionUID = Version.MAJOR;

  public void addItem(Item item) {
    items.add(item);
    item.setInventory(this);
  }

  public void removeItem(Item item) {
    items.remove(item);
    item.setInventory(null);
  }

}
