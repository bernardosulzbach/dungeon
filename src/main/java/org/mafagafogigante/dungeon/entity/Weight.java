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

package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Weight class that represents an amount of weight, in kilograms.
 */
public class Weight implements Comparable<Weight>, Serializable {

  public static final Weight ZERO = newInstance(0);
  private static final DecimalFormat WEIGHT_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.US);

  static {
    WEIGHT_FORMAT.applyPattern("0.### kg");
  }

  private final double value;

  private Weight(double value) {
    this.value = value;
  }

  /**
   * Returns a new Weight object from the provided value. The value must be nonnegative.
   */
  public static Weight newInstance(double value) {
    if (value < 0) {
      DungeonLogger.warning("Tried to create Weight from negative double.");
      return ZERO;
    }
    return new Weight(value);
  }

  /**
   * Produces a new Weight object by adding two existing ones.
   *
   * @param weight a Weight object
   * @return a Weight object representing the sum
   */
  public Weight add(Weight weight) {
    return newInstance(this.value + weight.value);
  }

  /**
   * Returns a Weight object that is equal to this Weight multiplied by a Percentage.
   *
   * @param percentage a Percentage object
   * @return a Weight object representing the relative value
   */
  public Weight multiply(Percentage percentage) {
    return newInstance(this.value * percentage.toDouble());
  }

  @Override
  public int compareTo(@NotNull Weight weight) {
    return Double.compare(value, weight.value);
  }

  @Override
  public String toString() {
    return WEIGHT_FORMAT.format(value);
  }

}
