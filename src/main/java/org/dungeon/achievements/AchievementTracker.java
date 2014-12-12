/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.dungeon.achievements;

import org.dungeon.game.Game;
import org.dungeon.io.DLogger;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * AchievementTracker that tracks the unlocked achievements.
 * <p/>
 * Created by Bernardo Sulzbach on 16/11/14.
 */
public class AchievementTracker implements Serializable {

  private final TreeSet<UnlockedAchievement> unlockedAchievements;

  public AchievementTracker() {
    this.unlockedAchievements = new TreeSet<UnlockedAchievement>(new UnlockedAchievementNameComparator());
  }

  /**
   * Returns how many unlocked achievements there are in this AchievementTracker.
   *
   * @return how many unlocked achievements there are in this AchievementTracker.
   */
  public int getUnlockedCount() {
    return unlockedAchievements.size();
  }

  /**
   * Unlock a specific Achievement.
   * <p/>
   * If there already is an UnlockedAchievement with the same ID, a warning will be logged.
   *
   * @param achievement the Achievement to be unlocked.
   */
  public void unlock(Achievement achievement) {
    DateTime now = Game.getGameState().getWorld().getWorldDate();
    if (!isUnlocked(achievement)) {
      unlockedAchievements.add(new UnlockedAchievement(achievement.getId(), achievement.getName(), now));
    } else {
      DLogger.warning("Tried to unlock an already unlocked achievement!");
    }
  }

  /**
   * Return the UnlockedAchievement object that corresponds to a specific Achievement.
   *
   * @param achievement an Achievement object.
   * @return the UnlockedAchievement that corresponds to this Achievement.
   */
  public UnlockedAchievement getUnlockedAchievement(Achievement achievement) {
    String id = achievement.getId();
    for (UnlockedAchievement ua : unlockedAchievements) {
      if (ua.id.equals(id)) {
        return ua;
      }
    }
    return null;
  }

  /**
   * Returns an array with all the unlocked achievements.
   *
   * @return an array with all the unlocked achievements.
   */
  public UnlockedAchievement[] getUnlockedAchievementArray() {
    return unlockedAchievements.toArray(new UnlockedAchievement[unlockedAchievements.size()]);
  }

  /**
   * Return true if a given Achievement is unlocked in this AchievementTracker.
   *
   * @param achievement an Achievement object.
   * @return true if this Achievement is unlocked, false otherwise.
   */
  public boolean isUnlocked(Achievement achievement) {
    return getUnlockedAchievement(achievement) != null;
  }

}
