package org.mafagafogigante.dungeon.achievements;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class AchievementStoreFactoryTest {

  @Test
  public void testMakeAchievementStoreShouldReturnLockedStore() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    try {
      List<Achievement> emptyList = Collections.emptyList();
      AchievementStore achievementStore = AchievementStoreFactory.makeAchievementStore(emptyList);
      achievementStore.addAchievement(achievementBuilder.createAchievement());
      Assert.fail("expected AchievementStore to be locked");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testDefaultStoreShouldBeLocked() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    try {
      AchievementStore defaultStore = AchievementStoreFactory.getDefaultStore();
      defaultStore.addAchievement(achievementBuilder.createAchievement());
      Assert.fail("expected AchievementStore to be locked");
    } catch (IllegalStateException expected) {
    }
  }

}