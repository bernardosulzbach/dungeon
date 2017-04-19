package org.mafagafogigante.dungeon.entity.creatures;

import org.junit.Assert;
import org.junit.Test;

public class CreatureBattleDamageTest {

  @Test
  public void shouldResetCountersAfterGet() {
    final CreatureBattleDamage creatureBattleDamage = new CreatureBattleDamage();
    final long zeroValue = 0L;
    final long inflictedDamage = 3L;
    final long receivedDamage = 4L;
    creatureBattleDamage.incrementInflicted(inflictedDamage);
    creatureBattleDamage.incrementReceived(receivedDamage);
    Assert.assertEquals(inflictedDamage, creatureBattleDamage.getAndResetInflictedCount());
    Assert.assertEquals(zeroValue, creatureBattleDamage.getAndResetInflictedCount());
    Assert.assertEquals(receivedDamage, creatureBattleDamage.getAndResetReceivedCount());
    Assert.assertEquals(zeroValue, creatureBattleDamage.getAndResetReceivedCount());
  }

}
