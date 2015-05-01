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

import org.dungeon.achievements.Achievement;
import org.dungeon.achievements.AchievementBuilder;
import org.dungeon.creatures.Creature;
import org.dungeon.io.DLogger;
import org.dungeon.io.ResourceReader;
import org.dungeon.items.ItemBlueprint;
import org.dungeon.skill.SkillDefinition;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.CounterMap;
import org.dungeon.util.StopWatch;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The class that stores all the game data that is loaded and not serialized.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public final class GameData {
  public static final Font FONT = getMonospacedFont();
  private static final PoetryLibrary poetryLibrary = new PoetryLibrary();
  private static final DreamLibrary dreamLibrary = new DreamLibrary();
  private static final HintLibrary hintLibrary = new HintLibrary();
  private static String tutorial = null;
  public static HashMap<ID, Achievement> ACHIEVEMENTS;
  public static String LICENSE;
  private static Map<ID, Creature> creatures = new HashMap<ID, Creature>();
  private static Map<ID, ItemBlueprint> itemBlueprints = new HashMap<ID, ItemBlueprint>();
  private static Map<ID, SkillDefinition> skillDefinitions = new HashMap<ID, SkillDefinition>();
  private static Map<ID, LocationPreset> locationPresets = new HashMap<ID, LocationPreset>();

  private GameData() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Returns the monospaced font used by the game interface.
   */
  private static Font getMonospacedFont() {
    final int FONT_SIZE = 15;
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
    try {
      InputStream fontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("DroidSansMono.ttf");
      font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, FONT_SIZE);
    } catch (FontFormatException bad) {
      DLogger.warning(bad.getMessage());
    } catch (IOException bad) {
      DLogger.warning(bad.getMessage());
    }
    return font;
  }

  public static PoetryLibrary getPoetryLibrary() {
    return poetryLibrary;
  }

  public static DreamLibrary getDreamLibrary() {
    return dreamLibrary;
  }

  public static HintLibrary getHintLibrary() {
    return hintLibrary;
  }

  public static String getTutorial() {
    if (tutorial == null) {
      loadTutorial();
    }
    return tutorial;
  }

  static void loadGameData() {
    StopWatch stopWatch = new StopWatch();
    DLogger.info("Started loading the game data.");
    loadItemBlueprints();
    loadCreatureBlueprints();
    createSkills();
    loadLocationPresets();
    loadAchievements();
    loadLicense();
    DLogger.info("Finished loading the game data. Took " + stopWatch.toString() + ".");
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
   * Loads all ItemBlueprints to a HashMap.
   */
  private static void loadItemBlueprints() {
    ResourceReader reader = new ResourceReader("items.txt");
    while (reader.readNextElement()) {
      ItemBlueprint blueprint = new ItemBlueprint();
      blueprint.setID(new ID(reader.getValue("ID")));
      blueprint.setType(reader.getValue("TYPE"));
      blueprint.setName(nameFromArray(reader.getArrayOfValues("NAME")));
      blueprint.setCurIntegrity(readIntegerFromResourceReader(reader, "CUR_INTEGRITY"));
      blueprint.setMaxIntegrity(readIntegerFromResourceReader(reader, "MAX_INTEGRITY"));
      blueprint.setWeight(Weight.newInstance(readDoubleFromResourceReader(reader, "WEIGHT")));
      blueprint.setRepairable(readIntegerFromResourceReader(reader, "REPAIRABLE") == 1);
      blueprint.setWeapon(readIntegerFromResourceReader(reader, "WEAPON") == 1);
      blueprint.setDamage(readIntegerFromResourceReader(reader, "DAMAGE"));
      blueprint.setHitRate(readDoubleFromResourceReader(reader, "HIT_RATE"));
      blueprint.setIntegrityDecrementOnHit(readIntegerFromResourceReader(reader, "INTEGRITY_DECREMENT_ON_HIT"));
      blueprint.setFood(readIntegerFromResourceReader(reader, "FOOD") == 1);
      if (reader.hasValue("NUTRITION")) {
        blueprint.setNutrition(readIntegerFromResourceReader(reader, "NUTRITION"));
      }
      if (reader.hasValue("INTEGRITY_DECREMENT_ON_EAT")) {
        blueprint.setIntegrityDecrementOnEat(readIntegerFromResourceReader(reader, "INTEGRITY_DECREMENT_ON_EAT"));
      }
      blueprint.setClock(readIntegerFromResourceReader(reader, "CLOCK") == 1);
      blueprint.setBook(readIntegerFromResourceReader(reader, "BOOK") == 1);
      if (reader.hasValue("SKILL")) {
        blueprint.setSkill(reader.getValue("SKILL"));
      }
      itemBlueprints.put(blueprint.getID(), blueprint);
    }
    reader.close();
    itemBlueprints = Collections.unmodifiableMap(itemBlueprints);
    DLogger.info("Loaded " + itemBlueprints.size() + " item blueprints.");
  }

  /**
   * Loads all CreatureBlueprints to a HashMap.
   */
  private static void loadCreatureBlueprints() {
    ResourceReader resourceReader = new ResourceReader("creatures.txt");
    while (resourceReader.readNextElement()) {
      ID id = new ID(resourceReader.getValue("ID"));
      String type = resourceReader.getValue("TYPE");
      Name name = nameFromArray(resourceReader.getArrayOfValues("NAME"));
      int health = readIntegerFromResourceReader(resourceReader, "HEALTH");
      int attack = readIntegerFromResourceReader(resourceReader, "ATTACK");
      String attackAlgorithmID = resourceReader.getValue("ATTACK_ALGORITHM_ID");
      creatures.put(id, new Creature(id, type, name, health, attack, attackAlgorithmID));
    }
    resourceReader.close();
    creatures = Collections.unmodifiableMap(creatures);
    DLogger.info("Loaded " + creatures.size() + " creatures.");
  }

  private static void loadLocationPresets() {
    ResourceReader resourceReader = new ResourceReader("locations.txt");
    while (resourceReader.readNextElement()) {
      ID id = new ID(resourceReader.getValue("ID"));
      String type = resourceReader.getValue("TYPE");
      Name name = nameFromArray(resourceReader.getArrayOfValues("NAME"));
      LocationPreset preset = new LocationPreset(id, type, name);
      preset.setBlobSize(readIntegerFromResourceReader(resourceReader, "BLOB_SIZE"));
      preset.setLightPermittivity(readDoubleFromResourceReader(resourceReader, "LIGHT_PERMITTIVITY"));
      // Spawners.
      if (resourceReader.hasValue("SPAWNERS")) {
        for (String dungeonList : resourceReader.getArrayOfValues("SPAWNERS")) {
          String[] spawner = ResourceReader.toArray(dungeonList);
          String spawnerID = spawner[0];
          int population = Integer.parseInt(spawner[1]);
          int delay = Integer.parseInt(spawner[2]);
          preset.addSpawner(new SpawnerPreset(spawnerID, population, delay));
        }
      }
      // Items.
      if (resourceReader.hasValue("ITEMS")) {
        for (String dungeonList : resourceReader.getArrayOfValues("ITEMS")) {
          String[] item = ResourceReader.toArray(dungeonList);
          String itemID = item[0];
          double frequency = Double.parseDouble(item[1]);
          preset.addItem(itemID, frequency);
        }
      }
      // Blocked Entrances.
      if (resourceReader.hasValue("BLOCKED_ENTRANCES")) {
        for (String dungeonList : resourceReader.getArrayOfValues("BLOCKED_ENTRANCES")) {
          String[] entrances = ResourceReader.toArray(dungeonList);
          for (String entrance : entrances) {
            preset.block(Direction.fromAbbreviation(entrance));
          }
        }
      }
      locationPresets.put(preset.getID(), preset);
    }
    resourceReader.close();
    locationPresets = Collections.unmodifiableMap(locationPresets);
    DLogger.info("Loaded " + locationPresets.size() + " location presets.");
  }

  private static void loadAchievements() {
    ACHIEVEMENTS = new HashMap<ID, Achievement>();
    ResourceReader reader = new ResourceReader("achievements.txt");
    while (reader.readNextElement()) {
      AchievementBuilder builder = new AchievementBuilder();
      builder.setId(reader.getValue("ID"));
      builder.setName(reader.getValue("NAME"));
      builder.setInfo(reader.getValue("INFO"));
      builder.setText(reader.getValue("TEXT"));

      builder.setMinimumBattleCount(readIntegerFromResourceReader(reader, "MINIMUM_BATTLE_COUNT"));
      builder.setLongestBattleLength(readIntegerFromResourceReader(reader, "LONGEST_BATTLE_LENGTH"));

      CounterMap<ID> killsByCreatureId = readIDCounterMap(reader, "KILLS_BY_CREATURE_ID");
      builder.setKillsByCreatureID(killsByCreatureId);

      CounterMap<String> killsByCreatureType = readStringCounterMap(reader, "KILLS_BY_CREATURE_TYPE");
      builder.setKillsByCreatureType(killsByCreatureType);

      CounterMap<CauseOfDeath> killsByCauseOfDeath = new CounterMap<CauseOfDeath>();
      if (reader.hasValue("KILLS_BY_CAUSE_OF_DEATH")) {
        String[] arrayOfCausesOfDeath = reader.getArrayOfValues("KILLS_BY_CAUSE_OF_DEATH");
        for (String dungeonList : arrayOfCausesOfDeath) {
          String[] elements = ResourceReader.toArray(dungeonList);
          TypeOfCauseOfDeath typeOfCauseOfDeath = TypeOfCauseOfDeath.valueOf(elements[0]);
          ID id = new ID(elements[1]);
          int amount = Integer.parseInt(elements[2]);
          killsByCauseOfDeath.incrementCounter(new CauseOfDeath(typeOfCauseOfDeath, id), amount);
        }
        builder.setKillsByCauseOfDeath(killsByCauseOfDeath);
      }

      CounterMap<ID> killsByLocationId = readIDCounterMap(reader, "KILLS_BY_LOCATION_ID");
      builder.setKillsByLocationID(killsByLocationId);

      CounterMap<ID> visitedLocations = readIDCounterMap(reader, "VISITED_LOCATIONS");
      builder.setVisitedLocations(visitedLocations);

      CounterMap<ID> maximumNumberOfVisits = readIDCounterMap(reader, "MAXIMUM_NUMBER_OF_VISITS");
      builder.setMaximumNumberOfVisits(maximumNumberOfVisits);

      Achievement achievement = builder.createAchievement();
      ACHIEVEMENTS.put(achievement.getID(), achievement);
    }
    reader.close();
    DLogger.info("Loaded " + ACHIEVEMENTS.size() + " achievements.");
  }

  /**
   * Attempts to read a double from a ResourceReader given a key.
   *
   * @param reader a ResourceReader
   * @param key    the String key
   * @return the double, if it could be obtained, or 0
   */
  private static double readDoubleFromResourceReader(ResourceReader reader, String key) {
    if (reader.hasValue(key)) {
      try {
        return Double.parseDouble(reader.getValue(key));
      } catch (NumberFormatException log) {
        DLogger.warning("Could not parse the value of " + key + ".");
      }
    }
    return 0.0;
  }

  /**
   * Attempts to read an integer from a ResourceReader given a key.
   *
   * @param reader a ResourceReader
   * @param key    the String key
   * @return the integer, if it could be obtained, or 0
   */
  private static int readIntegerFromResourceReader(ResourceReader reader, String key) {
    if (reader.hasValue(key)) {
      try {
        return Integer.parseInt(reader.getValue(key));
      } catch (NumberFormatException log) {
        DLogger.warning("Could not parse the value of " + key + ".");
      }
    }
    return 0;
  }

  /**
   * Reads a CounterMap from a ResourceReader based on the given key.
   *
   * @param reader the ResourceReader
   * @param key    the String key
   * @return a CounterMap of Strings
   */
  private static CounterMap<String> readStringCounterMap(ResourceReader reader, String key) {
    CounterMap<String> counterMap = new CounterMap<String>();
    if (reader.hasValue(key)) {
      try {
        String[] values = reader.getArrayOfValues(key);
        for (String dungeonList : values) {
          String[] parts = ResourceReader.toArray(dungeonList);
          counterMap.incrementCounter(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
      } catch (NumberFormatException log) {
        DLogger.warning("Could not parse the value of " + key + ".");
      }
    }
    return counterMap;
  }

  /**
   * Converts a {@code CounterMap<String>} to a {@code CounterMap<ID>}.
   *
   * @param stringCounterMap a {@code CounterMap<String>}
   * @return a CounterMap of IDs
   */
  private static CounterMap<ID> toIDCounterMap(CounterMap<String> stringCounterMap) {
    CounterMap<ID> idCounterMap = new CounterMap<ID>();
    for (String key : stringCounterMap.keySet()) {
      idCounterMap.incrementCounter(new ID(key), stringCounterMap.getCounter(key));
    }
    return idCounterMap;
  }

  /**
   * Reads a CounterMap from a ResourceReader based on the given key.
   *
   * @param reader the ResourceReader
   * @param key    the String key
   * @return a CounterMap of IDs
   */
  private static CounterMap<ID> readIDCounterMap(ResourceReader reader, String key) {
    return toIDCounterMap(readStringCounterMap(reader, key));
  }

  /**
   * Convenience method that creates a Name from an array of Strings.
   *
   * @param strings the array of Strings
   * @return a Name
   */
  private static Name nameFromArray(String[] strings) {
    if (strings.length == 1) {
      return Name.newInstance(strings[0]);
    } else if (strings.length > 1) {
      return Name.newInstance(strings[0], strings[1]);
    } else {
      DLogger.warning("Empty array used to create a Name! Using \"ERROR\".");
      return Name.newInstance("ERROR");
    }
  }

  private static void loadLicense() {
    ResourceReader reader = new ResourceReader("license.txt");
    reader.readNextElement();
    LICENSE = reader.getValue("LICENSE");
    reader.close();
  }

  private static void loadTutorial() {
    if (tutorial != null) { // Should only be called once.
      throw new AssertionError();
    }
    ResourceReader reader = new ResourceReader("tutorial.txt");
    reader.readNextElement();
    tutorial = reader.getValue("TUTORIAL");
    reader.close();
  }

  public static Map<ID, Creature> getCreatureModels() {
    return creatures;
  }

  public static Map<ID, ItemBlueprint> getItemBlueprints() {
    return itemBlueprints;
  }

  public static Map<ID, SkillDefinition> getSkillDefinitions() {
    return skillDefinitions;
  }

  public static Map<ID, LocationPreset> getLocationPresets() {
    return locationPresets;
  }

}
