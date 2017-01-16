package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AchievementsJsonFileTest extends ResourcesTypeTest {

  private static final JsonObject achievementsJsonFile = getJsonObjectByJsonFile(JsonFileEnum.ACHIEVEMENTS);
  private static final String ACHIEVEMENTS_FIELD = "achievements";
  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";
  private static final String INFO_FIELD = "info";
  private static final String TEXT_FIELD = "text";
  private static final String BATTLE_REQUIREMENTS_FIELD = "battleRequirements";
  private static final String EXPLORATION_REQUIREMENTS_FIELD = "explorationRequirements";
  private static final String COUNT_FIELD = "count";
  private static final String QUERY_FIELD = "query";
  private static final String TYPE_FIELD = "type";
  private static final String PART_OF_DAY_FIELD = "partOfDay";
  private static final String CAUSE_OF_DEATH_FIELD = "causeOfDeath";
  private static final String KILLS_BY_LOCATION_ID_FIELD = "killsByLocationID";
  private static final String MAXIMUM_NUMBER_OF_VISITS_FIELD = "maximumNumberOfVisits";
  private static final String VISITED_LOCATIONS_FIELD = "visitedLocations";
  private static final String FOREST_FIELD = "FOREST";
  private static final String STONE_BRIDGE_FIELD = "STONE_BRIDGE";
  private static final String TIMBER_BRIDGE_FIELD = "TIMBER_BRIDGE";
  private static final String GRAVEYARD_FIELD = "GRAVEYARD";
  private static final String DESERT_FIELD = "DESERT";

  @Test
  public void testIsFileHasValidStructure() {
    JsonRule caseOfDeathRuleObject = getCauseOfDeathRuleObject();
    JsonRule queryRuleObject = getQueryRuleObject(caseOfDeathRuleObject);
    JsonRule battleRequirementsRuleObject = getBattleRequirementsRuleObject(queryRuleObject);
    JsonRule visitedLocationsRuleObject = getVisitedLocationsRuleObject();
    JsonRule maximumNumberOfVisitsRuleObject = getMaximumNumberOfVisitsRuleObject();
    JsonRule killsByLocationIdRuleObject = getKillsByLocationIdRuleObject();
    JsonRule explorationRequirementsRuleObject =
        getExplorationRequirementsRuleObject(visitedLocationsRuleObject, maximumNumberOfVisitsRuleObject,
            killsByLocationIdRuleObject);
    JsonRule achievementRuleObject =
        makeAchievementsRuleObject(explorationRequirementsRuleObject, battleRequirementsRuleObject);
    JsonRule achievementsFileRuleObject = getAchievementsFileRuleObject(achievementRuleObject);
    achievementsFileRuleObject.validate(achievementsJsonFile);
  }

  private JsonRule getAchievementsFileRuleObject(JsonRule achievementRuleObject) {
    Map<String, JsonRule> achievementsFileRules = new HashMap<>();
    achievementsFileRules.put(ACHIEVEMENTS_FIELD, JsonRuleFactory.makeVariableArrayRule(achievementRuleObject));
    return JsonRuleFactory.makeObjectRule(achievementsFileRules);
  }

  private JsonRule makeAchievementsRuleObject(JsonRule explorationRequirementsRuleObject,
      JsonRule battleRequirementsRuleObject) {
    Map<String, JsonRule> achievementRules = new HashMap<>();
    achievementRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    achievementRules.put(NAME_FIELD, JsonRuleFactory.makeStringRule());
    achievementRules.put(INFO_FIELD, JsonRuleFactory.makeStringRule());
    achievementRules.put(TEXT_FIELD, JsonRuleFactory.makeStringRule());
    achievementRules.put(BATTLE_REQUIREMENTS_FIELD,
        JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeVariableArrayRule(battleRequirementsRuleObject)));
    achievementRules
        .put(EXPLORATION_REQUIREMENTS_FIELD, JsonRuleFactory.makeOptionalRule(explorationRequirementsRuleObject));
    return JsonRuleFactory.makeObjectRule(achievementRules);
  }

  private JsonRule getBattleRequirementsRuleObject(JsonRule queryRuleObject) {
    Map<String, JsonRule> battleRequirementsRules = new HashMap<>();
    battleRequirementsRules.put(COUNT_FIELD, JsonRuleFactory.makeIntegerRule());
    battleRequirementsRules.put(QUERY_FIELD, queryRuleObject);
    return JsonRuleFactory.makeObjectRule(battleRequirementsRules);
  }

  private JsonRule getQueryRuleObject(JsonRule caseOfDeathRuleObject) {
    Map<String, JsonRule> queryRules = new HashMap<>();
    queryRules.put(ID_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeStringRule()));
    queryRules.put(TYPE_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeStringRule()));
    queryRules.put(PART_OF_DAY_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeUppercaseStringRule()));
    queryRules.put(CAUSE_OF_DEATH_FIELD, JsonRuleFactory.makeOptionalRule(caseOfDeathRuleObject));
    return JsonRuleFactory.makeObjectRule(queryRules);
  }

  private JsonRule getCauseOfDeathRuleObject() {
    Map<String, JsonRule> causeOfDeathRules = new HashMap<>();
    causeOfDeathRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    causeOfDeathRules.put(TYPE_FIELD, JsonRuleFactory.makeUppercaseStringRule());
    return JsonRuleFactory.makeObjectRule(causeOfDeathRules);
  }

  private JsonRule getExplorationRequirementsRuleObject(JsonRule visitedLocationsRuleObject,
      JsonRule maximumNumberOfVisitsRuleObject, JsonRule killsByLocationIdRuleObject) {
    Map<String, JsonRule> explorationRequirementsRules = new HashMap<>();
    explorationRequirementsRules
        .put(KILLS_BY_LOCATION_ID_FIELD, JsonRuleFactory.makeOptionalRule(killsByLocationIdRuleObject));
    explorationRequirementsRules
        .put(MAXIMUM_NUMBER_OF_VISITS_FIELD, JsonRuleFactory.makeOptionalRule(maximumNumberOfVisitsRuleObject));
    explorationRequirementsRules
        .put(VISITED_LOCATIONS_FIELD, JsonRuleFactory.makeOptionalRule(visitedLocationsRuleObject));
    return JsonRuleFactory.makeObjectRule(explorationRequirementsRules);
  }

  private JsonRule getKillsByLocationIdRuleObject() {
    Map<String, JsonRule> killsByLocationIdRules = new HashMap<>();
    killsByLocationIdRules.put(FOREST_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule()));
    return JsonRuleFactory.makeObjectRule(killsByLocationIdRules);
  }

  private JsonRule getMaximumNumberOfVisitsRuleObject() {
    Map<String, JsonRule> maximumNumberOfVisitsRules = new HashMap<>();
    maximumNumberOfVisitsRules
        .put(STONE_BRIDGE_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule()));
    maximumNumberOfVisitsRules
        .put(TIMBER_BRIDGE_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule()));
    maximumNumberOfVisitsRules
        .put(GRAVEYARD_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule()));
    return JsonRuleFactory.makeObjectRule(maximumNumberOfVisitsRules);
  }

  private JsonRule getVisitedLocationsRuleObject() {
    Map<String, JsonRule> visitedLocationsRules = new HashMap<>();
    visitedLocationsRules.put(DESERT_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule()));
    visitedLocationsRules.put(GRAVEYARD_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule()));
    return JsonRuleFactory.makeObjectRule(visitedLocationsRules);
  }

}
