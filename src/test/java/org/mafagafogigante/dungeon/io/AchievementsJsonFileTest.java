package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AchievementsJsonFileTest extends ResourcesTypeTest {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";
  private static final String INFO_FIELD = "info";
  private static final String TEXT_FIELD = "text";
  private static final String TYPE_FIELD = "type";
  private static final String COUNT_FIELD = "count";
  private static final String QUERY_FIELD = "query";
  private static final String FOREST_FIELD = "FOREST";
  private static final String DESERT_FIELD = "DESERT";
  private static final String GRAVEYARD_FIELD = "GRAVEYARD";
  private static final String PART_OF_DAY_FIELD = "partOfDay";
  private static final String ACHIEVEMENTS_FIELD = "achievements";
  private static final String STONE_BRIDGE_FIELD = "STONE_BRIDGE";
  private static final String CAUSE_OF_DEATH_FIELD = "causeOfDeath";
  private static final String TIMBER_BRIDGE_FIELD = "TIMBER_BRIDGE";
  private static final String VISITED_LOCATIONS_FIELD = "visitedLocations";
  private static final String BATTLE_REQUIREMENTS_FIELD = "battleRequirements";
  private static final String KILLS_BY_LOCATION_ID_FIELD = "killsByLocationID";
  private static final String ACHIEVEMENTS_JSON_FILE_NAME = "achievements.json";
  private static final String MAXIMUM_NUMBER_OF_VISITS_FIELD = "maximumNumberOfVisits";
  private static final String EXPLORATION_REQUIREMENTS_FIELD = "explorationRequirements";

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
    JsonObject achievementsFileJsonObject = getJsonObjectByJsonFile(ACHIEVEMENTS_JSON_FILE_NAME);
    achievementsFileRuleObject.validate(achievementsFileJsonObject);
  }

  private JsonRule getAchievementsFileRuleObject(JsonRule achievementRuleObject) {
    Map<String, JsonRule> achievementsFileRules = new HashMap<>();
    achievementsFileRules.put(ACHIEVEMENTS_FIELD, JsonRuleFactory.makeVariableArrayRule(achievementRuleObject));
    return JsonRuleFactory.makeObjectRule(achievementsFileRules);
  }

  private JsonRule makeAchievementsRuleObject(JsonRule explorationRequirementsRuleObject,
      JsonRule battleRequirementsRuleObject) {
    final JsonRule jsonStringRule = JsonRuleFactory.makeStringRule();
    Map<String, JsonRule> achievementRules = new HashMap<>();
    achievementRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    achievementRules.put(NAME_FIELD, jsonStringRule);
    achievementRules.put(INFO_FIELD, jsonStringRule);
    achievementRules.put(TEXT_FIELD, jsonStringRule);
    final JsonRule battleVariableJsonRule = JsonRuleFactory.makeVariableArrayRule(battleRequirementsRuleObject);
    achievementRules.put(BATTLE_REQUIREMENTS_FIELD, JsonRuleFactory.makeOptionalRule(battleVariableJsonRule));
    final JsonRule optionalExplorationRule = JsonRuleFactory.makeOptionalRule(explorationRequirementsRuleObject);
    achievementRules.put(EXPLORATION_REQUIREMENTS_FIELD, optionalExplorationRule);
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
    final JsonRule optionalStringRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeStringRule());
    queryRules.put(ID_FIELD, optionalStringRule);
    queryRules.put(TYPE_FIELD, optionalStringRule);
    final JsonRule uppercaseRule = JsonRuleFactory.makeUppercaseStringRule();
    final JsonRule optionalUppercaseRule = JsonRuleFactory.makeOptionalRule(uppercaseRule);
    queryRules.put(PART_OF_DAY_FIELD, optionalUppercaseRule);
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
    final JsonRule optionalKillsByLocationRule = JsonRuleFactory.makeOptionalRule(killsByLocationIdRuleObject);
    explorationRequirementsRules.put(KILLS_BY_LOCATION_ID_FIELD, optionalKillsByLocationRule);
    final JsonRule optionalMaximumNumberRule = JsonRuleFactory.makeOptionalRule(maximumNumberOfVisitsRuleObject);
    explorationRequirementsRules.put(MAXIMUM_NUMBER_OF_VISITS_FIELD, optionalMaximumNumberRule);
    final JsonRule optionalVisitedLocationsRule = JsonRuleFactory.makeOptionalRule(visitedLocationsRuleObject);
    explorationRequirementsRules.put(VISITED_LOCATIONS_FIELD, optionalVisitedLocationsRule);
    return JsonRuleFactory.makeObjectRule(explorationRequirementsRules);
  }

  private JsonRule getKillsByLocationIdRuleObject() {
    Map<String, JsonRule> killsByLocationIdRules = new HashMap<>();
    final JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule());
    killsByLocationIdRules.put(FOREST_FIELD, optionalIntegerRule);
    return JsonRuleFactory.makeObjectRule(killsByLocationIdRules);
  }

  private JsonRule getMaximumNumberOfVisitsRuleObject() {
    Map<String, JsonRule> maximumNumberOfVisitsRules = new HashMap<>();
    final JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule());
    maximumNumberOfVisitsRules.put(STONE_BRIDGE_FIELD, optionalIntegerRule);
    maximumNumberOfVisitsRules.put(TIMBER_BRIDGE_FIELD, optionalIntegerRule);
    maximumNumberOfVisitsRules.put(GRAVEYARD_FIELD, optionalIntegerRule);
    return JsonRuleFactory.makeObjectRule(maximumNumberOfVisitsRules);
  }

  private JsonRule getVisitedLocationsRuleObject() {
    Map<String, JsonRule> visitedLocationsRules = new HashMap<>();
    final JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule());
    visitedLocationsRules.put(DESERT_FIELD, optionalIntegerRule);
    visitedLocationsRules.put(GRAVEYARD_FIELD, optionalIntegerRule);
    return JsonRuleFactory.makeObjectRule(visitedLocationsRules);
  }

}
