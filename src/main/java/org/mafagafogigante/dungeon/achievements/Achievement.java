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

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.util.CounterMap;

import java.util.Collection;

/**
 * Achievement class.
 */
public class Achievement {

  private final Id id;
  private final String name;
  private final String info;
  private final String text;

  private final BattleComponent battle;
  private final ExplorationComponent exploration;

  /**
   * Constructs an Achievement with the specified ID, name and info.
   *
   * @param info the String displayed when the "Achievements" command is used
   * @param text the String used to explain why the character unlocked the achievement
   */
  public Achievement(String id, String name, String info, String text,
      Collection<BattleStatisticsRequirement> battleRequirements, CounterMap<Id> killsByLocationId,
      CounterMap<Id> visitedLocations, CounterMap<Id> maximumNumberOfVisits) {
    this.id = new Id(id);
    this.name = name;
    this.info = info;
    this.text = text;
    battle = new BattleComponent(battleRequirements);
    exploration = new ExplorationComponent(killsByLocationId, visitedLocations, maximumNumberOfVisits);
  }

  public Id getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getInfo() {
    return info;
  }

  public String getText() {
    return text;
  }

  /**
   * Evaluates if the statistics fulfill this Achievement's conditions.
   *
   * @return true if the Achievement is fulfilled, false otherwise.
   */
  boolean isFulfilled() {
    return battle.isFulfilled() && exploration.isFulfilled();
  }

  @Override
  public String toString() {
    return String.format("Achievement{id=%s, name='%s', info='%s', text='%s'}", id, name, info, text);
  }

}
