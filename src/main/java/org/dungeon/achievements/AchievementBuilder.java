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

import org.dungeon.game.Id;
import org.dungeon.util.CounterMap;

import java.util.ArrayList;
import java.util.Collection;

class AchievementBuilder {

  private final Collection<BattleStatisticsRequirement> requirements = new ArrayList<BattleStatisticsRequirement>();
  private String id;
  private String name;
  private String info;
  private String text;
  private CounterMap<Id> killsByLocationId;
  private CounterMap<Id> visitedLocations;
  private CounterMap<Id> maximumNumberOfVisits;

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

  public void addBattleStatisticsRequirement(BattleStatisticsRequirement requirement) {
    requirements.add(requirement);
  }

  public void setKillsByLocationId(CounterMap<Id> killsByLocationId) {
    if (killsByLocationId.isNotEmpty()) {
      this.killsByLocationId = killsByLocationId;
    }
  }

  public void setVisitedLocations(CounterMap<Id> visitedLocations) {
    if (visitedLocations.isNotEmpty()) {
      this.visitedLocations = visitedLocations;
    }
  }

  public void setMaximumNumberOfVisits(CounterMap<Id> maximumNumberOfVisits) {
    if (maximumNumberOfVisits.isNotEmpty()) {
      this.maximumNumberOfVisits = maximumNumberOfVisits;
    }
  }

  public Achievement createAchievement() {
    return new Achievement(id, name, info, text, requirements, killsByLocationId, visitedLocations,
        maximumNumberOfVisits);
  }

}