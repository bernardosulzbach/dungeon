package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.stats.Statistics;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AchievementTracker that tracks the unlocked achievements.
 */
public class AchievementTracker implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Statistics statistics;
  private final Map<Id, UnlockedAchievement> unlockedAchievements = new HashMap<>();

  public AchievementTracker(@NotNull Statistics statistics) {
    this.statistics = statistics;
  }

  /**
   * Writes an achievement unlocked message with some information about the unlocked achievement.
   */
  private static void writeAchievementUnlock(Achievement achievement, DungeonString builder) {
    String format = "You unlocked the achievement %s because you %s.";
    builder.append(String.format(format, achievement.getName(), achievement.getText()));
  }

  /**
   * Returns how many unlocked achievements there are in this AchievementTracker.
   */
  int getUnlockedCount() {
    return unlockedAchievements.size();
  }

  /**
   * Unlock a specific Achievement.
   *
   * <p>If there already is an UnlockedAchievement with the same ID, a warning will be logged.
   *
   * @param achievement the Achievement to be unlocked.
   */
  private void unlock(Achievement achievement, Date date, DungeonString builder) {
    if (hasNotBeenUnlocked(achievement)) {
      unlockedAchievements.put(achievement.getId(), new UnlockedAchievement(achievement, date));
      writeAchievementUnlock(achievement, builder);
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
    return unlockedAchievements.get(achievement.getId());
  }

  /**
   * Returns a List with all the UnlockedAchievements in this AchievementTracker sorted using the provided Comparator.
   *
   * @param comparator a Comparator of UnlockedAchievements, not null
   * @return a sorted List with all the UnlockedAchievements in this AchievementTracker
   */
  List<UnlockedAchievement> getUnlockedAchievements(@NotNull Comparator<UnlockedAchievement> comparator) {
    List<UnlockedAchievement> list = new ArrayList<>(unlockedAchievements.values());
    Collections.sort(list, comparator);
    return list;
  }

  /**
   * Return true if a given Achievement has not been unlocked yet.
   */
  public boolean hasNotBeenUnlocked(@NotNull Achievement achievement) {
    return getUnlockedAchievement(achievement) == null;
  }

  /**
   * Updates this AchievementTracker by iterating over the achievements and unlocking the ones that are fulfilled but
   * not yet added to the unlocked list of this tracker.
   *
   * <p>Before writing the first achievement unlock message, if there is one, a new line is written.
   */
  public void update(AchievementStore achievementStore, Date date) {
    DungeonString dungeonString = new DungeonString();
    boolean wroteNewLine = false; // If we are going to write anything at all, we must start with a blank line.
    for (Achievement achievement : achievementStore.getAchievements()) {
      if (hasNotBeenUnlocked(achievement) && achievement.isFulfilled(statistics)) {
        if (!wroteNewLine) {
          dungeonString.append("\n");
          wroteNewLine = true;
        }
        unlock(achievement, date, dungeonString);
        dungeonString.append("\n");
      }
    }
    if (dungeonString.getLength() != 0) {
      Writer.write(dungeonString);
    }
  }

}
