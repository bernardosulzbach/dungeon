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
  private static final String MAXIMUM_NUMBER_OF_VISITS_FIELD = "maximumNumberOfVisits";
  private static final String EXPLORATION_REQUIREMENTS_FIELD = "explorationRequirements";

  @Test
  public void testIsFileHasValidStructure() {
    JsonRule caseOfDeathRule = getCauseOfDeathRule();
    JsonRule queryRule = getQueryRule(caseOfDeathRule);
    JsonRule battleRequirementsRule = getBattleRequirementsRule(queryRule);
    JsonRule visitedLocationsRule = getVisitedLocationsRule();
    JsonRule maximumNumberOfVisitsRule = getMaximumNumberOfVisitsRule();
    JsonRule killsByLocationIdRule = getKillsByLocationIdRule();
    Map<String, JsonRule> explorationRequirementsRules = new HashMap<>();
    JsonRule optionalKillsByLocationRule = JsonRuleFactory.makeOptionalRule(killsByLocationIdRule);
    explorationRequirementsRules.put(KILLS_BY_LOCATION_ID_FIELD, optionalKillsByLocationRule);
    JsonRule optionalMaximumNumberRule = JsonRuleFactory.makeOptionalRule(maximumNumberOfVisitsRule);
    explorationRequirementsRules.put(MAXIMUM_NUMBER_OF_VISITS_FIELD, optionalMaximumNumberRule);
    JsonRule optionalVisitedLocationsRule = JsonRuleFactory.makeOptionalRule(visitedLocationsRule);
    explorationRequirementsRules.put(VISITED_LOCATIONS_FIELD, optionalVisitedLocationsRule);
    JsonRule explorationRequirementsRule = JsonRuleFactory.makeObjectRule(explorationRequirementsRules);
    JsonRule achievementRule = makeAchievementsRule(explorationRequirementsRule, battleRequirementsRule);
    JsonRule achievementsFileRule = getAchievementsFileRule(achievementRule);
    String filename = ResourceNameResolver.resolveName(DungeonResource.ACHIEVEMENTS);
    JsonObject achievementsFileJson = getJsonObjectByJsonFilename(filename);
    achievementsFileRule.validate(achievementsFileJson);
  }

  private JsonRule getAchievementsFileRule(JsonRule achievementRule) {
    Map<String, JsonRule> achievementsFileRules = new HashMap<>();
    achievementsFileRules.put(ACHIEVEMENTS_FIELD, JsonRuleFactory.makeVariableArrayRule(achievementRule));
    return JsonRuleFactory.makeObjectRule(achievementsFileRules);
  }

  private JsonRule makeAchievementsRule(JsonRule explorationRequirementsRule, JsonRule battleRequirementsRule) {
    JsonRule jsonStringRule = JsonRuleFactory.makeStringRule();
    Map<String, JsonRule> achievementRules = new HashMap<>();
    achievementRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    achievementRules.put(NAME_FIELD, jsonStringRule);
    achievementRules.put(INFO_FIELD, jsonStringRule);
    achievementRules.put(TEXT_FIELD, jsonStringRule);
    JsonRule battleVariableJsonRule = JsonRuleFactory.makeVariableArrayRule(battleRequirementsRule);
    achievementRules.put(BATTLE_REQUIREMENTS_FIELD, JsonRuleFactory.makeOptionalRule(battleVariableJsonRule));
    JsonRule optionalExplorationRule = JsonRuleFactory.makeOptionalRule(explorationRequirementsRule);
    achievementRules.put(EXPLORATION_REQUIREMENTS_FIELD, optionalExplorationRule);
    return JsonRuleFactory.makeObjectRule(achievementRules);
  }

  private JsonRule getBattleRequirementsRule(JsonRule queryRule) {
    Map<String, JsonRule> battleRequirementsRules = new HashMap<>();
    battleRequirementsRules.put(COUNT_FIELD, JsonRuleFactory.makeIntegerRule());
    battleRequirementsRules.put(QUERY_FIELD, queryRule);
    return JsonRuleFactory.makeObjectRule(battleRequirementsRules);
  }

  private JsonRule getQueryRule(JsonRule caseOfDeathRule) {
    Map<String, JsonRule> queryRules = new HashMap<>();
    JsonRule optionalStringRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeStringRule());
    queryRules.put(ID_FIELD, optionalStringRule);
    queryRules.put(TYPE_FIELD, optionalStringRule);
    JsonRule uppercaseRule = JsonRuleFactory.makeUppercaseStringRule();
    JsonRule optionalUppercaseRule = JsonRuleFactory.makeOptionalRule(uppercaseRule);
    queryRules.put(PART_OF_DAY_FIELD, optionalUppercaseRule);
    queryRules.put(CAUSE_OF_DEATH_FIELD, JsonRuleFactory.makeOptionalRule(caseOfDeathRule));
    return JsonRuleFactory.makeObjectRule(queryRules);
  }

  private JsonRule getCauseOfDeathRule() {
    Map<String, JsonRule> causeOfDeathRules = new HashMap<>();
    causeOfDeathRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    causeOfDeathRules.put(TYPE_FIELD, JsonRuleFactory.makeUppercaseStringRule());
    return JsonRuleFactory.makeObjectRule(causeOfDeathRules);
  }

  private JsonRule getKillsByLocationIdRule() {
    Map<String, JsonRule> killsByLocationIdRules = new HashMap<>();
    JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule());
    killsByLocationIdRules.put(FOREST_FIELD, optionalIntegerRule);
    return JsonRuleFactory.makeObjectRule(killsByLocationIdRules);
  }

  private JsonRule getMaximumNumberOfVisitsRule() {
    Map<String, JsonRule> maximumNumberOfVisitsRules = new HashMap<>();
    JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule());
    maximumNumberOfVisitsRules.put(STONE_BRIDGE_FIELD, optionalIntegerRule);
    maximumNumberOfVisitsRules.put(TIMBER_BRIDGE_FIELD, optionalIntegerRule);
    maximumNumberOfVisitsRules.put(GRAVEYARD_FIELD, optionalIntegerRule);
    return JsonRuleFactory.makeObjectRule(maximumNumberOfVisitsRules);
  }

  private JsonRule getVisitedLocationsRule() {
    Map<String, JsonRule> visitedLocationsRules = new HashMap<>();
    JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeIntegerRule());
    visitedLocationsRules.put(DESERT_FIELD, optionalIntegerRule);
    visitedLocationsRules.put(GRAVEYARD_FIELD, optionalIntegerRule);
    return JsonRuleFactory.makeObjectRule(visitedLocationsRules);
  }

}
