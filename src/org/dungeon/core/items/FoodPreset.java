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
    CHERRY("Cherry", 5),
    APPLE("Apple", 10),
    WATERMELON("Watermelon", 15);

    protected final String name;
    protected final int nutrition;

    FoodPreset(String name, int nutrition) {
        this.name = name;
        this.nutrition = nutrition;
    }
}
