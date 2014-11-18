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
        } else if (selectionResult.size() == 1) {
            return selectionResult.getMatch(0);
        } else {
            Utils.printAmbiguousSelectionMessage();
        }
        return null;
    }

}
