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

import org.dungeon.core.game.Selectable;

import java.io.Serializable;

/**
 * Item abstract class that defines common properties for all items.
 *
 * @author Bernardo Sulzbach
 *         <p/>
 *         Change log
 *         Sulzbach, 18/09/2014: added the type field.
 */
public abstract class Item implements Cloneable, Serializable, Selectable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String type;
    private boolean destructible;

    public static Item createItem(FoodPreset preset) {
        return Food.createFood(preset);
    }

    public static Item createItem(SimpleWeaponPreset preset) {
        return SimpleWeapon.createWeapon(preset);
    }

    protected Item(String name, String type) {
        this(name, type, true);
    }

    protected Item(String name, String type, boolean destructible) {
        this.name = name;
        this.type = type;
        this.destructible = destructible;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public String toSelectionEntry() {
        return String.format("%-12s%-24s", String.format("[%s]", getType()), getName());
    }
}
