package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CreaturesJsonFileTest extends ResourcesTypeTest {

  private static final String ID_FIELD = "id";
  private static final String TYPE_FIELD = "type";
  private static final String NAME_FIELD = "name";
  private static final String TAGS_FIELD = "tags";
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
  private static final String CREATURES_JSON_FILE_NAME = "creatures.json";
  private static final String ATTACK_ALGORITHM_ID_FIELD = "attackAlgorithmID";
  private static final String INVENTORY_ITEM_LIMIT_FIELD = "inventoryItemLimit";
  private static final String INVENTORY_WEIGHT_LIMIT_FIELD = "inventoryWeightLimit";

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
    creatureRules.put(ID_FIELD, idRule);
    creatureRules.put(TYPE_FIELD, JsonRuleFactory.makeStringRule());
    creatureRules.put(NAME_FIELD, nameJsonRuleObject);
    final JsonRule variableUppercaseRule = JsonRuleFactory.makeVariableArrayRule(uppercaseJsonRule);
    creatureRules.put(TAGS_FIELD, JsonRuleFactory.makeOptionalRule(variableUppercaseRule));
    creatureRules.put(INVENTORY_ITEM_LIMIT_FIELD, JsonRuleFactory.makeOptionalRule(integerRule));
    final JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(integerRule);
    creatureRules.put(INVENTORY_WEIGHT_LIMIT_FIELD, optionalIntegerRule);
    final JsonRule variableIdRule = JsonRuleFactory.makeVariableArrayRule(idRule);
    creatureRules.put(INVENTORY_FIELD, JsonRuleFactory.makeOptionalRule(variableIdRule));
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

}
