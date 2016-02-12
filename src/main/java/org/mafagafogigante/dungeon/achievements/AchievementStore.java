package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that stores Achievements.
 */
public final class AchievementStore {

  private final Set<Id> registeredIds = new HashSet<>();
  private final List<Achievement> achievements = new ArrayList<>();

  private boolean locked = false;

  AchievementStore() {
  }

  /**
   * Returns an unmodifiable view of the achievements contained in this AchievementStore.
   */
  public List<Achievement> getAchievements() {
    return Collections.unmodifiableList(achievements);
  }

  /**
   * Adds a new Achievement to this AchievementStore.
   *
   * <p>Throws an IllegalStateException if this AchievementStore is locked <p>Throws an IllegalArgumentException if this
   * AchievementStore already has an Achievement with the same Id
   */
  public void addAchievement(Achievement achievement) {
    if (locked) {
      throw new IllegalStateException("this AchievementStore is locked");
    }
    if (registeredIds.contains(achievement.getId())) {
      throw new IllegalArgumentException("there is already an Achievement with the id " + achievement.getId());
    }
    registeredIds.add(achievement.getId());
    achievements.add(achievement);
  }

  /**
   * Locks this AchievementStore, disallowing it to receive new Achievements.
   */
  public void lock() {
    locked = true;
  }

}
