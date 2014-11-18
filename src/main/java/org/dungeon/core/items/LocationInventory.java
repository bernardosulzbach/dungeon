package org.dungeon.core.items;

/**
 * The inventory used by Location objects.
 *
 * Created by Bernardo Sulzbach on 15/11/14.
 */
public class LocationInventory extends BaseInventory {

    @Override
    public void addItem(Item item) {
        items.add(item);
        item.setOwner(null);
    }

    @Override
    public void removeItem(Item item) {
        items.remove(item);
    }

}
