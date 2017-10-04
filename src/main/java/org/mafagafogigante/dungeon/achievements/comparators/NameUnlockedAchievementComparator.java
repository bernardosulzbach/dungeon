package org.mafagafogigante.dungeon.achievements.comparators;

import org.mafagafogigante.dungeon.achievements.UnlockedAchievement;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.Comparator;

class NameUnlockedAchievementComparator implements Comparator<UnlockedAchievement>, Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  @Override
  public int compare(UnlockedAchievement a, UnlockedAchievement b) {
    return a.getName().compareTo(b.getName());
  }

}
