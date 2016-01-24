/*
 * Copyright (C) 2016 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;

import org.jetbrains.annotations.NotNull;

/**
 * Restrictions imposed upon an ItemFactory.
 *
 * <p>It is of the ItemFactory discretion to call {@code registerItem()} on an ItemFactoryRestriction.
 */
interface ItemFactoryRestrictions {

  /**
   * Evaluates if an item with the provided Id may be made.
   */
  boolean canMakeItem(@NotNull Id id);

  /**
   * Registers a new Item made by the ItemFactory.
   */
  void registerItem(@NotNull Id id);

}
