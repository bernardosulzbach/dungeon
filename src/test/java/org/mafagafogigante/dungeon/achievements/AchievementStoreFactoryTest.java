package org.mafagafogigante.dungeon.achievements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class AchievementStoreFactoryTest {

  @Test
  public void testMakeAchievementStoreShouldReturnLockedStore() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    List<Achievement> emptyList = Collections.emptyList();
    AchievementStore achievementStore = AchievementStoreFactory.makeAchievementStore(emptyList);
    Assertions.assertThrows(IllegalStateException.class, () -> {
      achievementStore.addAchievement(achievementBuilder.createAchievement());
    });
  }

  @Test
  public void testDefaultStoreShouldBeLocked() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    AchievementStore defaultStore = AchievementStoreFactory.getDefaultStore();
    Assertions.assertThrows(IllegalStateException.class, () -> {
      defaultStore.addAchievement(achievementBuilder.createAchievement());
    });
  }

}
