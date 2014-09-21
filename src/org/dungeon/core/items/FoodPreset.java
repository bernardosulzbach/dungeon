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

/**
 * Enumerated presets used to create foods.
 * <p/>
 * Change log
 * Created by Bernardo on 18/09/2014.
 */
public enum FoodPreset {
    CHERRY("Cherry", 2, 50, 4, 2, 8, 2),
    APPLE("Apple", 4, 40, 24, 8, 14, 2),
    WATERMELON("Watermelon", 10, 30, 40, 10, 26, 4);

    protected final String name;
    protected final int damage;
    protected final int missRate;
    protected final int integrity;
    protected final int decrement;
    protected final int nutrition;
    protected final int experience;

    FoodPreset(String name, int damage, int missRate, int integrity, int decrement, int nutrition, int experience) {
        this.name = name;
        this.damage = damage;
        this.missRate = missRate;
        this.integrity = integrity;
        this.decrement = decrement;
        this.nutrition = nutrition;
        this.experience = experience;
    }
}
