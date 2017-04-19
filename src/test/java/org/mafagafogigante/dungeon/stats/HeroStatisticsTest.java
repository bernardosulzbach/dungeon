package org.mafagafogigante.dungeon.stats;

import org.junit.Assert;
import org.junit.Test;

public class HeroStatisticsTest {

  @Test
  public void shouldIncrementStatisticValues() {
    final HeroStatistics heroStatistics = new HeroStatistics();
    final long damageInflictedValue = 3L;
    final long damageReceivedValue = 4L;
    final long healByEatValue = 5L;
    final long restTimeValue = 6L;
    final long sleepTimeValue = 7L;
    heroStatistics.incrementDamageInflictedCount(damageInflictedValue);
    heroStatistics.incrementDamageInflictedCount(damageInflictedValue);
    heroStatistics.incrementDamageReceivedCount(damageReceivedValue);
    heroStatistics.incrementDamageReceivedCount(damageReceivedValue);
    heroStatistics.incrementHealByEatCount(healByEatValue);
    heroStatistics.incrementHealByEatCount(healByEatValue);
    heroStatistics.incrementRestTimeCount(restTimeValue);
    heroStatistics.incrementRestTimeCount(restTimeValue);
    heroStatistics.incrementSleepTimeCount(sleepTimeValue);
    heroStatistics.incrementSleepTimeCount(sleepTimeValue);
    Assert.assertEquals(damageInflictedValue + damageInflictedValue, heroStatistics.getDamageInflictedCount());
    Assert.assertEquals(damageReceivedValue + damageReceivedValue, heroStatistics.getDamageReceivedCount());
    Assert.assertEquals(healByEatValue + healByEatValue, heroStatistics.getHealByEatCount());
    Assert.assertEquals(restTimeValue + restTimeValue, heroStatistics.getRestTimeCount());
    Assert.assertEquals(sleepTimeValue + sleepTimeValue, heroStatistics.getSleepTimeCount());
  }

}
