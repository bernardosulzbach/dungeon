package org.mafagafogigante.dungeon.entity.creatures;

import java.io.Serializable;

/**
 * The damage counters of a creature.
 */
public class CreatureBattleDamage implements Serializable {

  private long receivedCount;
  private long inflictedCount;

  /**
   * Increments the received damage by the specified amount.
   *
   * @param receivedDamage a nonnegative long
   */
  public void incrementReceived(long receivedDamage) {
    this.receivedCount += receivedDamage;
  }

  /**
   * Increments the inflicted damage by the specified amount.
   *
   * @param inflictedDamage a nonnegative long
   */
  public void incrementInflicted(long inflictedDamage) {
    this.inflictedCount += inflictedDamage;
  }

  /**
   * Gets and resets received damage count.
   */
  public long getAndResetReceivedCount() {
    try {
      return receivedCount;
    } finally {
      receivedCount = 0L;
    }
  }

  /**
   * Gets and resets inflicted damage count.
   */
  public long getAndResetInflictedCount() {
    try {
      return inflictedCount;
    } finally {
      inflictedCount = 0L;
    }
  }

  @Override
  public String toString() {
    return "CreatureBattleDamage{" +
        "receivedCount=" + receivedCount +
        ", inflictedCount=" + inflictedCount +
        '}';
  }

}
