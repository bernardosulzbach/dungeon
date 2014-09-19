package org.dungeon.core.items;

/**
 * Created by Bernardo on 18/09/2014.
 */
public enum WeaponPreset {
    SPEAR("Spear", 13, 5, 100, 1);

    protected final String name;
    protected final int damage;
    protected final int missRate;
    protected final int startingIntegrity;
    protected final int hitDecrement;

    WeaponPreset(String name, int damage, int missRate, int startingIntegrity, int hitDecrement) {
        this.name = name;
        this.damage = damage;
        this.missRate = missRate;
        this.startingIntegrity = startingIntegrity;
        this.hitDecrement = hitDecrement;
    }
}
