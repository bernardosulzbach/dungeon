/*
 * Copyright (C) 2015 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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