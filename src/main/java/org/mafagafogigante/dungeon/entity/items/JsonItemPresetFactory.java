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

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.Collection;

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
      preset.setTagSet(new TagSetParser<>(Item.Tag.class, itemObject.get("tags")).parse());
      preset.setUnique(itemObject.getBoolean("unique", false));
      if (itemObject.get("decompositionPeriod") != null) {
        long seconds = DungeonTimeParser.parsePeriod(itemObject.get("decompositionPeriod").asString()).getSeconds();
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
        preset.setDrinkableHealing(itemObject.get("drinkableHealing").asInt());
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
