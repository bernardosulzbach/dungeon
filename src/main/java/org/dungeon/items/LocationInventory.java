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
package org.dungeon.items;

/**
 * The inventory used by Location objects.
 * <p/>
 * Created by Bernardo Sulzbach on 15/11/14.
 */
public class LocationInventory extends BaseInventory {

    @Override
    public boolean addItem(Item item) {
        items.add(item);
        item.setOwner(null);
        return true;
    }

    @Override
    public void removeItem(Item item) {
        items.remove(item);
    }

}
