package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.Percentage;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A CreaturePresetFactory that uses JSON files.
 */
public class JsonCreaturePresetFactory implements CreaturePresetFactory {

  private static final int DEFAULT_INVENTORY_ITEM_LIMIT = 100;
  private static final double DEFAULT_INVENTORY_WEIGHT_LIMIT = 100.0;
  private final String filename;

  public JsonCreaturePresetFactory(String filename) {
    this.filename = filename;
  }

  /**
   * Attempts to read a string from the provided JSON object, returning null if the string is not present or if the
   * value is not a string.
   *
   * @param jsonObject a JsonObject, not null
   * @param name a String, not null
   * @return a String or null
   */
  @Nullable
  private static String getStringFromJsonObject(@NotNull JsonObject jsonObject, @NotNull String name) {
    JsonValue value = jsonObject.get(name);
    if (value == null || !value.isString()) {
      return null;
    } else {
      return value.asString();
    }
  }

  @Nullable
  private static Percentage getPercentageFromJsonObject(@NotNull JsonObject jsonObject, @NotNull String name) {
    String percentageString = getStringFromJsonObject(jsonObject, name);
    if (percentageString != null) {
      if (Percentage.isValidPercentageString(percentageString)) {
        return Percentage.fromString(percentageString);
      } else {
        throw new IllegalStateException("JSON contains invalid percentage string: " + percentageString + ".");
      }
    }
    return null;
  }

  private static List<Id> getInventory(JsonObject object) {
    if (object.get("inventory") == null) {
      return Collections.emptyList();
    } else {
      List<Id> list = new ArrayList<>();
      for (JsonValue value : object.get("inventory").asArray()) {
        list.add(new Id(value.asString()));
      }
      return list;
    }
  }

  private static List<Drop> getDrops(JsonObject object) {
    if (object.get("drops") == null) {
      return Collections.emptyList();
    } else {
      List<Drop> list = new ArrayList<>();
      for (JsonValue value : object.get("drops").asArray()) {
        JsonArray dropArray = value.asArray();
        list.add(new Drop(new Id(dropArray.get(0).asString()), new Percentage(dropArray.get(1).asDouble())));
      }
      return list;
    }
  }

  private static void setVisibility(CreaturePreset preset, JsonObject presetObject) {
    Percentage visibilityPercentage = getPercentageFromJsonObject(presetObject, "visibility");
    if (visibilityPercentage != null) {
      preset.setVisibility(visibilityPercentage);
    }
  }

  private static void setLuminosityIfPresent(CreaturePreset preset, JsonObject presetObject) {
    Percentage luminosityPercentage = getPercentageFromJsonObject(presetObject, "luminosity");
    if (luminosityPercentage != null) {
      preset.setLuminosity(new Luminosity(luminosityPercentage));
    }
  }

  private static void setWeaponIfPreset(CreaturePreset preset, JsonObject presetObject) {
    String weapon = getStringFromJsonObject(presetObject, "weapon");
    if (weapon != null) {
      preset.setWeaponId(new Id(weapon));
    }
  }

  @Override
  public Collection<CreaturePreset> getCreaturePresets() {
    Collection<CreaturePreset> creaturePresetMap = new ArrayList<>();
    JsonObject object = JsonObjectFactory.makeJsonObject(filename);
    for (JsonValue value : object.get("creatures").asArray()) {
      JsonObject presetObject = value.asObject();
      CreaturePreset preset = new CreaturePreset();
      preset.setId(new Id(presetObject.get("id").asString()));
      preset.setType(presetObject.get("type").asString());
      preset.setName(NameFactory.fromJsonObject(presetObject.get("name").asObject()));
      if (presetObject.get("tags") != null) {
        preset.setTagSet(TagSet.fromJsonArray(presetObject.get("tags").asArray(), Creature.Tag.class));
      } else {
        preset.setTagSet(TagSet.makeEmptyTagSet(Creature.Tag.class));
      }
      preset.setInventoryItemLimit(presetObject.getInt("inventoryItemLimit", DEFAULT_INVENTORY_ITEM_LIMIT));
      preset.setInventoryWeightLimit(presetObject.getDouble("inventoryWeightLimit", DEFAULT_INVENTORY_WEIGHT_LIMIT));
      preset.setItems(getInventory(presetObject));
      preset.setDropList(getDrops(presetObject));
      setLuminosityIfPresent(preset, presetObject);
      setVisibility(preset, presetObject);
      preset.setWeight(Weight.newInstance(presetObject.get("weight").asDouble()));
      preset.setHealth(presetObject.get("health").asInt());
      preset.setAttack(presetObject.get("attack").asInt());
      setWeaponIfPreset(preset, presetObject);
      preset.setAttackAlgorithmId(AttackAlgorithmId.valueOf(presetObject.get("attackAlgorithmID").asString()));
      creaturePresetMap.add(preset);
    }
    DungeonLogger.info("Loaded " + creaturePresetMap.size() + " creature presets.");
    return creaturePresetMap;
  }

}
