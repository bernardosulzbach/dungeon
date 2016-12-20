package org.mafagafogigante.dungeon.achievements;

import org.junit.Test;

public class AchievementStoreTest {

  @Test(expected = IllegalStateException.class)
  public void testAddAchievementShouldFailIfLocked() throws Exception {
    // Not the right way to instantiate an AchievementStore outside the factory, used only for testing.
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("A");
    AchievementStore achievementStore = new AchievementStore();
    achievementStore.addAchievement(achievementBuilder.createAchievement());
    achievementStore.lock();
    achievementBuilder.setId("B");
    achievementStore.addAchievement(achievementBuilder.createAchievement());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddAchievementShouldFailForRepeatedId() throws Exception {
    // Not the right way to instantiate an AchievementStore outside the factory, used only for testing.
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("A");
    AchievementStore achievementStore = new AchievementStore();
    achievementStore.addAchievement(achievementBuilder.createAchievement());
    achievementStore.addAchievement(achievementBuilder.createAchievement());
  }

}
