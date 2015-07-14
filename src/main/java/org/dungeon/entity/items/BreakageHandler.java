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

import org.dungeon.game.Game;

import org.jetbrains.annotations.NotNull;

/**
 * Uninstantiable class that handles item breakage via a handleBreakage(Item) method.
 */
public final class BreakageHandler {

  private BreakageHandler() {
  }

  /**
   * Handles an item breakage.
   *
   * @param item the Item object that just broke, not null, broken
   */
  public static void handleBreakage(@NotNull Item item) {
    if (!item.isBroken()) {
      throw new IllegalArgumentException("item should be broken.");
    }
    if (!item.hasTag(Item.Tag.REPAIRABLE)) {
      item.getInventory().removeItem(item);
      return; // The Item object will disappear from the game, don't worry about its state.
    }
    if (item.hasTag(Item.Tag.CLOCK)) {
      // A clock just broke! Update its last time record.
      item.getClockComponent().setLastTime(Game.getGameState().getWorld().getWorldDate());
    }
  }

}
