/*
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.core.items;

public enum ItemPreset {
    APPLE("APPLE", "Food", "Apple", 12, 12, false, true, 6, 0.7, 4, new FoodComponent(8, 4, 6)),
    AXE("AXE", "Weapon", "Axe", 85, 85, false, true, 18, 0.8, 1, null),
    CHERRY("CHERRY", "Food", "Cherry", 4, 4, false, true, 2, 0.8, 4, new FoodComponent(4, 2, 4)),
    DAGGER("DAGGER", "Weapon", "Dagger", 80, 80, true, true, 12, 0.8, 1, null),
    LONGSWORD("LONGSWORD", "Weapon", "Longsword", 100, 100, true, true, 20, 0.85, 1, null),
    MACE("MACE", "Weapon", "Mace", 90, 90, true, true, 16, 0.8, 1, null),
    SPEAR("SPEAR", "Weapon", "Spear", 70, 70, true, true, 10, 0.9, 1, null),
    STAFF("STAFF", "Weapon", "Staff", 75, 75, true, true, 14, 0.75, 1, null),
    STICK("STICK", "Weapon", "Stick", 30, 30, true, true, 6, 0.8, 1, null),
    STONE("STONE", "Weapon", "Stone", 200, 200, true, true, 8, 0.7, 1, null),
    WATERMELON("WATERMELON", "Food", "Watermelon", 40, 40, false, true, 8, 0.6, 8, new FoodComponent(10, 8, 10));

    // Identification fields.
    protected final String id;
    protected final String type;
    protected final String name;

    // Durability fields.
    protected final int maxIntegrity;
    protected final int curIntegrity;
    protected final boolean repairable;

    // Weapon fields.
    protected final boolean weapon;
    protected final int damage;
    protected final double hitRate;
    protected final int integrityDecrementOnHit;

    // Food fields.
    protected final FoodComponent foodComponent;

    ItemPreset(String id,
               String type,
               String name,
               int maxIntegrity,
               int curIntegrity,
               boolean repairable,
               boolean weapon,
               int damage,
               double hitRate,
               int integrityDecrementOnHit,
               FoodComponent foodComponent) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.maxIntegrity = maxIntegrity;
        this.curIntegrity = curIntegrity;
        this.repairable = repairable;
        this.weapon = weapon;
        this.damage = damage;
        this.hitRate = hitRate;
        this.integrityDecrementOnHit = integrityDecrementOnHit;
        this.foodComponent = foodComponent;
    }

    public String getId() {
        return this.id;
    }
}
