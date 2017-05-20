package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.LocationPreset;
import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationsJsonFileTest extends ResourcesTypeTest {

  private static final String ID_FIELD = "id";
  private static final String TYPE_FIELD = "type";
  private static final String NAME_FIELD = "name";
  private static final String INFO_FIELD = "info";
  private static final String TAGS_FIELD = "tags";
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
    JsonRule itemsRule = getItemsRule();
    JsonRule populationRule = getPopulationRule();
    JsonRule spawnersRule = getSpawnersRule(populationRule);
    JsonRule blockedEntrancesRule = getBlockedEntrancesRule();
    JsonRule colorRule = getColorRuleGroup();
    JsonRule nameRule = getNameRule();
    Map<String, JsonRule> locationsRules = new HashMap<>();
    JsonRule idRule = JsonRuleFactory.makeIdRule();
    JsonRule blobBoundRule = JsonRuleFactory.makeBoundIntegerRule(BLOB_SIZE_MIN, BLOB_SIZE_MAX);
    JsonRule lightBoundRule = JsonRuleFactory.makeBoundDoubleRule(PERMITTIVITY_MIN, PERMITTIVITY_MAX);
    locationsRules.put(ID_FIELD, idRule);
    JsonRule locationTypeEnumRule = JsonRuleFactory.makeEnumJsonRule(LocationPreset.Type.class);
    locationsRules.put(TYPE_FIELD, locationTypeEnumRule);
    locationsRules.put(NAME_FIELD, nameRule);
    locationsRules.put(COLOR_FIELD, colorRule);
    locationsRules.put(SYMBOL_FIELD, JsonRuleFactory.makeStringLengthRule(SYMBOL_STRING_LENGTH));
    locationsRules.put(INFO_FIELD, JsonRuleFactory.makeStringRule());
    locationsRules.put(BLOB_SIZE_FIELD, blobBoundRule);
    locationsRules.put(LIGHT_PERMITTIVITY_FIELD, lightBoundRule);
    locationsRules.put(BLOCKED_ENTRANCES_FIELD, blockedEntrancesRule);
    locationsRules.put(SPAWNERS_FIELD, spawnersRule);
    JsonRule idArrayRule = JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeIdRule());
    JsonRule tagsRule = JsonRuleFactory.makeOptionalRule(idArrayRule);
    locationsRules.put(TAGS_FIELD, tagsRule);
    locationsRules.put(ITEMS_FIELD, itemsRule);
    JsonRule locationRule = JsonRuleFactory.makeObjectRule(locationsRules);
    JsonRule locationsFileJsonRule = getLocationsFileRule(locationRule);
    String locationsFilename = ResourceNameResolver.resolveName(DungeonResource.LOCATIONS);
    JsonObject locationsFileJsonObject = ResourcesTypeTest.getJsonObjectByJsonFilename(locationsFilename);
    locationsFileJsonRule.validate(locationsFileJsonObject);
  }

  private JsonRule getNameRule() {
    Map<String, JsonRule> nameRules = new HashMap<>();
    nameRules.put(SINGULAR_FIELD, JsonRuleFactory.makeStringRule());
    return JsonRuleFactory.makeObjectRule(nameRules);
  }

  private JsonRule getColorRuleGroup() {
    JsonRule colorIntegerInboundRule = JsonRuleFactory.makeBoundIntegerRule(COLOR_MIN, COLOR_MAX);
    JsonRule colorElementRule = JsonRuleFactory.makeVariableArrayRule(colorIntegerInboundRule);
    return JsonRuleFactory.makeGroupRule(JsonRuleFactory.makeArraySizeRule(COLOR_ARRAY_SIZE), colorElementRule);
  }

  private JsonRule getBlockedEntrancesRule() {
    JsonRule blockedEntranceLengthRule = JsonRuleFactory.makeStringLengthRule(BLOCKED_ENTRANCE_STRING_LENGTH);
    JsonRule uppercaseRule = JsonRuleFactory.makeUppercaseStringRule();
    JsonRule blockedEntranceGroupRule = JsonRuleFactory.makeGroupRule(blockedEntranceLengthRule, uppercaseRule);
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
    JsonRule integerRule = JsonRuleFactory.makeIntegerRule();
    populationRules.put(MINIMUM_FIELD, integerRule);
    populationRules.put(MAXIMUM_FIELD, integerRule);
    return JsonRuleFactory.makeObjectRule(populationRules);
  }

  private JsonRule getItemsRule() {
    Map<String, JsonRule> itemsRules = new HashMap<>();
    JsonRule probabilityBoundRule = JsonRuleFactory.makeBoundDoubleRule(PROBABILITY_MIN, PROBABILITY_MAX);
    String itemsFilename = ResourceNameResolver.resolveName(DungeonResource.ITEMS);
    List<Id> idGroup = extractIds(getJsonObjectByJsonFilename(itemsFilename));
    itemsRules.put(ID_FIELD, JsonRuleFactory.makeIdSetRule(idGroup));
    itemsRules.put(PROBABILITY_FIELD, probabilityBoundRule);
    JsonRule itemsObjectRule = JsonRuleFactory.makeObjectRule(itemsRules);
    JsonRule itemElementsRule = JsonRuleFactory.makeVariableArrayRule(itemsObjectRule);
    return JsonRuleFactory.makeOptionalRule(itemElementsRule);
  }

  private List<Id> extractIds(JsonObject object) {
    if (object.names().size() != 1) {
      Assert.fail();
    }
    JsonArray values = object.get(object.names().get(0)).asArray();
    List<Id> ids = new ArrayList<>();
    for (JsonValue value : values) {
      ids.add(new Id(value.asObject().get("id").asString()));
    }
    return ids;
  }

  private JsonRule getLocationsFileRule(JsonRule locationsRule) {
    Map<String, JsonRule> locationsFileRules = new HashMap<>();
    locationsFileRules.put(LOCATIONS_FIELD, JsonRuleFactory.makeVariableArrayRule(locationsRule));
    return JsonRuleFactory.makeObjectRule(locationsFileRules);
  }

}
