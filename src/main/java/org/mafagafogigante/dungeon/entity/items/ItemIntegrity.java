package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The integrity of an item.
 */
public class ItemIntegrity implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
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

  public boolean isPerfect() {
    return integrity.isMaximum();
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
