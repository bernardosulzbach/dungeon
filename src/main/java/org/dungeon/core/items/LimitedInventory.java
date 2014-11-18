package org.dungeon.core.items;

public interface LimitedInventory {

    public int getLimit();

    public void setLimit(int limit);

    public boolean isFull();

}
