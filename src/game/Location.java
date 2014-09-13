package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Location implements Serializable {

    private final String name;
    private final List<Creature> creatures;
    private final List<Item> items;

    public Location(String name) {
        creatures = new ArrayList<>();
        items = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addCreature(Creature creature) {
        creatures.add(creature);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Attempts to find an item by its name.
     *
     * @param name
     * @return an Item object if there is a match. null otherwise.
     */
    public Item findItem(String name) {
        for (Item curItem : items) {
            if (curItem.getName().equalsIgnoreCase(name)) {
                return curItem;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Remove all dead creatures in the current location.
     */
    public void removeAllDeadCreatures() {
        // To safely remove from a collection we must use an iterator.
        Iterator<Creature> i = creatures.iterator();
        while (i.hasNext()) {
            Creature creature = i.next();
            if (!creature.isAlive()) {
                i.remove();
            }
        }
    }

    /**
     * Retrieves all visible creatures to the observer.
     *
     * @param observer the creature that is trying to look for other creatures.
     * @return a list of all the visible creatures to this observer.
     */
    public List<Creature> getVisibleCreatures(Creature observer) {
        List<Creature> allButObserver = creatures;
        allButObserver.remove(observer);
        return allButObserver;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getCreatureCount() {
        return creatures.size();
    }

    public int getItemCount() {
        return items.size();
    }

    /**
     * Get a list of all visible weapons to the observer.
     *
     * @return a list of weapons.
     */
    public List<Weapon> getVisibleWeapons() {
        List<Weapon> visibleWeapons = new ArrayList<>();
        for (Item visibleItem : items) {
            if (visibleItem instanceof Weapon) {
                visibleWeapons.add((Weapon) visibleItem);
            }
        }
        return visibleWeapons;
    }

    /**
     * Get a list of all visible items to the observer.
     *
     * @return a list of items.
     */
    public List<Item> getVisibleItems() {
        return items;
    }
}
