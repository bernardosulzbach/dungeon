package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LocationsJsonFileTest extends ResourcesTypeTest {

  private static final JsonObject locationsJson = getJsonObjectByJsonFile(JsonFileEnum.LOCATIONS);
  private static final String ID_FIELD = "id";
  private static final String TYPE_FIELD = "type";
  private static final String NAME_FIELD = "name";
  private static final String COLOR_FIELD = "color";
  private static final String SYMBOL_FIELD = "symbol";
  private static final String INFO_FIELD = "info";
  private static final String BLOB_SIZE_FIELD = "blobSize";
  private static final String LIGHT_PERMITTIVITY_FIELD = "lightPermittivity";
  private static final String BLOCKED_ENTRANCES_FIELD = "blockedEntrances";
  private static final String SPAWNERS_FIELD = "spawners";
  private static final String ITEMS_FIELD = "items";
  private static final String SINGULAR_FIELD = "singular";
  private static final String DELAY_FIELD = "delay";
  private static final String POPULATION_FIELD = "population";
  private static final String MINIMUM_FIELD = "minimum";
  private static final String MAXIMUM_FIELD = "maximum";
  private static final String PROBABILITY_FIELD = "probability";
  private static final String LOCATIONS_FIELD = "locations";
  private static final int BLOB_SIZE_MIN_VALUE = 0;
  private static final int BLOB_SIZE_MAX_VALUE = 100;
  private static final double PERMITTIVITY_MIN_VALUE = 0.0;
  private static final double PERMITTIVITY_MAX_VALUE = 1.0;
  private static final int COLOR_MIN_VALUE = 0;
  private static final int COLOR_MAX_VALUE = 255;
  private static final int COLOR_ARRAY_SIZE = 3;
  private static final int BLOCKED_ENTRANCE_STRING_LENGTH = 1;
  private static final double PROBABILITY_MIN_VALUE = 0.0;
  private static final double PROBABILITY_MAX_VALUE = 1.0;
  private static final int SYMBOL_STRING_LENGTH = 1;

  @Test
  public void testIsFileHasValidStructure() {
    JsonRule itemsRuleObject = getItemsRuleObject();
    JsonRule populationRuleObject = getPopulationRuleObject();
    JsonRule spawnersRuleObject = getSpawnersRuleObject(populationRuleObject);
    JsonRule blockedEntrancesRuleObject = getBlockedEntrancesRuleObject();
    JsonRule colorRule = getColorRuleGroup();
    JsonRule nameRuleObject = getNameRuleObject();
    JsonRule locationRuleObject =
        getLocationsRuleObject(itemsRuleObject, spawnersRuleObject, blockedEntrancesRuleObject, colorRule,
            nameRuleObject);
    JsonRule locationsFileJsonRule = getLocationsFileRuleObject(locationRuleObject);
    locationsFileJsonRule.validate(locationsJson);
  }

  private JsonRule getLocationsRuleObject(JsonRule itemsRuleObject, JsonRule spawnersRuleObject,
      JsonRule blockedEntrancesRuleObject, JsonRule colorRule, JsonRule nameRuleObject) {
    Map<String, JsonRule> locationsRules = new HashMap<>();
    locationsRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    locationsRules.put(TYPE_FIELD, JsonRuleFactory.makeIdRule());
    locationsRules.put(NAME_FIELD, nameRuleObject);
    locationsRules.put(COLOR_FIELD, colorRule);
    locationsRules.put(SYMBOL_FIELD, JsonRuleFactory.makeStringLengthRule(SYMBOL_STRING_LENGTH));
    locationsRules.put(INFO_FIELD, JsonRuleFactory.makeStringRule());
    locationsRules.put(BLOB_SIZE_FIELD, JsonRuleFactory.makeBoundIntegerRule(BLOB_SIZE_MIN_VALUE, BLOB_SIZE_MAX_VALUE));
    locationsRules.put(LIGHT_PERMITTIVITY_FIELD,
        JsonRuleFactory.makeBoundDoubleRule(PERMITTIVITY_MIN_VALUE, PERMITTIVITY_MAX_VALUE));
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
    JsonRule colorIntegerInboundRule = JsonRuleFactory.makeBoundIntegerRule(COLOR_MIN_VALUE, COLOR_MAX_VALUE);
    JsonRule colorElementRule = JsonRuleFactory.makeVariableArrayRule(colorIntegerInboundRule);
    return JsonRuleFactory.makeGroupRule(JsonRuleFactory.makeArraySizeRule(COLOR_ARRAY_SIZE), colorElementRule);
  }

  private JsonRule getBlockedEntrancesRuleObject() {
    JsonRule blockedEntranceElementGroupRule = JsonRuleFactory
        .makeGroupRule(JsonRuleFactory.makeStringLengthRule(BLOCKED_ENTRANCE_STRING_LENGTH),
            JsonRuleFactory.makeUppercaseStringRule());
    return JsonRuleFactory.makeVariableArrayRule(blockedEntranceElementGroupRule);
  }

  private JsonRule getSpawnersRuleObject(JsonRule populationRuleObject) {
    Map<String, JsonRule> spawnersRules = new HashMap<>();
    spawnersRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    spawnersRules.put(DELAY_FIELD, JsonRuleFactory.makeIntegerRule());
    spawnersRules.put(POPULATION_FIELD, populationRuleObject);
    JsonRule spawnerElementsRuleObject =
        JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeObjectRule(spawnersRules));
    return JsonRuleFactory.makeOptionalRule(spawnerElementsRuleObject);
  }

  private JsonRule getPopulationRuleObject() {
    Map<String, JsonRule> populationRules = new HashMap<>();
    populationRules.put(MINIMUM_FIELD, JsonRuleFactory.makeIntegerRule());
    populationRules.put(MAXIMUM_FIELD, JsonRuleFactory.makeIntegerRule());
    return JsonRuleFactory.makeObjectRule(populationRules);
  }

  private JsonRule getItemsRuleObject() {
    Map<String, JsonRule> itemsRules = new HashMap<>();
    itemsRules.put(ID_FIELD, JsonRuleFactory.makeIdRule());
    itemsRules
        .put(PROBABILITY_FIELD, JsonRuleFactory.makeBoundDoubleRule(PROBABILITY_MIN_VALUE, PROBABILITY_MAX_VALUE));
    JsonRule itemElementsRuleObject = JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeObjectRule(itemsRules));
    return JsonRuleFactory.makeOptionalRule(itemElementsRuleObject);
  }

  private JsonRule getLocationsFileRuleObject(JsonRule locationsRuleObject) {
    Map<String, JsonRule> locationsFileRules = new HashMap<>();
    locationsFileRules.put(LOCATIONS_FIELD, JsonRuleFactory.makeVariableArrayRule(locationsRuleObject));
    return JsonRuleFactory.makeObjectRule(locationsFileRules);
  }

}
