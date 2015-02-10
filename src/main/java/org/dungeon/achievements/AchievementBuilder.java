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

package org.dungeon.achievements;

import org.dungeon.game.ID;
import org.dungeon.util.CounterMap;

public class AchievementBuilder {
  private String id;
  private String name;
  private String info;
  private String text;
  private int minimumBattleCount;
  private int longestBattleLength;
  private CounterMap<ID> killsByCreatureID;
  private CounterMap<String> killsByCreatureType;
  private CounterMap<ID> killsByWeapon;
  private CounterMap<ID> killsByLocationID;
  private CounterMap<ID> distinctLocationsVisitCount;
  private CounterMap<ID> sameLocationVisitCounter;

  public AchievementBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public AchievementBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public AchievementBuilder setInfo(String info) {
    this.info = info;
    return this;
  }

  public AchievementBuilder setText(String text) {
    this.text = text;
    return this;
  }

  public AchievementBuilder setMinimumBattleCount(int minimumBattleCount) {
    this.minimumBattleCount = minimumBattleCount;
    return this;
  }

  public AchievementBuilder setLongestBattleLength(int longestBattleLength) {
    this.longestBattleLength = longestBattleLength;
    return this;
  }

  public AchievementBuilder setKillsByCreatureID(CounterMap<ID> killsByCreatureID) {
    if (killsByCreatureID.isNotEmpty()) {
      this.killsByCreatureID = killsByCreatureID;
    }
    return this;
  }

  public AchievementBuilder setKillsByCreatureType(CounterMap<String> killsByCreatureType) {
    if (killsByCreatureType.isNotEmpty()) {
      this.killsByCreatureType = killsByCreatureType;
    }
    return this;
  }

  public AchievementBuilder setKillsByWeapon(CounterMap<ID> killsByWeapon) {
    if (killsByWeapon.isNotEmpty()) {
      this.killsByWeapon = killsByWeapon;
    }
    return this;
  }

  public AchievementBuilder setKillsByLocationID(CounterMap<ID> killsByLocationID) {
    if (killsByLocationID.isNotEmpty()) {
      this.killsByLocationID = killsByLocationID;
    }
    return this;
  }

  public AchievementBuilder setDistinctLocationsVisitCount(CounterMap<ID> distinctLocationsVisitCount) {
    if (distinctLocationsVisitCount.isNotEmpty()) {
      this.distinctLocationsVisitCount = distinctLocationsVisitCount;
    }
    return this;
  }

  public AchievementBuilder setSameLocationVisitCounter(CounterMap<ID> sameLocationVisitCounter) {
    if (sameLocationVisitCounter.isNotEmpty()) {
      this.sameLocationVisitCounter = sameLocationVisitCounter;
    }
    return this;
  }

  public Achievement createAchievement() {
    return new Achievement(id, name, info, text, minimumBattleCount, longestBattleLength, killsByCreatureID,
        killsByCreatureType, killsByWeapon, killsByLocationID, distinctLocationsVisitCount, sameLocationVisitCounter);
  }

}