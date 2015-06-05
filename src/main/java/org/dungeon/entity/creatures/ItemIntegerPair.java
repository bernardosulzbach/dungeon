/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.dungeon.entity.creatures;

import org.dungeon.entity.items.Item;

/**
 * A convenience immutable class that represents a pair of an Item and an Integer.
 */
class ItemIntegerPair {

  private final Item item;
  private final Integer integer;

  ItemIntegerPair(Item item, Integer integer) {
    this.item = item;
    this.integer = integer;
  }

  public Item getItem() {
    return item;
  }

  public Integer getInteger() {
    return integer;
  }

}
