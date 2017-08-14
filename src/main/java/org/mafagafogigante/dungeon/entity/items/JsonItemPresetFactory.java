package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.date.DungeonTimeParser;
import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.entity.items.Item.Tag;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.io.TagSetParser;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.Percentage;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An ItemPresetFactory based on JSON files.
 */
public class JsonItemPresetFactory implements ItemPresetFactory {

  private final String filename;

  public JsonItemPresetFactory(String filename) {
    this.filename = filename;
  }

  @Override
  public Collection<ItemPreset> getItemPresets() {
    JsonObject objects = JsonObjectFactory.makeJsonObject(filename);
    Collection<ItemPreset> itemPresets = new ArrayList<>();
    for (JsonValue value : objects.get("items").asArray()) {
      JsonObject itemObject = value.asObject();
      ItemPreset preset = new ItemPreset();
      Id id = new Id(itemObject.get("id").asString());
      preset.setId(id);
      preset.setType(itemObject.get("type").asString());
      preset.setName(NameFactory.fromJsonObject(itemObject.get("name").asObject()));
      preset.setRarity(Rarity.valueOf(itemObject.get("rarity").asString()));
      preset.setTagSet(new TagSetParser<>(Item.Tag.class, itemObject.get("tags")).parse());
      if (itemObject.get("enchantments") != null) {
        for (JsonValue enchantment : itemObject.get("enchantments").asArray()) {
          Id enchantmentId = new Id(enchantment.asObject().get("id").asString());
          double probability = enchantment.asObject().get("probability").asDouble();
          preset.getEnchantmentRules().add(enchantmentId, probability);
        }
      }
      preset.setUnique(itemObject.getBoolean("unique", false));
      if (itemObject.get("decompositionPeriod") != null) {
        long seconds = DungeonTimeParser.parseDuration(itemObject.get("decompositionPeriod").asString()).getSeconds();
        preset.setPutrefactionPeriod(seconds);
      }
      JsonObject integrity = itemObject.get("integrity").asObject();
      preset.setIntegrity(new Integrity(integrity.get("current").asInt(), integrity.get("maximum").asInt()));
      preset.setVisibility(Percentage.fromString(itemObject.get("visibility").asString()));
      if (itemObject.get("luminosity") != null) {
        preset.setLuminosity(new Luminosity(Percentage.fromString(itemObject.get("luminosity").asString())));
      }
      preset.setWeight(Weight.newInstance(itemObject.get("weight").asDouble()));
      preset.setDamage(itemObject.get("damage").asInt());
      preset.setHitRate(Percentage.fromString(itemObject.get("hitRate").asString()));
      preset.setIntegrityDecrementOnHit(itemObject.get("integrityDecrementOnHit").asInt());
      if (itemObject.get("nutrition") != null) {
        preset.setNutrition(itemObject.get("nutrition").asInt());
      }
      if (itemObject.get("integrityDecrementOnEat") != null) {
        preset.setIntegrityDecrementOnEat(itemObject.get("integrityDecrementOnEat").asInt());
      }
      if (preset.getTagSet().hasTag(Tag.BOOK)) {
        preset.setText(itemObject.get("text").asString());
      }
      if (preset.getTagSet().hasTag(Tag.DRINKABLE)) {
        preset.setDrinkableDoses(itemObject.get("drinkableDoses").asInt());
        for (JsonValue effectValue : itemObject.get("drinkableEffects").asArray()) {
          JsonArray effectArray = effectValue.asArray();
          List<JsonValue> values = effectArray.values();
          Id effectId = new Id(values.get(0).asString());
          List<String> effectParameters = new ArrayList<>();
          for (int i = 1; i < values.size(); i++) {
            if (values.get(i).isString()) {
              // Calling toString() makes a JSON String, which includes the quotation marks.
              effectParameters.add(values.get(i).asString());
            } else {
              effectParameters.add(values.get(i).toString());
            }
          }
          preset.addDrinkableEffect(effectId, effectParameters);
        }
        preset.setIntegrityDecrementPerDose(itemObject.get("integrityDecrementPerDose").asInt());
      }
      if (itemObject.get("spell") != null) {
        preset.setSpellId(itemObject.get("spell").asString());
      }
      itemPresets.add(preset);
    }
    DungeonLogger.info("Loaded " + itemPresets.size() + " item presets.");
    return itemPresets;
  }

}
