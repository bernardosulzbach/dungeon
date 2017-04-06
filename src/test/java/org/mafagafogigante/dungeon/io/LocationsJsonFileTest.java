package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LocationsJsonFileTest extends ResourcesTypeTest {

  private static final String ID_FIELD = "id";
  private static final String TYPE_FIELD = "type";
  private static final String NAME_FIELD = "name";
  private static final String INFO_FIELD = "info";
  private static final String COLOR_FIELD = "color";
  private static final String ITEMS_FIELD = "items";
  private static final String DELAY_FIELD = "delay";
  private static final String SYMBOL_FIELD = "symbol";
  private static final String MINIMUM_FIELD = "minimum";
  private static final String MAXIMUM_FIELD = "maximum";
  private static final String SPAWNERS_FIELD = "spawners";
  private static final String SINGULAR_FIELD = "singular";
  private static final String BLOB_SIZE_FIELD = "blobSize";
  private static final String LOCATIONS_FIELD = "locations";
  private static final String POPULATION_FIELD = "population";
  private static final String PROBABILITY_FIELD = "probability";
  private static final String BLOCKED_ENTRANCES_FIELD = "blockedEntrances";
  private static final String LIGHT_PERMITTIVITY_FIELD = "lightPermittivity";
  private static final int COLOR_MIN = 0;
  private static final int COLOR_MAX = 255;
  private static final int COLOR_ARRAY_SIZE = 3;
  private static final int BLOB_SIZE_MIN = 0;
  private static final int BLOB_SIZE_MAX = 100;
  private static final int SYMBOL_STRING_LENGTH = 1;
  private static final double PROBABILITY_MIN = 0.0;
  private static final double PROBABILITY_MAX = 1.0;
  private static final double PERMITTIVITY_MIN = 0.0;
  private static final double PERMITTIVITY_MAX = 1.0;
  private static final int BLOCKED_ENTRANCE_STRING_LENGTH = 1;

  @Test
  public void testIsFileHasValidStructure() {
    final JsonRule itemsRule = getItemsRule();
    final JsonRule populationRule = getPopulationRule();
    final JsonRule spawnersRule = getSpawnersRule(populationRule);
    final JsonRule blockedEntrancesRule = getBlockedEntrancesRule();
    final JsonRule colorRule = getColorRuleGroup();
    final JsonRule nameRule = getNameRule();
    Map<String, JsonRule> locationsRules = new HashMap<>();
    final JsonRule idRule = JsonRuleFactory.makeIdRule();
    final JsonRule blobBoundRule = JsonRuleFactory.makeBoundIntegerRule(BLOB_SIZE_MIN, BLOB_SIZE_MAX);
    final JsonRule lightBoundRule = JsonRuleFactory.makeBoundDoubleRule(PERMITTIVITY_MIN, PERMITTIVITY_MAX);
    locationsRules.put(ID_FIELD, idRule);
    locationsRules.put(TYPE_FIELD, idRule);
    locationsRules.put(NAME_FIELD, nameRule);
    locationsRules.put(COLOR_FIELD, colorRule);
    locationsRules.put(SYMBOL_FIELD, JsonRuleFactory.makeStringLengthRule(SYMBOL_STRING_LENGTH));
    locationsRules.put(INFO_FIELD, JsonRuleFactory.makeStringRule());
    locationsRules.put(BLOB_SIZE_FIELD, blobBoundRule);
    locationsRules.put(LIGHT_PERMITTIVITY_FIELD, lightBoundRule);
    locationsRules.put(BLOCKED_ENTRANCES_FIELD, blockedEntrancesRule);
    locationsRules.put(SPAWNERS_FIELD, spawnersRule);
    locationsRules.put(ITEMS_FIELD, itemsRule);
    final JsonRule locationRule = JsonRuleFactory.makeObjectRule(locationsRules);
    final JsonRule locationsFileJsonRule = getLocationsFileRule(locationRule);
    JsonObject locationsFileJsonObject = getJsonObjectByJsonFile(JsonFileName.LOCATIONS);
    locationsFileJsonRule.validate(locationsFileJsonObject);
  }

  private JsonRule getNameRule() {
    Map<String, JsonRule> nameRules = new HashMap<>();
    nameRules.put(SINGULAR_FIELD, JsonRuleFactory.makeStringRule());
    return JsonRuleFactory.makeObjectRule(nameRules);
  }

  private JsonRule getColorRuleGroup() {
    final JsonRule colorIntegerInboundRule = JsonRuleFactory.makeBoundIntegerRule(COLOR_MIN, COLOR_MAX);
    final JsonRule colorElementRule = JsonRuleFactory.makeVariableArrayRule(colorIntegerInboundRule);
    return JsonRuleFactory.makeGroupRule(JsonRuleFactory.makeArraySizeRule(COLOR_ARRAY_SIZE), colorElementRule);
  }

  private JsonRule getBlockedEntrancesRule() {
    final JsonRule blockedEntranceLengthRule = JsonRuleFactory.makeStringLengthRule(BLOCKED_ENTRANCE_STRING_LENGTH);
    final JsonRule uppercaseRule = JsonRuleFactory.makeUppercaseStringRule();
    final JsonRule blockedEntranceGroupRule = JsonRuleFactory.makeGroupRule(blockedEntranceLengthRule, uppercaseRule);
    return JsonRuleFactory.makeVariableArrayRule(blockedEntranceGroupRule);
  }

  private JsonRule getSpawnersRule(JsonRule populationRule) {
    Map<String, JsonRule> spawnersRules = new HashMap<>();
    spawnersRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    spawnersRules.put(DELAY_FIELD, JsonRuleFactory.makeIntegerRule());
    spawnersRules.put(POPULATION_FIELD, populationRule);
    JsonRule spawnerElementsRule = JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeObjectRule(spawnersRules));
    return JsonRuleFactory.makeOptionalRule(spawnerElementsRule);
  }

  private JsonRule getPopulationRule() {
    Map<String, JsonRule> populationRules = new HashMap<>();
    final JsonRule integerRule = JsonRuleFactory.makeIntegerRule();
    populationRules.put(MINIMUM_FIELD, integerRule);
    populationRules.put(MAXIMUM_FIELD, integerRule);
    return JsonRuleFactory.makeObjectRule(populationRules);
  }

  private JsonRule getItemsRule() {
    Map<String, JsonRule> itemsRules = new HashMap<>();
    final JsonRule probabilityBoundRule = JsonRuleFactory.makeBoundDoubleRule(PROBABILITY_MIN, PROBABILITY_MAX);
    itemsRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    itemsRules.put(PROBABILITY_FIELD, probabilityBoundRule);
    final JsonRule itemsObjectRule = JsonRuleFactory.makeObjectRule(itemsRules);
    final JsonRule itemElementsRule = JsonRuleFactory.makeVariableArrayRule(itemsObjectRule);
    return JsonRuleFactory.makeOptionalRule(itemElementsRule);
  }

  private JsonRule getLocationsFileRule(JsonRule locationsRule) {
    Map<String, JsonRule> locationsFileRules = new HashMap<>();
    locationsFileRules.put(LOCATIONS_FIELD, JsonRuleFactory.makeVariableArrayRule(locationsRule));
    return JsonRuleFactory.makeObjectRule(locationsFileRules);
  }

}
