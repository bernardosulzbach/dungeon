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
  private static final String LOCATIONS_JSON_FILE_NAME = "locations.json";
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
    final JsonRule itemsRuleObject = getItemsRuleObject();
    final JsonRule populationRuleObject = getPopulationRuleObject();
    final JsonRule spawnersRuleObject = getSpawnersRuleObject(populationRuleObject);
    final JsonRule blockedEntrancesRuleObject = getBlockedEntrancesRuleObject();
    final JsonRule colorRule = getColorRuleGroup();
    final JsonRule nameRuleObject = getNameRuleObject();
    final JsonRule locationRuleObject =
        getLocationsRuleObject(itemsRuleObject, spawnersRuleObject, blockedEntrancesRuleObject, colorRule,
            nameRuleObject);
    final JsonRule locationsFileJsonRule = getLocationsFileRuleObject(locationRuleObject);
    JsonObject locationsFileJsonObject = getJsonObjectByJsonFile(LOCATIONS_JSON_FILE_NAME);
    locationsFileJsonRule.validate(locationsFileJsonObject);
  }

  private JsonRule getLocationsRuleObject(JsonRule itemsRuleObject, JsonRule spawnersRuleObject,
      JsonRule blockedEntrancesRuleObject, JsonRule colorRule, JsonRule nameRuleObject) {
    Map<String, JsonRule> locationsRules = new HashMap<>();
    final JsonRule idRule = JsonRuleFactory.makeIdRule();
    final JsonRule blobBoundRule = JsonRuleFactory.makeBoundIntegerRule(BLOB_SIZE_MIN, BLOB_SIZE_MAX);
    final JsonRule lightBoundRule = JsonRuleFactory.makeBoundDoubleRule(PERMITTIVITY_MIN, PERMITTIVITY_MAX);
    locationsRules.put(ID_FIELD, idRule);
    locationsRules.put(TYPE_FIELD, idRule);
    locationsRules.put(NAME_FIELD, nameRuleObject);
    locationsRules.put(COLOR_FIELD, colorRule);
    locationsRules.put(SYMBOL_FIELD, JsonRuleFactory.makeStringLengthRule(SYMBOL_STRING_LENGTH));
    locationsRules.put(INFO_FIELD, JsonRuleFactory.makeStringRule());
    locationsRules.put(BLOB_SIZE_FIELD, blobBoundRule);
    locationsRules.put(LIGHT_PERMITTIVITY_FIELD, lightBoundRule);
    locationsRules.put(BLOCKED_ENTRANCES_FIELD, blockedEntrancesRuleObject);
    locationsRules.put(SPAWNERS_FIELD, spawnersRuleObject);
    locationsRules.put(ITEMS_FIELD, itemsRuleObject);
    return JsonRuleFactory.makeObjectRule(locationsRules);
  }

  private JsonRule getNameRuleObject() {
    Map<String, JsonRule> nameRules = new HashMap<>();
    nameRules.put(SINGULAR_FIELD, JsonRuleFactory.makeStringRule());
    return JsonRuleFactory.makeObjectRule(nameRules);
  }

  private JsonRule getColorRuleGroup() {
    final JsonRule colorIntegerInboundRule = JsonRuleFactory.makeBoundIntegerRule(COLOR_MIN, COLOR_MAX);
    final JsonRule colorElementRule = JsonRuleFactory.makeVariableArrayRule(colorIntegerInboundRule);
    return JsonRuleFactory.makeGroupRule(JsonRuleFactory.makeArraySizeRule(COLOR_ARRAY_SIZE), colorElementRule);
  }

  private JsonRule getBlockedEntrancesRuleObject() {
    final JsonRule blockedEntranceLengthRule = JsonRuleFactory.makeStringLengthRule(BLOCKED_ENTRANCE_STRING_LENGTH);
    final JsonRule uppercaseRule = JsonRuleFactory.makeUppercaseStringRule();
    final JsonRule blockedEntranceGroupRule = JsonRuleFactory.makeGroupRule(blockedEntranceLengthRule, uppercaseRule);
    return JsonRuleFactory.makeVariableArrayRule(blockedEntranceGroupRule);
  }

  private JsonRule getSpawnersRuleObject(JsonRule populationRuleObject) {
    Map<String, JsonRule> spawnersRules = new HashMap<>();
    spawnersRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    spawnersRules.put(DELAY_FIELD, JsonRuleFactory.makeIntegerRule());
    spawnersRules.put(POPULATION_FIELD, populationRuleObject);
    final JsonRule spawnerElementsRuleObject =
        JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeObjectRule(spawnersRules));
    return JsonRuleFactory.makeOptionalRule(spawnerElementsRuleObject);
  }

  private JsonRule getPopulationRuleObject() {
    Map<String, JsonRule> populationRules = new HashMap<>();
    final JsonRule integerRule = JsonRuleFactory.makeIntegerRule();
    populationRules.put(MINIMUM_FIELD, integerRule);
    populationRules.put(MAXIMUM_FIELD, integerRule);
    return JsonRuleFactory.makeObjectRule(populationRules);
  }

  private JsonRule getItemsRuleObject() {
    Map<String, JsonRule> itemsRules = new HashMap<>();
    final JsonRule probabilityBoundRule = JsonRuleFactory.makeBoundDoubleRule(PROBABILITY_MIN, PROBABILITY_MAX);
    itemsRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    itemsRules.put(PROBABILITY_FIELD, probabilityBoundRule);
    final JsonRule itemsObjectRule = JsonRuleFactory.makeObjectRule(itemsRules);
    final JsonRule itemElementsRuleObject = JsonRuleFactory.makeVariableArrayRule(itemsObjectRule);
    return JsonRuleFactory.makeOptionalRule(itemElementsRuleObject);
  }

  private JsonRule getLocationsFileRuleObject(JsonRule locationsRuleObject) {
    Map<String, JsonRule> locationsFileRules = new HashMap<>();
    locationsFileRules.put(LOCATIONS_FIELD, JsonRuleFactory.makeVariableArrayRule(locationsRuleObject));
    return JsonRuleFactory.makeObjectRule(locationsFileRules);
  }

}
