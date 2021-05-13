package org.mafagafogigante.dungeon.achievements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AchievementStoreTest {

  @Test
  public void testAddAchievementShouldFailIfLocked() throws Exception {
    // Not the right way to instantiate an AchievementStore outside the factory, used only for testing.
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("A");
    AchievementStore achievementStore = new AchievementStore();
    achievementStore.addAchievement(achievementBuilder.createAchievement());
    achievementStore.lock();
    achievementBuilder.setId("B");
    Assertions.assertThrows(IllegalStateException.class, () -> {
      achievementStore.addAchievement(achievementBuilder.createAchievement());
    });
  }

  @Test
  public void testAddAchievementShouldFailForRepeatedId() throws Exception {
    // Not the right way to instantiate an AchievementStore outside the factory, used only for testing.
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("A");
    AchievementStore achievementStore = new AchievementStore();
    achievementStore.addAchievement(achievementBuilder.createAchievement());
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      achievementStore.addAchievement(achievementBuilder.createAchievement());
    });
  }

}
