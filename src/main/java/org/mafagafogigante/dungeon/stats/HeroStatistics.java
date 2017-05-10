package org.mafagafogigante.dungeon.stats;

import java.io.Serializable;

/**
 * HeroStatistics class that tracks the Hero's state statistics.
 */
public final class HeroStatistics implements Serializable {

  private long sleepTimeCount;
  private long damageReceivedCount;
  private long damageInflictedCount;
  private long restTimeCount;
  private long healByEatCount;

  public void incrementDamageInflictedCount(long damage) {
    damageInflictedCount += damage;
  }

  public void incrementDamageReceivedCount(long damage) {
    damageReceivedCount += damage;
  }

  public void incrementRestTimeCount(long resting) {
    restTimeCount += resting;
  }

  public void incrementHealByEatCount(long healing) {
    healByEatCount += healing;
  }

  public void incrementSleepTimeCount(long sleepTime) {
    sleepTimeCount += sleepTime;
  }

  public long getSleepTimeCount() {
    return sleepTimeCount;
  }

  public long getDamageReceivedCount() {
    return damageReceivedCount;
  }

  public long getDamageInflictedCount() {
    return damageInflictedCount;
  }

  public long getRestTimeCount() {
    return restTimeCount;
  }

  public long getHealByEatCount() {
    return healByEatCount;
  }

}
