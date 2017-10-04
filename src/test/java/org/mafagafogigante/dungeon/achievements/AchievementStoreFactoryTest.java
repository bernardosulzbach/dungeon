package org.mafagafogigante.dungeon.achievements;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class AchievementStoreFactoryTest {

  @Test(expected = IllegalStateException.class)
  public void testMakeAchievementStoreShouldReturnLockedStore() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    List<Achievement> emptyList = Collections.emptyList();
    AchievementStore achievementStore = AchievementStoreFactory.makeAchievementStore(emptyList);
    achievementStore.addAchievement(achievementBuilder.createAchievement());
  }

  @Test(expected = IllegalStateException.class)
  public void testDefaultStoreShouldBeLocked() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    AchievementStore defaultStore = AchievementStoreFactory.getDefaultStore();
    defaultStore.addAchievement(achievementBuilder.createAchievement());
  }

}
