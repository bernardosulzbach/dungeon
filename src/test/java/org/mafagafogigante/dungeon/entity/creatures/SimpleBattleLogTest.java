package org.mafagafogigante.dungeon.entity.creatures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleBattleLogTest {

  @Test
  public void testGetAndResetShouldResetCounters() {
    SimpleBattleLog simpleBattleLog = new SimpleBattleLog();
    long inflictedDamage = 1L;
    long receivedDamage = 2L;
    simpleBattleLog.incrementInflicted(inflictedDamage);
    simpleBattleLog.incrementTaken(receivedDamage);
    Assertions.assertEquals(inflictedDamage, simpleBattleLog.getAndResetInflicted());
    Assertions.assertEquals(0L, simpleBattleLog.getAndResetInflicted());
    Assertions.assertEquals(receivedDamage, simpleBattleLog.getAndResetTaken());
    Assertions.assertEquals(0L, simpleBattleLog.getAndResetTaken());
  }

}
