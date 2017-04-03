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
  private static final String DRINKABLE_DOSES_FIELD = "drinkableDoses";
  private static final String DRINKABLE_HEALING_FIELD = "drinkableHealing";
  private static final String DECOMPOSITION_PERIOD_FIELD = "decompositionPeriod";
  private static final String INTEGRITY_DECREMENT_ON_HIT_FIELD = "integrityDecrementOnHit";
  private static final String INTEGRITY_DECREMENT_ON_EAT_FIELD = "integrityDecrementOnEat";
  private static final String INTEGRITY_DECREMENT_PER_DOSE_FIELD = "integrityDecrementPerDose";
  private static final String CREATURES_DROPS_PATH = "creatures.drops";
  private static final String LOCATIONS_ITEMS_ID_PATH = "locations.items.id";
  private static final List<String> ITEMS_NOT_EXIST_IN_LOCATION_ITEMS = new ArrayList<>(Arrays
      .asList("APPLE", "BANANA", "CHERRY", "PINEAPPLE", "POTATO", "STRAWBERRY", "WATERMELON", "BEAR_BEEF", "WOLF_BEEF",
          "TIGER_BEEF", "RABBIT_BEEF", "BOAR_BEEF", "CROCODILE_BEEF", "COW_BEEF", "FOX_BEEF", "IGUANA_BEEF",
          "KOMODO_DRAGON_BEEF", "GIANT_RAT_BEEF", "ORC_SWORD", "BONE", "SKULL", "SPIDER_VENOM_GLAND",
          "PAGE_FROM_VOLUND_LOKE_FREY_S_DIARY", "WOOD_LOG", "RADIANT_SKIN", "HARPY_FEATHER"));
  private static final List<String> ITEMS_NOT_EXIST_IN_CREATURE_DROPS_ITEMS = new ArrayList<>(Arrays
      .asList("TORCH", "HEALING_POTION", "HEALING_DRAUGHT", "CLUB", "DAGGER", "DAGGER_OF_INFINITY", "FLAIL",
          "ETHEREAL_FLAIL", "LONGSWORD", "GLASS_SWORD", "THE_SUN_BLADE", "MACE", "SPEAR", "STAFF", "STONE", "SWORD",
          "TOME_OF_HEAL", "TOME_OF_FINGER_OF_DEATH", "TOME_OF_VEIL_OF_DARKNESS", "TOME_OF_UNVEIL",
          "HISTORY_OF_THE_THIRD_ERA", "CORVUS_EDGE", "PAGE_FROM_VOLUND_LOKE_FREY_S_DIARY", "RED_ONYX_WATCH",
          "POCKET_WATCH", "WRIST_WATCH"));

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
    JsonRule idJsonRule =
        JsonRuleFactory.makeGroupRule(getIdInLocationsItemsJsonRule(), getIdInCreaturesDropsJsonRule());
    itemRules.put(ID_FIELD, idJsonRule);
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

  private JsonRule getIdInLocationsItemsJsonRule() {
    JsonObject locationsFileJsonObject = getJsonObjectByJsonFile(LOCATIONS_JSON_FILE_NAME);
    Set<JsonValue> itemIdsInLocationItems = searchJsonValuesByPath(LOCATIONS_ITEMS_ID_PATH, locationsFileJsonObject);
    Set<String> itemIdsInLocationsItems = new HashSet<>();
    for (JsonValue locationItemId : itemIdsInLocationItems) {
      itemIdsInLocationsItems.add(locationItemId.asString());
    }
    itemIdsInLocationsItems.addAll(ITEMS_NOT_EXIST_IN_LOCATION_ITEMS);
    return JsonRuleFactory.makeSpecificIdJsonRule(itemIdsInLocationsItems);
  }

  private JsonRule getIdInCreaturesDropsJsonRule() {
    JsonObject creaturesFileJsonObject = getJsonObjectByJsonFile(CREATURES_JSON_FILE_NAME);
    Set<JsonValue> creaturesDrops = searchJsonValuesByPath(CREATURES_DROPS_PATH, creaturesFileJsonObject);
    Set<String> itemIdsInCreaturesDrops = new HashSet<>();
    for (JsonValue creatureDrops : creaturesDrops) {
      for (JsonValue creatureDrop : creatureDrops.asArray()) {
        String itemIdFromDrop = creatureDrop.asArray().get(0).asString();
        itemIdsInCreaturesDrops.add(itemIdFromDrop);
      }
    }
    itemIdsInCreaturesDrops.addAll(ITEMS_NOT_EXIST_IN_CREATURE_DROPS_ITEMS);
    return JsonRuleFactory.makeSpecificIdJsonRule(itemIdsInCreaturesDrops);
  }

}
