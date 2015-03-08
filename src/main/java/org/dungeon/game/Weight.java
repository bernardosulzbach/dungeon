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

package org.dungeon.game;

import org.dungeon.io.DLogger;

import java.io.Serializable;

/**
 * Weight class that represents an amount of weight, in kilograms.
 * <p/>
 * Created by Bernardo on 07/03/2015.
 */
public class Weight implements Comparable<Weight>, Serializable {

  public static final Weight ZERO = newInstance(0);

  private final double value;

  private Weight(double value) {
    this.value = value;
  }

  public static Weight newInstance(double value) {
    if (value < 0) {
      DLogger.warning("Tried to create Weight from negative double.");
      return ZERO;
    }
    return new Weight(value);
  }

  /**
   * Produces a new Weight object by adding two existing ones.
   *
   * @param o a Weight object
   * @return a Weight object representing the sum
   */
  public Weight add(Weight o) {
    return newInstance(this.value + o.value);
  }

  public String toString() {
    return String.valueOf(value + " kg");
  }

  @Override
  public int compareTo(Weight o) {
    return Double.compare(value, o.value);
  }

}
