package org.mafagafogigante.dungeon.achievements.comparators;

import org.mafagafogigante.dungeon.achievements.UnlockedAchievement;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class UnlockedAchievementComparators {

  private static final Map<String, Comparator<UnlockedAchievement>> comparatorMap;
  private static final Comparator<UnlockedAchievement> defaultComparator;

  static {
    comparatorMap = new HashMap<>();

    Comparator<UnlockedAchievement> nameComparator = new NameUnlockedAchievementComparator();
    comparatorMap.put("name", nameComparator);
    defaultComparator = nameComparator;

    Comparator<UnlockedAchievement> dateComparator = new DateUnlockedAchievementComparator();
    comparatorMap.put("date", dateComparator);
  }

  public static Comparator<UnlockedAchievement> getDefaultComparator() {
    return defaultComparator;
  }

  public static Map<String, Comparator<UnlockedAchievement>> getComparatorMap() {
    return comparatorMap;
  }

}
