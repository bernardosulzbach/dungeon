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

package org.dungeon.entity.items;

import org.dungeon.date.Date;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.game.GameData;
import org.dungeon.game.Id;

import org.jetbrains.annotations.NotNull;

/**
 * Provides methods to create different items for the game.
 */
public abstract class ItemFactory {

  /**
   * Attempts to create an item from the ItemPreset specified by an ID with the provided creation date.
   *
   * @param id the ID of the preset, not null
   * @param date the creation date of the item, not null
   * @return an Item with the specified creation date or null if the preset could not be found
   */
  public static Item makeItem(@NotNull Id id, @NotNull Date date) {
    ItemPreset itemPreset = GameData.getItemPresets().get(id);
    if (itemPreset != null) {
      return new Item(itemPreset, date);
    } else {
      return null;
    }
  }

  /**
   * Makes a corpse from the provided Creature. The creation Date of the corpse should be the Date of death.
   *
   * @param creature the Creature object
   * @param date the Date when the Creature died
   * @return an Item that represents the corpse of the Creature
   */
  public static Item makeCorpse(Creature creature, Date date) {
    if (!creature.hasTag(Creature.Tag.CORPSE)) {
      throw new AssertionError("Called makeCorpse for Creature that does not have the CORPSE tag!");
    }
    return makeItem(makeCorpseIDFromCreatureId(creature.getId()), date);
  }

  /**
   * Given a Creature ID, this method returns the corresponding corpse's ID.
   */
  public static Id makeCorpseIDFromCreatureId(Id id) {
    return new Id(id + "_CORPSE");
  }

}
