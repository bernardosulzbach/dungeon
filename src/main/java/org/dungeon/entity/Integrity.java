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

import org.dungeon.util.Percentage;

import java.io.Serializable;

/**
 * Two integer values with no reference to any entity.
 */
public class Integrity implements Serializable {

  private final int maximum;
  private int current;

  /**
   * Constructs a new Integrity object with the specified maximum and current integrities.
   *
   * @param maximum the maximum integrity, positive
   * @param current the current integrity, nonnegative, smaller than or equal to maximum
   */
  public Integrity(int maximum, int current) {
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
  }

  /**
   * Copy constructor.
   */
  public Integrity(Integrity integrity) {
    maximum = integrity.maximum;
    current = integrity.current;
  }

  public int getMaximum() {
    return maximum;
  }

  public int getCurrent() {
    return current;
  }

  /**
   * Safely sets current to the provided value.
   *
   * @param current the new current value, can be more than maximum or less than zero
   */
  private void setCurrent(int current) {
    this.current = Math.max(Math.min(maximum, current), 0);
  }

  public boolean isMaximum() {
    return getCurrent() == getMaximum();
  }

  public boolean isZero() {
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
    setCurrent(current + amount);
  }

  /**
   * Decrements the current integrity by the specified amount.
   *
   * @param amount a nonnegative integer
   */
  public void decrementBy(int amount) {
    setCurrent(current - amount);
  }

  @Override
  public String toString() {
    return String.format("%d/%d", current, maximum);
  }

}
