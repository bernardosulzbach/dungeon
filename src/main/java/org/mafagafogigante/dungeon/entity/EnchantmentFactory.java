package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.io.Version;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentFactory implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  private Map<Id, WeaponEnchantmentPreset> presets = new HashMap<>();

  /**
   * Construct an EnchantmentFactory from a JSON file.
   */
  public EnchantmentFactory(String filename) {
    JsonObject objects = JsonObjectFactory.makeJsonObject(filename);
    for (JsonValue value : objects.get("enchantments").asArray()) {
      JsonObject object = value.asObject();
      JsonObject damageObject = object.get("damage").asObject();
      Id id = new Id(object.get("id").asString());
      String name = object.get("name").asString();
      DamageType type = DamageType.valueOf(damageObject.get("type").asString());
      int amount = damageObject.get("amount").asInt();
      presets.put(id, new WeaponEnchantmentPreset(name, type, amount));
    }
  }

  public Enchantment makeEnchantment(@NotNull Id id) {
    return presets.get(id).makeEnchantment();
  }

}
