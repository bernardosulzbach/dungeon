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

package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.stats.TypeOfCauseOfDeath;
import org.mafagafogigante.dungeon.util.CounterMap;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that stores all loaded Achievements.
 */
public class AchievementStore {

  private static final List<Achievement> achievements = new ArrayList<Achievement>();
  private static boolean initialized = false;

  private AchievementStore() {
    throw new AssertionError();
  }

  /**
   * Initializes this class.
   */
  public static void initialize() {
    if (initialized) {
      throw new IllegalStateException("AchievementStore.initialize() has already been called.");
    }
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject("achievements.json");
    for (JsonValue achievementValue : jsonObject.get("achievements").asArray()) {
      JsonObject achievementObject = achievementValue.asObject();
      AchievementBuilder builder = new AchievementBuilder();
      builder.setId(achievementObject.get("id").asString());
      builder.setName(achievementObject.get("name").asString());
      builder.setInfo(achievementObject.get("info").asString());
      builder.setText(achievementObject.get("text").asString());
      JsonValue battleRequirements = achievementObject.get("battleRequirements");
      if (battleRequirements != null) {
        for (JsonValue requirementValue : battleRequirements.asArray()) {
          JsonObject requirementObject = requirementValue.asObject();
          JsonObject queryObject = requirementObject.get("query").asObject();
          BattleStatisticsQuery query = new BattleStatisticsQuery();
          JsonValue idValue = queryObject.get("id");
          if (idValue != null) {
            query.setId(new Id(idValue.asString()));
          }
          JsonValue typeValue = queryObject.get("type");
          if (typeValue != null) {
            query.setType(typeValue.asString());
          }
          JsonValue causeOfDeathValue = queryObject.get("causeOfDeath");
          if (causeOfDeathValue != null) {
            JsonObject causeOfDeathObject = causeOfDeathValue.asObject();
            TypeOfCauseOfDeath type = TypeOfCauseOfDeath.valueOf(causeOfDeathObject.get("type").asString());
            Id id = new Id(causeOfDeathObject.get("id").asString());
            query.setCauseOfDeath(new CauseOfDeath(type, id));
          }
          JsonValue partOfDayValue = queryObject.get("partOfDay");
          if (partOfDayValue != null) {
            query.setPartOfDay(PartOfDay.valueOf(partOfDayValue.asString()));
          }
          int count = requirementObject.get("count").asInt();
          BattleStatisticsRequirement requirement = new BattleStatisticsRequirement(query, count);
          builder.addBattleStatisticsRequirement(requirement);
        }
      }
      JsonValue explorationRequirements = achievementObject.get("explorationRequirements");
      if (explorationRequirements != null) {
        JsonValue killsByLocationId = explorationRequirements.asObject().get("killsByLocationID");
        if (killsByLocationId != null) {
          builder.setKillsByLocationId(idCounterMapFromJsonObject(killsByLocationId.asObject()));
        }
        JsonValue maximumNumberOfVisits = explorationRequirements.asObject().get("maximumNumberOfVisits");
        if (maximumNumberOfVisits != null) {
          builder.setMaximumNumberOfVisits(idCounterMapFromJsonObject(maximumNumberOfVisits.asObject()));
        }
        JsonValue visitedLocations = explorationRequirements.asObject().get("visitedLocations");
        if (visitedLocations != null) {
          builder.setVisitedLocations(idCounterMapFromJsonObject(visitedLocations.asObject()));
        }
      }
      Achievement achievement = builder.createAchievement();
      achievements.add(achievement);
    }
    initialized = true;
    DungeonLogger.info("Loaded " + achievements.size() + " achievements.");
  }

  private static CounterMap<Id> idCounterMapFromJsonObject(JsonObject jsonObject) {
    CounterMap<Id> counterMap = new CounterMap<Id>();
    for (Member member : jsonObject) {
      counterMap.incrementCounter(new Id(member.getName()), member.getValue().asInt());
    }
    return counterMap;
  }

  /**
   * Returns an unmodifiable view of all achievements.
   */
  public static List<Achievement> getAchievements() {
    return Collections.unmodifiableList(achievements);
  }

}
