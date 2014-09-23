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

package org.dungeon.core.test;

public class Item {

    // Identification fields.
    private String id;
    private String type;
    private String name;

    // Durability fields.
    private int maxIntegrity;
    private int curIntegrity;
    private boolean repairable;

    // Weapon fields.
    private boolean weapon;
    private int damage;
    private double hitRate;
    private int integrityDecrementOnHit;

    // Food fields.
    private boolean food;
    private int nutrition;
    private int integrityDecrementOnEat;

    private Item() {

    }

    public static Item createItem(ItemPreset preset) {
        Item result = new Item();

        result.id = preset.id;
        result.type = preset.type;
        result.name = preset.name;

        result.repairable = preset.repairable;
        result.maxIntegrity = preset.maxIntegrity;
        result.curIntegrity = preset.curIntegrity;

        result.weapon = preset.weapon;
        result.damage = preset.damage;
        result.hitRate = preset.hitRate;
        result.integrityDecrementOnHit = preset.integrityDecrementOnHit;

        result.food = preset.food;
        result.nutrition = preset.nutrition;
        result.integrityDecrementOnEat = preset.integrityDecrementOnEat;

        return result;
    }

    public int getIntegrity() {
        return curIntegrity;
    }
}
