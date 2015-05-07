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

package org.dungeon.items;

import org.dungeon.date.Date;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;

public abstract class ItemFactory {

  /**
   * Attempts to create an item from the ItemBlueprint specified by an ID. Returns null if no blueprint was found.
   */
  public static Item makeItem(ID id, Date date) {
    ItemBlueprint blueprint = GameData.getItemBlueprints().get(id);
    if (blueprint != null) {
      return new Item(blueprint, date);
    } else {
      return null;
    }
  }

}
