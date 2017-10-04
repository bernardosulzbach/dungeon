package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;

/**
 * Two integer values with no reference to any entity.
 */
public class Integrity implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
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
