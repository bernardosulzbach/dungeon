package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ItemsJsonFileTest extends ResourcesTypeTest {

  private static final String ID_FIELD = "id";
  private static final String TYPE_FIELD = "type";
  private static final String NAME_FIELD = "name";
  private static final String TAGS_FIELD = "tags";
  private static final String TEXT_FIELD = "text";
  private static final String ITEMS_FIELD = "items";
  private static final String SPELL_FIELD = "spell";
  private static final String UNIQUE_FIELD = "unique";
  private static final String WEIGHT_FIELD = "weight";
  private static final String DAMAGE_FIELD = "damage";
  private static final String PLURAL_FIELD = "plural";
  private static final String CURRENT_FIELD = "current";
  private static final String MAXIMUM_FIELD = "maximum";
  private static final String HIT_RATE_FIELD = "hitRate";
  private static final String SINGULAR_FIELD = "singular";
  private static final String NUTRITION_FIELD = "nutrition";
  private static final String INTEGRITY_FIELD = "integrity";
  private static final String VISIBILITY_FIELD = "visibility";
  private static final String LUMINOSITY_FIELD = "luminosity";
  private static final String ITEMS_JSON_FILE_NAME = "items.json";
  private static final String DECOMPOSITION_PERIOD_FIELD = "decompositionPeriod";
  private static final String INTEGRITY_DECREMENT_ON_HIT_FIELD = "integrityDecrementOnHit";
  private static final String INTEGRITY_DECREMENT_ON_EAT_FIELD = "integrityDecrementOnEat";
  private static final String DRINKABLE_DOSES_FIELD = "drinkableDoses";
  private static final String DRINKABLE_HEALING_FIELD = "drinkableHealing";
  private static final String INTEGRITY_DECREMENT_PER_DOSE_FIELD = "integrityDecrementPerDose";

  @Test
  public void testIsFileHasValidStructure() {
    final JsonRule integrityRuleObject = getIntegrityRuleObject();
    final JsonRule nameRuleObject = getNameRuleObject();
    final JsonRule itemsJsonRuleObject = getItemRuleObject(integrityRuleObject, nameRuleObject);
    final JsonRule itemsFileJsonRuleObject = getItemsFileJsonRuleObject(itemsJsonRuleObject);
    JsonObject itemsFileJsonObject = getJsonObjectByJsonFile(ITEMS_JSON_FILE_NAME);
    itemsFileJsonRuleObject.validate(itemsFileJsonObject);
  }

  private JsonRule getItemsFileJsonRuleObject(JsonRule itemsJsonRuleObject) {
    Map<String, JsonRule> itemsFileRules = new HashMap<>();
    itemsFileRules.put(ITEMS_FIELD, JsonRuleFactory.makeVariableArrayRule(itemsJsonRuleObject));
    return JsonRuleFactory.makeObjectRule(itemsFileRules);
  }

  private JsonRule getItemRuleObject(JsonRule integrityRuleObject, JsonRule nameRuleObject) {
    Map<String, JsonRule> itemRules = new HashMap<>();
    final JsonRule idRule = JsonRuleFactory.makeIdRule();
    final JsonRule stringRule = JsonRuleFactory.makeStringRule();
    final JsonRule percentRule = JsonRuleFactory.makePercentRule();
    final JsonRule integerRule = JsonRuleFactory.makeIntegerRule();
    final JsonRule optionalIntegerRule = JsonRuleFactory.makeOptionalRule(integerRule);
    itemRules.put(ID_FIELD, idRule);
    itemRules.put(TYPE_FIELD, stringRule);
    itemRules.put(NAME_FIELD, nameRuleObject);
    itemRules.put(TAGS_FIELD, JsonRuleFactory.makeVariableArrayRule(stringRule));
    itemRules.put(UNIQUE_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeBooleanRule()));
    itemRules.put(INTEGRITY_FIELD, integrityRuleObject);
    itemRules.put(WEIGHT_FIELD, JsonRuleFactory.makeBoundDoubleRule(Double.MIN_VALUE, 100));
    itemRules.put(VISIBILITY_FIELD, percentRule);
    itemRules.put(LUMINOSITY_FIELD, JsonRuleFactory.makeOptionalRule(percentRule));
    itemRules.put(DECOMPOSITION_PERIOD_FIELD, JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makePeriodRule()));
    itemRules.put(DAMAGE_FIELD, integerRule);
    itemRules.put(HIT_RATE_FIELD, percentRule);
    itemRules.put(INTEGRITY_DECREMENT_ON_HIT_FIELD, integerRule);
    itemRules.put(NUTRITION_FIELD, optionalIntegerRule);
    itemRules.put(INTEGRITY_DECREMENT_ON_EAT_FIELD, optionalIntegerRule);
    itemRules.put(DRINKABLE_DOSES_FIELD, optionalIntegerRule);
    itemRules.put(DRINKABLE_HEALING_FIELD, optionalIntegerRule);
    itemRules.put(INTEGRITY_DECREMENT_PER_DOSE_FIELD, optionalIntegerRule);
    itemRules.put(TEXT_FIELD, JsonRuleFactory.makeOptionalRule(stringRule));
    itemRules.put(SPELL_FIELD, JsonRuleFactory.makeOptionalRule(idRule));
    return JsonRuleFactory.makeObjectRule(itemRules);
  }

  private JsonRule getNameRuleObject() {
    Map<String, JsonRule> nameRules = new HashMap<>();
    final JsonRule stringRule = JsonRuleFactory.makeStringRule();
    final JsonRule optionalStringRule = JsonRuleFactory.makeOptionalRule(stringRule);
    nameRules.put(SINGULAR_FIELD, stringRule);
    nameRules.put(PLURAL_FIELD, optionalStringRule);
    return JsonRuleFactory.makeObjectRule(nameRules);
  }

  private JsonRule getIntegrityRuleObject() {
    Map<String, JsonRule> integrityRules = new HashMap<>();
    final JsonRule integerRule = JsonRuleFactory.makeIntegerRule();
    integrityRules.put(CURRENT_FIELD, integerRule);
    integrityRules.put(MAXIMUM_FIELD, integerRule);
    return JsonRuleFactory.makeObjectRule(integrityRules);
  }

}
