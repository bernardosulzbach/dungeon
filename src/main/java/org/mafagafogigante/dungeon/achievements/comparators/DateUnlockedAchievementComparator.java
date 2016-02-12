package org.mafagafogigante.dungeon.achievements.comparators;

import org.mafagafogigante.dungeon.achievements.UnlockedAchievement;

import java.io.Serializable;
import java.util.Comparator;

class DateUnlockedAchievementComparator implements Comparator<UnlockedAchievement>, Serializable {

  @Override
  public int compare(UnlockedAchievement left, UnlockedAchievement right) {
    return left.getDate().compareTo(right.getDate());
  }

}
