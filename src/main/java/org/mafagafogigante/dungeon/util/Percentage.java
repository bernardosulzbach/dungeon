package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * A class that represents a percentage value between 0.0% and 100.00%.
 */
public class Percentage implements Comparable<Percentage>, Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final double ONE = 1.0;
  private static final double ZERO = 0.0;

  private final double value;

  /**
   * Constructs a percentage from a double between 0 and 1.
   */
  public Percentage(double percentage) {
    if (DungeonMath.fuzzyCompare(percentage, ZERO) < 0) {
      value = ZERO;
      DungeonLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ZERO + " instead.");
    } else if (DungeonMath.fuzzyCompare(percentage, ONE) > 0) {
      value = ONE;
      DungeonLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ONE + " instead.");
    } else {
      value = percentage;
    }
  }

  /**
   * Creates a Percentage object from a String representation of a Percentage.
   *
   * <p>Percentage.isValidPercentageString(String) should return true to the provided String.
   *
   * @param percentage the String representation of a valid percentage
   * @return a Percentage object
   */
  public static Percentage fromString(String percentage) {
    if (!isValidPercentageString(percentage)) {
      throw new IllegalArgumentException("Provided String is not a valid percentage: " + percentage + "!");
    }
    return new Percentage(doubleFromPercentageString(percentage));
  }

  /**
   * Checks if a String is a valid percentage string.
   */
  public static boolean isValidPercentageString(String percentage) {
    if (percentage != null) {
      try {
        return isValidPercentageDouble(doubleFromPercentageString(percentage));
      } catch (NumberFormatException e) {
        // Fall to false.
      }
    }
    return false;
  }

  private static double doubleFromPercentageString(String percentage) {
    return Double.parseDouble(trimAndDiscardLastCharacter(percentage)) / 100;
  }

  private static boolean isValidPercentageDouble(double value) {
    return DungeonMath.fuzzyCompare(value, ZERO) >= 0 && DungeonMath.fuzzyCompare(value, ONE) <= 0;
  }

  private static String trimAndDiscardLastCharacter(String string) {
    String trimmed = string.trim();
    return trimmed.substring(0, trimmed.length() - 1);
  }

  public double toDouble() {
    return value;
  }

  public Percentage add(Percentage extraProficiency) {
    return new Percentage(Math.min(value + extraProficiency.value, ONE));
  }

  public Percentage multiply(Percentage percentage) {
    return new Percentage(toDouble() * percentage.toDouble());
  }

  @Override
  public int compareTo(@NotNull Percentage percentage) {
    return DungeonMath.fuzzyCompare(toDouble(), percentage.toDouble());
  }

  boolean biggerThanOrEqualTo(Percentage percentage) {
    return compareTo(percentage) >= 0;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    return compareTo((Percentage) object) == 0;
  }

  @Override
  public int hashCode() {
    long temp = Double.doubleToLongBits(value);
    return (int) (temp ^ (temp >>> 32));
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "%.2f%%", value * 100);
  }

}
