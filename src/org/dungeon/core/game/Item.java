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
package org.dungeon.core.game;

import java.io.Serializable;

/**
 * Item class that defines common properties for all items.
 *
 * @author Bernardo Sulzbach
 */
public abstract class Item implements Serializable, Selectable {

    private static final String TYPE = "Generic";

    private String name;
    private boolean destructible;

    public Item(String name) {
        this.name = name;
        this.destructible = false;
    }

    public Item(String name, boolean destructible) {
        this.name = name;
        this.destructible = destructible;
    }

    public String getName() {
        return name;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public String toShortString() {
        return String.format("[%s] %-20s", TYPE, getName());
    }
}
