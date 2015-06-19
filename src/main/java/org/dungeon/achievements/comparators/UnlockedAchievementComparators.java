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

package org.dungeon.achievements.comparators;

import org.dungeon.achievements.UnlockedAchievement;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class UnlockedAchievementComparators {

  private static final Map<String, Comparator<UnlockedAchievement>> comparatorMap;
  private static final Comparator<UnlockedAchievement> defaultComparator;

  static {
    comparatorMap = new HashMap<String, Comparator<UnlockedAchievement>>();

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
