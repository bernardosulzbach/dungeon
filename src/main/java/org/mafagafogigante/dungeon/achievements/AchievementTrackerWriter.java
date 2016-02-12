package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.achievements.comparators.UnlockedAchievementComparators;
import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.io.Writer;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;

/**
 * A class that parses IssuedCommands for writing unlocked achievements to the screen.
 */
public class AchievementTrackerWriter {
  /**
   * Parses an issued command that makes the game write to the screen all achievements the Hero has unlocked so far.
   */
  public static void parseCommand(String[] arguments) {
    Comparator<UnlockedAchievement> comparator = getComparator(arguments);
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
   * @return a Comparator of UnlockedAchievements, or null
   */
  private static Comparator<UnlockedAchievement> getComparator(String[] arguments) {
    if (arguments.length > 0 && arguments[0].equalsIgnoreCase("by")) {
      for (String comparatorName : UnlockedAchievementComparators.getComparatorMap().keySet()) {
        if (arguments[1].equalsIgnoreCase(comparatorName)) {
          return UnlockedAchievementComparators.getComparatorMap().get(comparatorName);
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
    DungeonString string = new DungeonString();
    for (UnlockedAchievement unlockedAchievement : unlockedAchievements) {
      Duration sinceUnlock = new Duration(unlockedAchievement.getDate(), now);
      string.setColor(Color.ORANGE);
      string.append(String.format("%s (%s ago)%n", unlockedAchievement.getName(), sinceUnlock));
      string.setColor(Color.YELLOW);
      string.append(String.format(" %s%n", unlockedAchievement.getInfo()));
    }
    int total = AchievementStoreFactory.getDefaultStore().getAchievements().size();
    string.setColor(Color.CYAN);
    string.append(String.format("Progress: %d/%d", tracker.getUnlockedCount(), total));
    Writer.write(string);
  }

  /**
   * Writes a listing of valid UnlockedAchievement orderings to the screen.
   */
  private static void writeValidOrderings() {
    Writer.write("Valid orderings:");
    for (String comparatorName : UnlockedAchievementComparators.getComparatorMap().keySet()) {
      Writer.write(" " + comparatorName);
    }
  }

}
