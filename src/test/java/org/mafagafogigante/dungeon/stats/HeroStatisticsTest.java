package org.mafagafogigante.dungeon.stats;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class HeroStatisticsTest {

  @Test
  public void shouldIncrementStatisticValues() {
    HeroStatistics heroStatistics = new HeroStatistics();
    long damageInflicted = 1L;
    heroStatistics.incrementDamageInflicted(damageInflicted);
    heroStatistics.incrementDamageInflicted(damageInflicted);
    Assert.assertEquals(damageInflicted + damageInflicted, heroStatistics.getDamageInflicted());

    long damageTaken = 2L;
    heroStatistics.incrementDamageTaken(damageTaken);
    heroStatistics.incrementDamageTaken(damageTaken);
    Assert.assertEquals(damageTaken + damageTaken, heroStatistics.getDamageTaken());

    long healingThroughEating = 3L;
    heroStatistics.incrementHealingThroughEating(healingThroughEating);
    heroStatistics.incrementHealingThroughEating(healingThroughEating);
    Assert.assertEquals(healingThroughEating + healingThroughEating, heroStatistics.getHealingThroughEating());

    // We cannot check for absolute values (because time values are transformed internally), only for relations.
    long sleepingTimeIncrement = 4L;
    long initialSleepingTime = heroStatistics.getSleepingTime();
    heroStatistics.incrementSleepingTime(sleepingTimeIncrement);
    long sleepingTimeAfterOneIncrement = heroStatistics.getSleepingTime();
    Assert.assertThat(sleepingTimeAfterOneIncrement, Matchers.greaterThan(initialSleepingTime));
    heroStatistics.incrementSleepingTime(sleepingTimeIncrement);
    long sleepingTimeAfterTwoIncrements = heroStatistics.getSleepingTime();
    Assert.assertThat(sleepingTimeAfterTwoIncrements, Matchers.greaterThan(sleepingTimeAfterOneIncrement));

    long restingTimeIncrement = 5L;
    long initialRestingTime = heroStatistics.getRestingTime();
    heroStatistics.incrementRestingTime(restingTimeIncrement);
    long restingTimeAfterOneIncrement = heroStatistics.getRestingTime();
    Assert.assertThat(restingTimeAfterOneIncrement, Matchers.greaterThan(initialRestingTime));
    heroStatistics.incrementRestingTime(restingTimeIncrement);
    long restingTimeAfterTwoIncrements = heroStatistics.getRestingTime();
    Assert.assertThat(restingTimeAfterTwoIncrements, Matchers.greaterThan(restingTimeAfterOneIncrement));
  }

}
