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

package org.dungeon.entity;

import org.dungeon.entity.items.BreakageHandler;
import org.dungeon.entity.items.Item;
import org.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The integrity of an entity.
 */
public class Integrity implements Serializable {

  private final int maximum;
  private final Item item;
  private int current;

  private Integrity(int maximum, int current, @NotNull Item item) {
    if (maximum < 1) {
      throw new IllegalArgumentException("maximum should be positive.");
    }
    if (current < 0) {
      throw new IllegalArgumentException("current should be nonnegative.");
    }
    if (maximum < current) {
      throw new IllegalArgumentException("current should be greater than or equal to maximum.");
    }
    this.maximum = maximum;
    this.current = current;
    this.item = item;
  }

  /**
   * Makes a new Integrity object with the specified maximum and current integrities.
   *
   * @param maximum the maximum integrity, positive
   * @param current the current integrity, nonnegative, smaller than or equal to maximum
   * @param item    the Item which this integrity refers to, not null
   * @return an Integrity object
   */
  public static Integrity makeIntegrity(int maximum, int current, @NotNull Item item) {
    return new Integrity(maximum, current, item);
  }

  public int getMaximum() {
    return maximum;
  }

  public int getCurrent() {
    return current;
  }

  /**
   * Returns whether or not this Integrity represents the integrity of a broken entity.
   *
   * @return true if the current integrity is zero
   */
  public boolean isBroken() {
    return getCurrent() == 0;
  }

  public Percentage toPercentage() {
    return new Percentage(getCurrent() / (double) getMaximum());
  }

  /**
   * Increments the current integrity by the specified amount.
   *
   * @param amount a nonnegative integer
   */
  public void incrementBy(int amount) {
    current = Math.min(current + amount, maximum);
  }

  /**
   * Decrements the current integrity by the specified amount.
   *
   * @param amount a nonnegative integer
   */
  public void decrementBy(int amount) {
    current = Math.max(current - amount, 0);
    if (isBroken()) {
      BreakageHandler.handleBreakage(item);
    }
  }

  @Override
  public String toString() {
    return String.format("%d/%d", current, maximum);
  }

}
