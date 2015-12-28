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