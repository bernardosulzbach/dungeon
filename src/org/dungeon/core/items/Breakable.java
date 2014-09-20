package org.dungeon.core.items;

public interface Breakable {
    boolean isBroken();
    void decrementIntegrity();
    void repair();
}
