package org.dungeon.core.items;

/**
 * Created by Bernardo on 18/09/2014.
 */
public enum FoodPreset {
    CHERRY("Cherry", 5);

    protected final String name;
    protected final int nutrition;

    FoodPreset(String name, int nutrition) {
        this.name = name;
        this.nutrition = nutrition;
    }
}
