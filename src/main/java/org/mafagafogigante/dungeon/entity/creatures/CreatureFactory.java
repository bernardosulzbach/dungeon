/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.achievements.AchievementTracker;
import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.Visibility;
import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.entity.items.ItemFactory;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.stats.Statistics;
import org.mafagafogigante.dungeon.util.Percentage;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The factory of creatures.
 */
public final class CreatureFactory implements Serializable {

  private static final int DEFAULT_INVENTORY_ITEM_LIMIT = 100;
  private static final double DEFAULT_INVENTORY_WEIGHT_LIMIT = 100.0;

  private final World world;
  private final Map<Id, CreaturePreset> creaturePresetMap;

  /**
   * Creates a CreatureFactory for the specified world.
   */
  public CreatureFactory(World world) {
    this.world = world;
    this.creaturePresetMap = loadCreaturePresetMapFromJson();
  }

  /**
   * Creates a CreatureFactory from the specified presets.
   */
  public CreatureFactory(Collection<CreaturePreset> presets) {
    this.world = null;
    this.creaturePresetMap = new HashMap<>();
    for (CreaturePreset preset : presets) {
      creaturePresetMap.put(preset.getId(), preset);
    }
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
      preset.setVisibility(new Visibility(visibilityPercentage));
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

  private static void equipWeapon(Creature creature, CreaturePreset preset) {
    if (preset.getWeaponId() != null) {
      // Get the weapon from the creature's inventory.
      for (Item item : creature.getInventory().getItems()) {
        if (item.getId().equals(preset.getWeaponId())) {
          creature.setWeapon(item);
          break;
        }
      }
      if (!creature.hasWeapon()) { // Did not found a suitable Item in the inventory.
        String format = "%s not found in the inventory of %s!";
        DungeonLogger.warning(String.format(format, preset.getWeaponId(), creature.getId()));
      }
    }
  }

  /**
   * Gives a Creature all the Items defined in the corresponding CreaturePreset and equips its weapon, if there is one.
   *
   * @param creature the Creature
   */
  private void giveItems(Creature creature) {
    giveItems(creature, world.getWorldDate());
  }

  /**
   * Gives a Creature all the Items defined in the corresponding CreaturePreset and equips its weapon, if there is one.
   *
   * @param creature the Creature
   * @param date the Date when the Items this Creature has were created
   */
  private void giveItems(Creature creature, Date date) {
    CreaturePreset preset = creaturePresetMap.get(creature.getId());
    ItemFactory itemFactory = world.getItemFactory();
    for (Id itemId : preset.getItems()) {
      if (itemFactory.canMakeItem(itemId)) {
        Item item = itemFactory.makeItem(itemId, date);
        SimulationResult result = creature.getInventory().simulateItemAddition(item);
        if (result == SimulationResult.SUCCESSFUL) {
          creature.getInventory().addItem(item);
        } else {
          DungeonLogger.warning("Could not add " + itemId + " to " + creature.getId() + ". Reason: " + result + ".");
        }
      }
    }
    equipWeapon(creature, preset);
  }

  /**
   * Loads all creature presets from the resource file.
   */
  private Map<Id, CreaturePreset> loadCreaturePresetMapFromJson() {
    Map<Id, CreaturePreset> creaturePresetMap = new HashMap<>();
    JsonObject object = JsonObjectFactory.makeJsonObject("creatures.json");
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
      creaturePresetMap.put(preset.getId(), preset);
    }
    DungeonLogger.info("Loaded " + creaturePresetMap.size() + " creature presets.");
    return creaturePresetMap;
  }

  public Collection<CreaturePreset> getPresets() {
    return creaturePresetMap.values();
  }

  /**
   * Attempts to create a creature from the CreaturePreset specified by an ID. Returns null if no preset was found.
   *
   * <p>Also adds the new creature to the statistics.
   */
  public Creature makeCreature(Id id) {
    CreaturePreset preset = creaturePresetMap.get(id);
    if (preset != null) {
      Creature creature = new Creature(preset);
      Game.getGameState().getStatistics().getWorldStatistics().addSpawn(creature.getName().getSingular());
      giveItems(creature);
      return creature;
    } else {
      return null;
    }
  }

  /**
   * Creates the Hero.
   *
   * @param date the Date when the Items the Hero has were created
   * @return the Hero object
   */
  public Hero makeHero(Date date, Statistics statistics) {
    DateOfBirthGenerator dateOfBirthGenerator = new DateOfBirthGenerator(date, 30);
    Date dateOfBirth = dateOfBirthGenerator.generateDateOfBirth();
    Hero hero = new Hero(creaturePresetMap.get(new Id("HERO")), new AchievementTracker(statistics), dateOfBirth);
    giveItems(hero, date);
    return hero;
  }

}
