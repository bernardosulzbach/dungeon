package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.io.Version;
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

  private static final long serialVersionUID = Version.MAJOR;
  public static final Weight ZERO = newInstance(0.0);
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
    if (value < 0.0) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Weight weight = (Weight) o;
    return Double.compare(weight.value, value) == 0;
  }

  @Override
  public int hashCode() {
    long temp = Double.doubleToLongBits(value);
    return (int) (temp ^ (temp >>> 32));
  }

  @Override
  public String toString() {
    return WEIGHT_FORMAT.format(value);
  }

}
