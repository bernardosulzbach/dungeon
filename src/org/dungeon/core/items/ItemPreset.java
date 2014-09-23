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
    APPLE("APPLE", "Food", "Apple", 12, 12, false, true, 6, 0.7, 4, true, 8, 6),
    CHERRY("CHERRY", "Food", "Cherry", 4, 4, false, true, 2, 0.8, 4, true, 4, 4),
    SPEAR("SPEAR", "Weapon", "Spear", 80, 80, true, true, 10, 0.85, 1, false, 0, 0),
    WATERMELON("WATERMELON", "Food", "Watermelon", 40, 40, false, true, 8, 0.6, 8, true, 10, 10);

    // Identification fields.
    protected String id;
    protected String type;
    protected String name;

    // Durability fields.
    protected int maxIntegrity;
    protected int curIntegrity;
    protected boolean repairable;

    // Weapon fields.
    protected boolean weapon;
    protected int damage;
    protected double hitRate;
    protected int integrityDecrementOnHit;

    // Food fields.
    protected boolean food;
    protected int nutrition;
    protected int integrityDecrementOnEat;

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
               boolean food,
               int nutrition,
               int integrityDecrementOnEat) {
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
        this.food = food;
        this.nutrition = nutrition;
        this.integrityDecrementOnEat = integrityDecrementOnEat;
    }
}
