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

package org.dungeon.achievements;

import org.dungeon.achievements.comparators.UnlockedAchievementComparators;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.game.Game;
import org.dungeon.io.Writer;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;

/**
 * A class that parses IssuedCommands for writing unlocked achievements to the screen.
 */
public class AchievementTrackerWriter {

  /**
   * Parses an IssuedCommand that makes the game write to the screen all achievements the Hero has unlocked so far.
   *
   * @param issuedCommand an IssuedCommand, not null
   */
  public static void parseCommand(IssuedCommand issuedCommand) {
    Comparator<UnlockedAchievement> comparator = getComparator(issuedCommand);
    if (comparator != null) {
      AchievementTracker achievementTracker = Game.getGameState().getHero().getAchievementTracker();
      writeAchievementTracker(achievementTracker, comparator);
    } else {
      writeValidOrderings();
    }
  }

  /**
   * Parses an IssuedCommand trying to identify the specified ordering for the achievements. If nothing is specified,
   * the default comparator is returned. If an ordering specification is invalid ("by" followed by an unrecognized
   * ordering), this method returns null.
   *
   * @param issuedCommand an IssuedCommand, not null
   * @return a Comparator of UnlockedAchievements, or null
   */
  private static Comparator<UnlockedAchievement> getComparator(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments() && issuedCommand.getFirstArgument().equalsIgnoreCase("by")) {
      if (issuedCommand.getArguments().length > 1) {
        String secondArgument = issuedCommand.getArguments()[1];
        for (String comparatorName : UnlockedAchievementComparators.getComparatorMap().keySet()) {
          if (secondArgument.equalsIgnoreCase(comparatorName)) {
            return UnlockedAchievementComparators.getComparatorMap().get(comparatorName);
          }
        }
      }
      return null;
    } else {
      return UnlockedAchievementComparators.getDefaultComparator();
    }
  }

  /**
   * Writes an AchievementTracker UnlockedAchievements to the screen after sorting it with the specified Comparator.
   *
   * @param tracker the AchievementTracker, not null
   * @param comparator the Comparator, not null
   */
  private static void writeAchievementTracker(AchievementTracker tracker, Comparator<UnlockedAchievement> comparator) {
    List<UnlockedAchievement> unlockedAchievements = tracker.getUnlockedAchievements(comparator);
    Date now = Game.getGameState().getWorld().getWorldDate();
    for (UnlockedAchievement unlockedAchievement : unlockedAchievements) {
      Period sinceUnlock = new Period(unlockedAchievement.getDate(), now);
      Writer.writeString(String.format("%s (%s ago)", unlockedAchievement.getName(), sinceUnlock), Color.ORANGE);
      Writer.writeString(" " + unlockedAchievement.getInfo(), Color.YELLOW);
    }
    int total = AchievementStore.getAchievements().size();
    Writer.writeString(String.format("Progress: %d/%d", tracker.getUnlockedCount(), total), Color.CYAN);
  }

  /**
   * Writes a listing of valid UnlockedAchievement orderings to the screen.
   */
  private static void writeValidOrderings() {
    Writer.writeString("Valid orderings:");
    for (String comparatorName : UnlockedAchievementComparators.getComparatorMap().keySet()) {
      Writer.writeString(" " + comparatorName);
    }
  }

}
