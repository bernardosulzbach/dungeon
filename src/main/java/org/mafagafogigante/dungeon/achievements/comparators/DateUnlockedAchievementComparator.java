package org.mafagafogigante.dungeon.achievements.comparators;

import org.mafagafogigante.dungeon.achievements.UnlockedAchievement;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.Comparator;

class DateUnlockedAchievementComparator implements Comparator<UnlockedAchievement>, Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  @Override
  public int compare(UnlockedAchievement left, UnlockedAchievement right) {
    return left.getDate().compareTo(right.getDate());
  }

}
