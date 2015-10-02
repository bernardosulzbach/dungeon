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

import org.dungeon.date.Date;
import org.dungeon.game.DungeonStringBuilder;
import org.dungeon.game.Game;
import org.dungeon.game.Id;
import org.dungeon.io.Writer;
import org.dungeon.logging.DungeonLogger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AchievementTracker that tracks the unlocked achievements.
 */
public class AchievementTracker implements Serializable {

  private final Set<UnlockedAchievement> unlockedAchievements = new HashSet<UnlockedAchievement>();

  /**
   * Writes an achievement unlocked message with some information about the unlocked achievement.
   */
  private static void writeAchievementUnlock(Achievement achievement, DungeonStringBuilder builder) {
    String format = "You unlocked the achievement %s because you %s.";
    builder.append(String.format(format, achievement.getName(), achievement.getText()));
  }

  /**
   * Returns how many unlocked achievements there are in this AchievementTracker.
   *
   * @return how many unlocked achievements there are in this AchievementTracker
   */
  public int getUnlockedCount() {
    return unlockedAchievements.size();
  }

  /**
   * Unlock a specific Achievement.
   *
   * <p>If there already is an UnlockedAchievement with the same ID, a warning will be logged.
   *
   * @param achievement the Achievement to be unlocked.
   */
  private void unlock(Achievement achievement, DungeonStringBuilder builder) {
    Date now = Game.getGameState().getWorld().getWorldDate();
    if (!isUnlocked(achievement)) {
      writeAchievementUnlock(achievement, builder);
      unlockedAchievements.add(new UnlockedAchievement(achievement, now));
    } else {
      DungeonLogger.warning("Tried to unlock an already unlocked achievement.");
    }
  }

  /**
   * Return the UnlockedAchievement object that corresponds to a specific Achievement.
   *
   * @param achievement an Achievement object.
   * @return the UnlockedAchievement that corresponds to this Achievement.
   */
  private UnlockedAchievement getUnlockedAchievement(Achievement achievement) {
    Id id = achievement.getId();
    for (UnlockedAchievement ua : unlockedAchievements) {
      if (ua.id.equals(id)) {
        return ua;
      }
    }
    return null;
  }

  /**
   * Returns a List with all the UnlockedAchievements in this AchievementTracker sorted using the provided Comparator.
   *
   * @param comparator a Comparator of UnlockedAchievements, not null
   * @return a sorted List with all the UnlockedAchievements in this AchievementTracker
   */
  public List<UnlockedAchievement> getUnlockedAchievements(Comparator<UnlockedAchievement> comparator) {
    if (comparator == null) {
      throw new IllegalArgumentException("comparator is null.");
    }
    List<UnlockedAchievement> list = new ArrayList<UnlockedAchievement>(unlockedAchievements);
    Collections.sort(list, comparator);
    return list;
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

  /**
   * Updates this AchievementTracker by iterating over the achievements and unlocking the ones that are fulfilled but
   * not yet added to the unlocked list of this tracker.
   *
   * <p>Before writing the first achievement unlock message, if there is one, a new line is written.
   */
  public void update() {
    DungeonStringBuilder builder = new DungeonStringBuilder();
    boolean wroteNewLine = false; // If we are going to write anything at all, we must start with a blank line.
    for (Achievement achievement : AchievementStore.getAchievements()) {
      if (!isUnlocked(achievement) && achievement.isFulfilled()) {
        if (!wroteNewLine) {
          builder.append("\n");
          wroteNewLine = true;
        }
        unlock(achievement, builder);
        builder.append("\n");
      }
    }
    Writer.write(builder);
  }

}
