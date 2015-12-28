/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.date.Date;

import java.io.Serializable;

/**
 * UnlockedAchievement class that records the unlocking of an achievement.
 */
public final class UnlockedAchievement implements Serializable {

  private final String name;
  private final String info;
  private final Date date;

  /**
   * Constructs a new UnlockedAchievement.
   *
   * @param achievement the Achievement that was unlocked
   * @param date the date when the achievement was unlocked
   */
  public UnlockedAchievement(Achievement achievement, Date date) {
    this.name = achievement.getName();
    this.info = achievement.getInfo();
    this.date = date;
  }

  public String getName() {
    return name;
  }

  public String getInfo() {
    return info;
  }

  public Date getDate() {
    return date;
  }

  @Override
  public String toString() {
    return "Unlocked " + name + " in " + date + ".";
  }

}
