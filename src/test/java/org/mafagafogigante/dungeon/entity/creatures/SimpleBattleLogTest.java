package org.mafagafogigante.dungeon.entity.creatures;

import org.junit.Assert;
import org.junit.Test;

public class SimpleBattleLogTest {

  @Test
  public void testGetAndResetShouldResetCounters() {
    SimpleBattleLog simpleBattleLog = new SimpleBattleLog();
    long inflictedDamage = 1L;
    long receivedDamage = 2L;
    simpleBattleLog.incrementInflicted(inflictedDamage);
    simpleBattleLog.incrementTaken(receivedDamage);
    Assert.assertEquals(inflictedDamage, simpleBattleLog.getAndResetInflicted());
    Assert.assertEquals(0L, simpleBattleLog.getAndResetInflicted());
    Assert.assertEquals(receivedDamage, simpleBattleLog.getAndResetTaken());
    Assert.assertEquals(0L, simpleBattleLog.getAndResetTaken());
  }

}
