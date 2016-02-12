package org.mafagafogigante.dungeon.achievements.comparators;

import org.mafagafogigante.dungeon.achievements.UnlockedAchievement;

import java.io.Serializable;
import java.util.Comparator;

class NameUnlockedAchievementComparator implements Comparator<UnlockedAchievement>, Serializable {

  @Override
  public int compare(UnlockedAchievement a, UnlockedAchievement b) {
    return a.getName().compareTo(b.getName());
  }

}
