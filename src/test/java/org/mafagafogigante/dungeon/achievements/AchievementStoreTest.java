package org.mafagafogigante.dungeon.achievements;

import org.junit.Assert;
import org.junit.Test;

public class AchievementStoreTest {

  @Test
  public void testAddAchievementShouldFailIfLocked() throws Exception {
    // Not the right way to instantiate an AchievementStore outside the factory, used only for testing.
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    AchievementStore achievementStore = new AchievementStore();
    achievementStore.addAchievement(achievementBuilder.createAchievement());
    achievementStore.lock();
    achievementBuilder.setId("ACHIEVEMENT_2");
    try {
      achievementStore.addAchievement(achievementBuilder.createAchievement());
      Assert.fail("expected AchievementStore to throw an IllegalStateException for being locked");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testAddAchievementShouldFailForRepeatedId() throws Exception {
    // Not the right way to instantiate an AchievementStore outside the factory, used only for testing.
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    AchievementStore achievementStore = new AchievementStore();
    achievementStore.addAchievement(achievementBuilder.createAchievement());
    try {
      achievementStore.addAchievement(achievementBuilder.createAchievement());
      Assert.fail("expected AchievementStore to throw an IllegalArgumentException on repeated Id");
    } catch (IllegalArgumentException expected) {
    }
  }

}