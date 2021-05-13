package org.mafagafogigante.dungeon.stats;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeroStatisticsTest {

  @Test
  public void shouldIncrementStatisticValues() {
    HeroStatistics heroStatistics = new HeroStatistics();
    long damageInflicted = 1L;
    heroStatistics.incrementDamageInflicted(damageInflicted);
    heroStatistics.incrementDamageInflicted(damageInflicted);
    Assertions.assertEquals(damageInflicted + damageInflicted, heroStatistics.getDamageInflicted());

    long damageTaken = 2L;
    heroStatistics.incrementDamageTaken(damageTaken);
    heroStatistics.incrementDamageTaken(damageTaken);
    Assertions.assertEquals(damageTaken + damageTaken, heroStatistics.getDamageTaken());

    long healingThroughEating = 3L;
    heroStatistics.incrementHealingThroughEating(healingThroughEating);
    heroStatistics.incrementHealingThroughEating(healingThroughEating);
    Assertions.assertEquals(healingThroughEating + healingThroughEating, heroStatistics.getHealingThroughEating());

    // We cannot check for absolute values (because time values are transformed internally), only for relations.
    long sleepingTimeIncrement = 4L;
    long initialSleepingTime = heroStatistics.getSleepingTime();
    heroStatistics.incrementSleepingTime(sleepingTimeIncrement);
    long sleepingTimeAfterOneIncrement = heroStatistics.getSleepingTime();
    MatcherAssert.assertThat(sleepingTimeAfterOneIncrement, Matchers.greaterThan(initialSleepingTime));
    heroStatistics.incrementSleepingTime(sleepingTimeIncrement);
    long sleepingTimeAfterTwoIncrements = heroStatistics.getSleepingTime();
    MatcherAssert.assertThat(sleepingTimeAfterTwoIncrements, Matchers.greaterThan(sleepingTimeAfterOneIncrement));

    long restingTimeIncrement = 5L;
    long initialRestingTime = heroStatistics.getRestingTime();
    heroStatistics.incrementRestingTime(restingTimeIncrement);
    long restingTimeAfterOneIncrement = heroStatistics.getRestingTime();
    MatcherAssert.assertThat(restingTimeAfterOneIncrement, Matchers.greaterThan(initialRestingTime));
    heroStatistics.incrementRestingTime(restingTimeIncrement);
    long restingTimeAfterTwoIncrements = heroStatistics.getRestingTime();
    MatcherAssert.assertThat(restingTimeAfterTwoIncrements, Matchers.greaterThan(restingTimeAfterOneIncrement));
  }

}
