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
import org.dungeon.stats.CauseOfDeath;
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
  private CounterMap<CauseOfDeath> killsByCauseOfDeath;
  private CounterMap<ID> killsByLocationID;
  private CounterMap<ID> visitedLocations;
  private CounterMap<ID> maximumNumberOfVisits;

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setMinimumBattleCount(int minimumBattleCount) {
    this.minimumBattleCount = minimumBattleCount;
  }

  public void setLongestBattleLength(int longestBattleLength) {
    this.longestBattleLength = longestBattleLength;
  }

  public void setKillsByCreatureID(CounterMap<ID> killsByCreatureID) {
    if (killsByCreatureID.isNotEmpty()) {
      this.killsByCreatureID = killsByCreatureID;
    }
  }

  public void setKillsByCreatureType(CounterMap<String> killsByCreatureType) {
    if (killsByCreatureType.isNotEmpty()) {
      this.killsByCreatureType = killsByCreatureType;
    }
  }

  public void setKillsByCauseOfDeath(CounterMap<CauseOfDeath> killsByCauseOfDeath) {
    if (killsByCauseOfDeath.isNotEmpty()) {
      this.killsByCauseOfDeath = killsByCauseOfDeath;
    }
  }

  public void setKillsByLocationID(CounterMap<ID> killsByLocationID) {
    if (killsByLocationID.isNotEmpty()) {
      this.killsByLocationID = killsByLocationID;
    }
  }

  public void setVisitedLocations(CounterMap<ID> visitedLocations) {
    if (visitedLocations.isNotEmpty()) {
      this.visitedLocations = visitedLocations;
    }
  }

  public void setMaximumNumberOfVisits(CounterMap<ID> maximumNumberOfVisits) {
    if (maximumNumberOfVisits.isNotEmpty()) {
      this.maximumNumberOfVisits = maximumNumberOfVisits;
    }
  }

  public Achievement createAchievement() {
    return new Achievement(id, name, info, text, minimumBattleCount, longestBattleLength, killsByCreatureID,
        killsByCreatureType, killsByCauseOfDeath, killsByLocationID, visitedLocations, maximumNumberOfVisits);
  }

}