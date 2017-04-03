package org.mafagafogigante.dungeon.io;

import static org.mafagafogigante.dungeon.io.JsonSearchUtil.searchJsonValuesByPath;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreaturesJsonFileTest extends ResourcesTypeTest {

  private static final String ID_FIELD = "id";
  private static final String TYPE_FIELD = "type";
  private static final String NAME_FIELD = "name";
  private static final String TAGS_FIELD = "tags";
  private static final String DROPS_FIELD = "drops";
  private static final String PLURAL_FIELD = "plural";
  private static final String WEIGHT_FIELD = "weight";
  private static final String HEALTH_FIELD = "health";
  private static final String ATTACK_FIELD = "attack";
  private static final String WEAPON_FIELD = "weapon";
  private static final String SINGULAR_FIELD = "singular";
  private static final String CREATURES_FIELD = "creatures";
  private static final String INVENTORY_FIELD = "inventory";
  private static final String LUMINOSITY_FIELD = "luminosity";
  private static final String VISIBILITY_FIELD = "visibility";
  private static final String ATTACK_ALGORITHM_ID_FIELD = "attackAlgorithmID";
  private static final String INVENTORY_ITEM_LIMIT_FIELD = "inventoryItemLimit";
  private static final String INVENTORY_WEIGHT_LIMIT_FIELD = "inventoryWeightLimit";
  private static final String LOCATIONS_SPAWNERS_ID_PATH = "locations.spawners.id";
  private static final List<String> CREATURES_NOT_EXIST_IN_LOCATION_SPAWNERS =
      new ArrayList<>(Arrays.asList("HERO", "DUMMY"));

  @Test
  public void testIsFileHasValidStructure() {
    final JsonRule nameJsonRuleObject = getNameRuleObject();
    final JsonRule creatureRuleObject = getCreatureRuleObject(nameJsonRuleObject);
    final JsonRule creaturesFileJsonRuleObject = getCreaturesFileRuleObject(creatureRuleObject);
    JsonObject creaturesFileJsonObject = getJsonObjectByJsonFile(CREATURES_JSON_FILE_NAME);
    creaturesFileJsonRuleObject.validate(creaturesFileJsonObject);
  }

  private JsonRule getCreaturesFileRuleObject(JsonRule creatureRuleObject) {
    Map<String, JsonRule> creaturesFileRules = new HashMap<>();
    creaturesFileRules.put(CREATURES_FIELD, JsonRuleFactory.makeVariableArrayRule(creatureRuleObject));
    return JsonRuleFactory.makeObjectRule(creaturesFileRules);
  }

  private JsonRule getCreatureRuleObject(JsonRule nameJsonRuleObject) {
    Map<String, JsonRule> creatureRules = new HashMap<>();
    final JsonRule idRule = JsonRuleFactory.makeIdRule();
    final JsonRule percentRule = JsonRuleFactory.makePercentRule();
    final JsonRule integerRule = JsonRuleFactory.makeIntegerRule();
    final JsonRule uppercaseJsonRule = JsonRuleFactory.makeUppercaseStringRule();
    creatureRules.put(ID_FIELD, getIdInLocationsSpawnersJsonRule());
    creatureRules.put(TYPE_FIELD, JsonRuleFactory.makeStringRule());
    creatureRules.put(NAME_FIELD, nameJsonRuleObject);
    final JsonRule variableUppercaseRule = JsonRuleFactory.makeVariableArrayRule(uppercaseJsonRule);
    creatureRules.put(TAGS_FIELD, JsonRuleFactory.makeOptionalRule(variableUppercaseRule));
    creatureRules.put(INVENTORY_ITEM_LIMIT_FIELD, JsonRuleFactory.makeOptionalRule(integerRule));
    final JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(integerRule);
    creatureRules.put(INVENTORY_WEIGHT_LIMIT_FIELD, optionalIntegerRule);
    final JsonRule variableIdRule = JsonRuleFactory.makeVariableArrayRule(idRule);
    creatureRules.put(INVENTORY_FIELD, JsonRuleFactory.makeOptionalRule(variableIdRule));
    creatureRules.put(DROPS_FIELD, getDropsRule());
    creatureRules.put(LUMINOSITY_FIELD, JsonRuleFactory.makeOptionalRule(percentRule));
    creatureRules.put(VISIBILITY_FIELD, percentRule);
    creatureRules.put(WEIGHT_FIELD, JsonRuleFactory.makeBoundDoubleRule(Double.MIN_VALUE, Double.MAX_VALUE));
    creatureRules.put(HEALTH_FIELD, integerRule);
    creatureRules.put(ATTACK_FIELD, integerRule);
    creatureRules.put(ATTACK_ALGORITHM_ID_FIELD, idRule);
    creatureRules.put(WEAPON_FIELD, JsonRuleFactory.makeOptionalRule(idRule));
    return JsonRuleFactory.makeObjectRule(creatureRules);
  }

  private JsonRule getNameRuleObject() {
    Map<String, JsonRule> nameRules = new HashMap<>();
    final JsonRule optionalStringRule = JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeStringRule());
    nameRules.put(SINGULAR_FIELD, JsonRuleFactory.makeStringRule());
    nameRules.put(PLURAL_FIELD, optionalStringRule);
    return JsonRuleFactory.makeObjectRule(nameRules);
  }

  private JsonRule getDropsRule() {
    JsonRule doubleRule = JsonRuleFactory.makeBoundDoubleRule(0.0, 1.0);
    JsonRule idRule = JsonRuleFactory.makeIdRule();
    List<JsonRule> dropRules = new ArrayList<>(Arrays.asList(idRule, doubleRule));
    JsonRule innerArrayRule = JsonRuleFactory.makeFixedArrayRule(dropRules);
    JsonRule outerArrayRule = JsonRuleFactory.makeVariableArrayRule(innerArrayRule);
    return JsonRuleFactory.makeOptionalRule(outerArrayRule);
  }

  private JsonRule getIdInLocationsSpawnersJsonRule() {
    JsonObject locationsFileJsonObject = getJsonObjectByJsonFile(LOCATIONS_JSON_FILE_NAME);
    Set<JsonValue> locationSpawnerIdValues =
        searchJsonValuesByPath(LOCATIONS_SPAWNERS_ID_PATH, locationsFileJsonObject);
    Set<String> creatureIdsInLocationsSpawners = new HashSet<>();
    for (JsonValue locationSpawnerId : locationSpawnerIdValues) {
      creatureIdsInLocationsSpawners.add(locationSpawnerId.asString());
    }
    creatureIdsInLocationsSpawners.addAll(CREATURES_NOT_EXIST_IN_LOCATION_SPAWNERS);
    return JsonRuleFactory.makeSpecificIdJsonRule(creatureIdsInLocationsSpawners);
  }

}
