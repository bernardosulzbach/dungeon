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

import org.dungeon.entity.Integrity;
import org.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The integrity of an item.
 */
public class ItemIntegrity implements Serializable {

  private final Integrity integrity;
  private final Item item;

  private ItemIntegrity(@NotNull Integrity integrity, @NotNull Item item) {
    this.integrity = new Integrity(integrity);
    this.item = item;
  }

  /**
   * Makes a new ItemIntegrity object for the specified item from the provided Integrity.
   */
  public static ItemIntegrity makeItemIntegrity(@NotNull Integrity integrity, @NotNull Item item) {
    return new ItemIntegrity(integrity, item);
  }

  public int getMaximum() { // Convenience that avoids getIntegrity().getIntegrity() in the code.
    return integrity.getMaximum();
  }

  public int getCurrent() { // Convenience that avoids getIntegrity().getIntegrity() in the code.
    return integrity.getCurrent();
  }

  /**
   * Returns whether or not this ItemIntegrity represents the integrity of a broken item.
   *
   * @return true if the current integrity is zero
   */
  public boolean isBroken() {
    return integrity.isZero();
  }

  public Percentage toPercentage() {
    return integrity.toPercentage();
  }

  /**
   * Increments the current integrity by the specified amount.
   *
   * @param amount a nonnegative integer
   */
  public void incrementBy(int amount) { // Convenience that avoids getIntegrity().getIntegrity() in the code.
    integrity.incrementBy(amount);
  }

  /**
   * Decrements the current integrity by the specified amount. If the Item ends up broken, it is passed to
   * BreakageHandler.
   *
   * @param amount a nonnegative integer
   */
  public void decrementBy(int amount) { // Must exist. After delegating the decrement, this method checks for breakage.
    integrity.decrementBy(amount);
    if (isBroken()) {
      BreakageHandler.handleBreakage(item);
    }
  }

  @Override
  public String toString() {
    return "ItemIntegrity{" +
        "integrity=" + integrity +
        ", item=" + item +
        '}';
  }

}
