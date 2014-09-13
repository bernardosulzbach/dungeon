package game;

import java.io.Serializable;

/**
 * Item class that defines common properties for all items.
 *
 * @author bernardo
 */
public class Item implements Serializable {

    private static final String TYPE = "Generic";
    
    private String name;
    private boolean destructible;

    public Item(String name) {
        this.name = name;
        this.destructible = false;
    }

    public Item(String name, boolean destructible) {
        this.name = name;
        this.destructible = destructible;
    }

    public String getName() {
        return name;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }
    
    public String toShortString() {
        return String.format("[%s] %-20s", TYPE, getName());
    }
}
