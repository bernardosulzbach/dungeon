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

package org.dungeon.entity.creatures;

import org.dungeon.entity.Integrity;
import org.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The health of a creature.
 */
public class CreatureHealth implements Serializable {

  private final Integrity integrity;
  private final Creature creature;

  private CreatureHealth(int health, @NotNull Creature creature) {
    this.integrity = new Integrity(health, health);
    this.creature = creature;
  }

  public static CreatureHealth makeCreatureIntegrity(int health, @NotNull Creature creature) {
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
