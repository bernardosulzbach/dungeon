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

import java.util.ArrayList;
import java.util.Collection;

public class AchievementBuilder {

  private final Collection<BattleStatisticsRequirement> requirements = new ArrayList<BattleStatisticsRequirement>();
  private String id;
  private String name;
  private String info;
  private String text;
  private CounterMap<ID> killsByLocationID;
  private CounterMap<ID> visitedLocations;
  private CounterMap<ID> maximumNumberOfVisits;

  public void setID(String id) {
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
    return new Achievement(id, name, info, text, requirements, killsByLocationID, visitedLocations,
        maximumNumberOfVisits);
  }

}