/*
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.game;

import org.dungeon.achievements.AchievementStore;
import org.dungeon.entity.creatures.CreatureFactory;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.io.DungeonLogger;
import org.dungeon.io.JsonObjectFactory;
import org.dungeon.skill.SkillDefinition;
import org.dungeon.util.StopWatch;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The class that stores all the game data that is loaded and not serialized.
 */
public final class GameData {

  private static final LocationPresetStore locationPresetStore = new LocationPresetStore();
  public static String LICENSE;
  private static String tutorial = null;
  private static Map<Id, SkillDefinition> skillDefinitions = new HashMap<Id, SkillDefinition>();

  private GameData() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  public static String getTutorial() {
    if (tutorial == null) {
      loadTutorial();
    }
    return tutorial;
  }

  /**
   * Triggers essential game data loading.
   */
  static void loadGameData() {
    StopWatch stopWatch = new StopWatch();
    DungeonLogger.info("Started loading the game data.");
    effectivelyLoadGameData();
    DungeonLogger.info("Finished loading the game data. Took " + stopWatch.toString() + ".");
  }

  /**
   * Effectively loads the game data.
   */
  private static void effectivelyLoadGameData() {
    ItemFactory.loadItemPresets();
    CreatureFactory.loadCreaturePresetsAndMakeCorpsePresets();
    ItemFactory.blockNewItemPresets(); // Must happen after CreatureFactory makes the corpse presets.
    createSkills();
    loadLocationPresets();
    AchievementStore.initialize();
    loadLicense();
  }

  /**
   * Creates all the Skills (hardcoded).
   */
  private static void createSkills() {
    if (!skillDefinitions.isEmpty()) {
      throw new AssertionError();
    }

    SkillDefinition fireball = new SkillDefinition("FIREBALL", "Fireball", 10, 0, 6);
    skillDefinitions.put(fireball.id, fireball);

    SkillDefinition burningGround = new SkillDefinition("BURNING_GROUND", "Burning Ground", 18, 0, 12);
    skillDefinitions.put(burningGround.id, burningGround);

    SkillDefinition repair = new SkillDefinition("REPAIR", "Repair", 0, 40, 10);
    skillDefinitions.put(repair.id, repair);
    skillDefinitions = Collections.unmodifiableMap(skillDefinitions);
  }

  private static void loadLocationPresets() {
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject("locations.json");
    for (JsonValue jsonValue : jsonObject.get("locations").asArray()) {
      JsonObject presetObject = jsonValue.asObject();
      Id id = new Id(presetObject.get("id").asString());
      LocationPreset.Type type = LocationPreset.Type.valueOf(presetObject.get("type").asString());
      Name name = NameFactory.nameFromJsonObject(presetObject.get("name").asObject());
      LocationPreset preset = new LocationPreset(id, type, name);
      char symbol = presetObject.get("symbol").asString().charAt(0);
      preset.setDescription(new LocationDescription(symbol, colorFromJsonArray(presetObject.get("color").asArray())));
      preset.getDescription().setInfo(presetObject.get("info").asString());
      preset.setBlobSize(presetObject.get("blobSize").asInt());
      preset.setLightPermittivity(presetObject.get("lightPermittivity").asDouble());
      if (presetObject.get("spawners") != null) {
        for (JsonValue spawnerValue : presetObject.get("spawners").asArray()) {
          JsonObject spawner = spawnerValue.asObject();
          String spawnerId = spawner.get("id").asString();
          int population = spawner.get("population").asInt();
          int delay = spawner.get("delay").asInt();
          preset.addSpawner(new SpawnerPreset(spawnerId, population, delay));
        }
      }
      if (presetObject.get("items") != null) {
        for (JsonValue itemValue : presetObject.get("items").asArray()) {
          JsonObject item = itemValue.asObject();
          String itemId = item.get("id").asString();
          double probability = item.get("probability").asDouble();
          preset.addItem(itemId, probability);
        }
      }
      if (presetObject.get("blockedEntrances") != null) {
        for (JsonValue abbreviation : presetObject.get("blockedEntrances").asArray()) {
          preset.block(Direction.fromAbbreviation(abbreviation.asString()));
        }
      }
      locationPresetStore.addLocationPreset(preset);
    }
    DungeonLogger.info("Loaded " + locationPresetStore.getSize() + " location presets.");
  }

  private static Color colorFromJsonArray(JsonArray color) {
    return new Color(color.get(0).asInt(), color.get(1).asInt(), color.get(2).asInt());
  }

  private static void loadLicense() {
    JsonObject license = JsonObjectFactory.makeJsonObject("license.json");
    LICENSE = license.get("license").asString();
  }

  private static void loadTutorial() {
    if (tutorial != null) { // Should only be called once.
      throw new AssertionError();
    }
    tutorial = JsonObjectFactory.makeJsonObject("tutorial.json").get("tutorial").asString();
  }

  public static Map<Id, SkillDefinition> getSkillDefinitions() {
    return skillDefinitions;
  }

  public static LocationPresetStore getLocationPresetStore() {
    return locationPresetStore;
  }

  public static class InvalidTagException extends IllegalArgumentException {

    public InvalidTagException(String message, Throwable cause) {
      super(message, cause);
    }

  }

}
