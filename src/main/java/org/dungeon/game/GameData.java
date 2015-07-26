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
import org.dungeon.date.DungeonTimeParser;
import org.dungeon.entity.Integrity;
import org.dungeon.entity.Weight;
import org.dungeon.entity.creatures.CreatureFactory;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemPreset;
import org.dungeon.io.DungeonLogger;
import org.dungeon.io.JsonObjectFactory;
import org.dungeon.io.ResourceReader;
import org.dungeon.skill.SkillDefinition;
import org.dungeon.util.Percentage;
import org.dungeon.util.StopWatch;

import com.eclipsesource.json.JsonObject;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The class that stores all the game data that is loaded and not serialized.
 */
public final class GameData {

  private static final LocationPresetStore locationPresetStore = new LocationPresetStore();
  public static String LICENSE;
  private static String tutorial = null;
  private static Map<Id, ItemPreset> itemPresets = new HashMap<Id, ItemPreset>();
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

  static void loadGameData() {
    StopWatch stopWatch = new StopWatch();
    DungeonLogger.info("Started loading the game data.");
    loadItemPresets();
    CreatureFactory.loadCreaturePresets(itemPresets);
    GameData.itemPresets = Collections.unmodifiableMap(GameData.itemPresets);
    createSkills();
    loadLocationPresets();
    AchievementStore.initialize();
    loadLicense();
    DungeonLogger.info("Finished loading the game data. Took " + stopWatch.toString() + ".");
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

  /**
   * Loads all item presets that are not programmatically generated.
   */
  private static void loadItemPresets() {
    ResourceReader reader = new ResourceReader("items.txt");
    while (reader.readNextElement()) {
      ItemPreset preset = new ItemPreset();
      preset.setId(new Id(reader.getValue("ID")));
      preset.setType(reader.getValue("TYPE"));
      preset.setName(nameFromArray(reader.getArrayOfValues("NAME")));
      for (Item.Tag tag : tagSetFromArray(Item.Tag.class, reader.getArrayOfValues("TAGS"))) {
        preset.addTag(tag);
      }
      if (preset.hasTag(Item.Tag.BOOK)) {
        preset.setText(reader.getValue("TEXT"));
      }
      if (reader.hasValue("DECOMPOSITION_PERIOD")) {
        long seconds = DungeonTimeParser.parsePeriod(reader.getValue("DECOMPOSITION_PERIOD")).getSeconds();
        preset.setPutrefactionPeriod(seconds);
      }
      int curIntegrity = readIntegerFromResourceReader(reader, "CUR_INTEGRITY");
      int maxIntegrity = readIntegerFromResourceReader(reader, "MAX_INTEGRITY");
      preset.setIntegrity(new Integrity(curIntegrity, maxIntegrity));
      preset.setVisibility(reader.readVisibility());
      if (reader.hasValue("LUMINOSITY")) {
        preset.setLuminosity(reader.readLuminosity());
      }
      preset.setWeight(Weight.newInstance(readDoubleFromResourceReader(reader, "WEIGHT")));
      preset.setDamage(readIntegerFromResourceReader(reader, "DAMAGE"));
      preset.setHitRate(new Percentage(readDoubleFromResourceReader(reader, "HIT_RATE")));
      preset.setIntegrityDecrementOnHit(readIntegerFromResourceReader(reader, "INTEGRITY_DECREMENT_ON_HIT"));
      if (reader.hasValue("NUTRITION")) {
        preset.setNutrition(readIntegerFromResourceReader(reader, "NUTRITION"));
      }
      if (reader.hasValue("INTEGRITY_DECREMENT_ON_EAT")) {
        preset.setIntegrityDecrementOnEat(readIntegerFromResourceReader(reader, "INTEGRITY_DECREMENT_ON_EAT"));
      }
      if (reader.hasValue("SKILL")) {
        preset.setSkill(reader.getValue("SKILL"));
      }
      itemPresets.put(preset.getId(), preset);
    }
    reader.close();
    DungeonLogger.info("Loaded " + itemPresets.size() + " item presets.");
  }

  private static void loadLocationPresets() {
    ResourceReader reader = new ResourceReader("locations.txt");
    while (reader.readNextElement()) {
      Id id = new Id(reader.getValue("ID"));
      LocationPreset.Type type = LocationPreset.Type.valueOf(reader.getValue("TYPE"));
      Name name = nameFromArray(reader.getArrayOfValues("NAME"));
      LocationPreset preset = new LocationPreset(id, type, name);
      preset.setDescription(new LocationDescription(reader.readCharacter("SYMBOL"), reader.readColor()));
      if (reader.hasValue("INFO")) {
        preset.getDescription().setInfo(reader.getValue("INFO"));
      }
      preset.setBlobSize(readIntegerFromResourceReader(reader, "BLOB_SIZE"));
      preset.setLightPermittivity(readDoubleFromResourceReader(reader, "LIGHT_PERMITTIVITY"));
      // Spawners.
      if (reader.hasValue("SPAWNERS")) {
        for (String dungeonList : reader.getArrayOfValues("SPAWNERS")) {
          String[] spawner = ResourceReader.toArray(dungeonList);
          String spawnerId = spawner[0];
          int population = Integer.parseInt(spawner[1]);
          int delay = Integer.parseInt(spawner[2]);
          preset.addSpawner(new SpawnerPreset(spawnerId, population, delay));
        }
      }
      // Items.
      if (reader.hasValue("ITEMS")) {
        for (String dungeonList : reader.getArrayOfValues("ITEMS")) {
          String[] item = ResourceReader.toArray(dungeonList);
          String itemId = item[0];
          double frequency = Double.parseDouble(item[1]);
          preset.addItem(itemId, frequency);
        }
      }
      // Blocked Entrances.
      if (reader.hasValue("BLOCKED_ENTRANCES")) {
        for (String dungeonList : reader.getArrayOfValues("BLOCKED_ENTRANCES")) {
          String[] entrances = ResourceReader.toArray(dungeonList);
          for (String entrance : entrances) {
            preset.block(Direction.fromAbbreviation(entrance));
          }
        }
      }
      locationPresetStore.addLocationPreset(preset);
    }
    reader.close();
    DungeonLogger.info("Loaded " + locationPresetStore.getSize() + " location presets.");
  }

  /**
   * Attempts to read a double from a ResourceReader given a key.
   *
   * @param reader a ResourceReader
   * @param key the String key
   * @return the double, if it could be obtained, or 0
   */
  private static double readDoubleFromResourceReader(ResourceReader reader, String key) {
    if (reader.hasValue(key)) {
      try {
        return Double.parseDouble(reader.getValue(key));
      } catch (NumberFormatException log) {
        DungeonLogger.warning("Could not parse the value of " + key + ".");
      }
    }
    return 0.0;
  }

  /**
   * Attempts to read an integer from a ResourceReader given a key.
   *
   * @param reader a ResourceReader
   * @param key the String key
   * @return the integer, if it could be obtained, or 0
   */
  private static int readIntegerFromResourceReader(ResourceReader reader, String key) {
    if (reader.hasValue(key)) {
      try {
        return Integer.parseInt(reader.getValue(key));
      } catch (NumberFormatException log) {
        DungeonLogger.warning("Could not parse the value of " + key + ".");
      }
    }
    return 0;
  }

  /**
   * Convenience method that creates a Name from an array of Strings.
   *
   * @param strings the array of Strings
   * @return a Name
   */
  private static Name nameFromArray(String[] strings) {
    if (strings.length == 1) {
      return NameFactory.newInstance(strings[0]);
    } else if (strings.length > 1) {
      return NameFactory.newInstance(strings[0], strings[1]);
    } else {
      DungeonLogger.warning("Empty array used to create a Name! Using \"ERROR\".");
      return NameFactory.newInstance("ERROR");
    }
  }

  /**
   * Creates a Set of tags from an array of Strings.
   *
   * @param enumClass the Class of the enum
   * @param strings the array of Strings
   * @param <E> an Enum type
   * @return a Set of Item.Tag
   */
  private static <E extends Enum<E>> Set<E> tagSetFromArray(Class<E> enumClass, String[] strings) {
    Set<E> set = EnumSet.noneOf(enumClass);
    for (String tag : strings) {
      try {
        set.add(Enum.valueOf(enumClass, tag));
      } catch (IllegalArgumentException fatal) {
        // Guarantee that bugged resource files are not going to make it to a release.
        String message = "invalid tag '" + tag + "' found.";
        throw new InvalidTagException(message, fatal);
      }
    }
    return set;
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

  public static Map<Id, ItemPreset> getItemPresets() {
    return itemPresets;
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
