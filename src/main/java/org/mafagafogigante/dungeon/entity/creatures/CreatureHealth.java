package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The health of a creature.
 */
public class CreatureHealth implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Integrity integrity;
  private final Creature creature;

  private CreatureHealth(int health, @NotNull Creature creature) {
    this.integrity = new Integrity(health, health);
    this.creature = creature;
  }

  public static CreatureHealth makeCreatureHealth(int health, @NotNull Creature creature) {
    return new CreatureHealth(health, creature);
  }

  public HealthState getHealthState() {
    double fraction = getCurrent() / (double) getMaximum();
    return HealthState.values()[(int) ((HealthState.values().length - 1) * (1 - fraction))];
  }

  public int getMaximum() { // Convenience that avoids getHealth().getHealth() in the code.
    return integrity.getMaximum();
  }

  public int getCurrent() { // Convenience that avoids getHealth().getHealth() in the code.
    return integrity.getCurrent();
  }

  public boolean isFull() {
    return integrity.isMaximum();
  }

  public boolean isAlive() {
    return !isDead();
  }

  /**
   * Returns whether or not this CreatureHealth represents the health of a dead creature.
   *
   * @return true if the current health is zero
   */
  public boolean isDead() {
    return integrity.isZero();
  }

  public Percentage toPercentage() {
    return integrity.toPercentage();
  }

  /**
   * Increments the current health by the specified amount.
   *
   * @param amount a nonnegative integer
   */
  public void incrementBy(int amount) { // Convenience that avoids getHealth().getHealth() in the code.
    integrity.incrementBy(amount);
  }

  /**
   * Decrements the current health by the specified amount. If the Creature ends up dead, it is passed to DeathHandler.
   *
   * @param amount a nonnegative integer
   */
  public void decrementBy(int amount) { // Must exist. After delegating the decrement, this method checks for death.
    integrity.decrementBy(amount);
    if (isDead()) {
      DeathHandler.handleDeath(creature);
    }
  }

  @Override
  public String toString() {
    return "CreatureHealth{" +
        "integrity=" + integrity +
        '}';
  }

}
