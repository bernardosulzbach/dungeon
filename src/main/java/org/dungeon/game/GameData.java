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
import org.dungeon.creatures.CreatureBlueprint;
import org.dungeon.io.DLogger;
import org.dungeon.io.ResourceReader;
import org.dungeon.items.ItemBlueprint;
import org.dungeon.skill.SkillDefinition;
import org.dungeon.util.StopWatch;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The class that stores all the game data that is loaded and not serialized.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public final class GameData {

  private static final PoetryLibrary poetryLibrary = new PoetryLibrary();
  private static final HintLibrary hintLibrary = new HintLibrary();

  public static HashMap<ID, Achievement> ACHIEVEMENTS;
  public static Font monospaced;
  public static String LICENSE;
  private static Map<ID, CreatureBlueprint> creatureBlueprints = new HashMap<ID, CreatureBlueprint>();
  private static Map<ID, ItemBlueprint> itemBlueprints = new HashMap<ID, ItemBlueprint>();
  private static Map<ID, SkillDefinition> skillDefinitions = new HashMap<ID, SkillDefinition>();
  private static Map<ID, LocationPreset> locationPresets = new HashMap<ID, LocationPreset>();
  private static ClassLoader loader;

  public static PoetryLibrary getPoetryLibrary() {
    return poetryLibrary;
  }

  public static HintLibrary getHintLibrary() {
    return hintLibrary;
  }

  static void loadGameData() {
    StopWatch stopWatch = new StopWatch();
    DLogger.info("Started loading the game data.");

    loader = Thread.currentThread().getContextClassLoader();

    monospaced = new Font("Monospaced", Font.PLAIN, 14);

    loadItemBlueprints();
    loadCreatureBlueprints();
    createSkills();
    loadLocationPresets();

    createAchievements();

    loadLicense();

    DLogger.info("Finished loading the game data. Took " + stopWatch.toString() + ".");
  }

  /**
   * Creates all the Skills (hardcoded).
   */
  private static void createSkills() {
    SkillDefinition fireball = new SkillDefinition("FIREBALL", "Skill", "Fireball", 10, 6);
    skillDefinitions.put(fireball.getID(), fireball);

    SkillDefinition burningGround = new SkillDefinition("BURNING_GROUND", "Skill", "Burning Ground", 18, 12);
    skillDefinitions.put(burningGround.getID(), burningGround);
    skillDefinitions = Collections.unmodifiableMap(skillDefinitions);
  }

  /**
   * Loads all ItemBlueprints to a HashMap.
   */
  private static void loadItemBlueprints() {
    ResourceReader resourceReader = new ResourceReader(loader.getResourceAsStream("items.txt"), "items.txt");
    while (resourceReader.readNextElement()) {
      ItemBlueprint blueprint = new ItemBlueprint();
      blueprint.setID(new ID(resourceReader.getValue("ID")));
      blueprint.setType(resourceReader.getValue("TYPE"));
      blueprint.setName(resourceReader.getValue("NAME"));
      blueprint.setCurIntegrity(Integer.parseInt(resourceReader.getValue("CUR_INTEGRITY")));
      blueprint.setMaxIntegrity(Integer.parseInt(resourceReader.getValue("MAX_INTEGRITY")));
      blueprint.setRepairable(Integer.parseInt(resourceReader.getValue("REPAIRABLE")) == 1);
      blueprint.setWeapon(Integer.parseInt(resourceReader.getValue("WEAPON")) == 1);
      blueprint.setDamage(Integer.parseInt(resourceReader.getValue("DAMAGE")));
      blueprint.setHitRate(Double.parseDouble(resourceReader.getValue("HIT_RATE")));
      blueprint.setIntegrityDecrementOnHit(Integer.parseInt(resourceReader.getValue("INTEGRITY_DECREMENT_ON_HIT")));
      blueprint.setFood(Integer.parseInt(resourceReader.getValue("FOOD")) == 1);
      if (resourceReader.hasValue("NUTRITION")) {
        blueprint.setNutrition(Integer.parseInt(resourceReader.getValue("NUTRITION")));
      }
      if (resourceReader.hasValue("INTEGRITY_DECREMENT_ON_EAT")) {
        blueprint.setIntegrityDecrementOnEat(Integer.parseInt(resourceReader.getValue("INTEGRITY_DECREMENT_ON_EAT")));
      }
      blueprint.setClock(Integer.parseInt(resourceReader.getValue("CLOCK")) == 1);
      blueprint.setBook(Integer.parseInt(resourceReader.getValue("BOOK")) == 1);
      if (resourceReader.hasValue("SKILL")) {
        blueprint.setSkill(resourceReader.getValue("SKILL"));
      }
      itemBlueprints.put(blueprint.getID(), blueprint);
    }
    resourceReader.close();
    itemBlueprints = Collections.unmodifiableMap(itemBlueprints);
    DLogger.info("Loaded " + itemBlueprints.size() + " item blueprints.");
  }

  /**
   * Loads all CreatureBlueprints to a HashMap.
   */
  private static void loadCreatureBlueprints() {
    ResourceReader resourceReader = new ResourceReader(loader.getResourceAsStream("creatures.txt"), "creatures.txt");
    while (resourceReader.readNextElement()) {
      CreatureBlueprint blueprint = new CreatureBlueprint();
      blueprint.setID(new ID(resourceReader.getValue("ID")));
      blueprint.setType(resourceReader.getValue("TYPE"));
      blueprint.setName(resourceReader.getValue("NAME"));
      blueprint.setCurHealth(Integer.parseInt(resourceReader.getValue("CUR_HEALTH")));
      blueprint.setMaxHealth(Integer.parseInt(resourceReader.getValue("MAX_HEALTH")));
      blueprint.setAttack(Integer.parseInt(resourceReader.getValue("ATTACK")));
      blueprint.setAttackAlgorithmID(resourceReader.getValue("ATTACK_ALGORITHM_ID"));
      creatureBlueprints.put(blueprint.getID(), blueprint);
    }
    resourceReader.close();
    creatureBlueprints = Collections.unmodifiableMap(creatureBlueprints);
    DLogger.info("Loaded " + creatureBlueprints.size() + " creature blueprints.");
  }

  private static void loadLocationPresets() {
    SpawnerPreset bat = new SpawnerPreset("BAT", 1, 6);
    SpawnerPreset bear = new SpawnerPreset("BEAR", 1, 12);
    SpawnerPreset boar = new SpawnerPreset("BOAR", 3, 8);
    SpawnerPreset crocodile = new SpawnerPreset("CROCODILE", 1, 6);
    SpawnerPreset fox = new SpawnerPreset("FOX", 4, 4);
    SpawnerPreset frog = new SpawnerPreset("FROG", 2, 2);
    SpawnerPreset komodoDragon = new SpawnerPreset("KOMODO_DRAGON", 1, 4);
    SpawnerPreset orc = new SpawnerPreset("ORC", 2, 2);
    SpawnerPreset rabbit = new SpawnerPreset("RABBIT", 8, 1);
    SpawnerPreset rat = new SpawnerPreset("RAT", 6, 2);
    SpawnerPreset skeleton = new SpawnerPreset("SKELETON", 1, 12);
    SpawnerPreset snake = new SpawnerPreset("SNAKE", 3, 4);
    SpawnerPreset spider = new SpawnerPreset("SPIDER", 2, 4);
    SpawnerPreset whiteTiger = new SpawnerPreset("WHITE_TIGER", 1, 24);
    SpawnerPreset wolf = new SpawnerPreset("WOLF", 3, 12);
    SpawnerPreset zombie = new SpawnerPreset("ZOMBIE", 2, 4);

    LocationPreset clearing = new LocationPreset("CLEARING", "Land", "Clearing");
    clearing.addSpawner(frog).addSpawner(rabbit).addSpawner(spider).addSpawner(fox);
    clearing.addItem("CHERRY", 0.6).addItem("STICK", 0.9);
    clearing.setLightPermittivity(1.0);
    clearing.finish();
    locationPresets.put(clearing.getID(), clearing);

    LocationPreset desert = new LocationPreset("DESERT", "Land", "Desert");
    desert.addSpawner(rat).addSpawner(snake).addSpawner(zombie).addSpawner(boar);
    desert.addItem("MACE", 0.1).addItem("STAFF", 0.2).addItem("DAGGER", 0.15).addItem("SPEAR", 0.1);
    desert.setLightPermittivity(1.0);
    desert.finish();
    locationPresets.put(desert.getID(), desert);

    LocationPreset forest = new LocationPreset("FOREST", "Land", "Forest");
    forest.addSpawner(bear).addSpawner(frog).addSpawner(rabbit).addSpawner(whiteTiger).addSpawner(zombie);
    forest.addItem("AXE", 0.2).addItem("POCKET_WATCH", 0.03).addItem("STICK", 0.5).addItem("TOME_OF_FIREBALL", 0.1);
    forest.setLightPermittivity(0.7);
    forest.finish();
    locationPresets.put(forest.getID(), forest);

    LocationPreset graveyard = new LocationPreset("GRAVEYARD", "Land", "Graveyard");
    graveyard.addSpawner(bat).addSpawner(skeleton).addSpawner(zombie).addSpawner(orc);
    graveyard.addItem("LONGSWORD", 0.15).addItem("WRIST_WATCH", 0.025).addItem("TOME_OF_BURNING_GROUND", 0.1);
    graveyard.setLightPermittivity(0.9);
    graveyard.finish();
    locationPresets.put(graveyard.getID(), graveyard);

    LocationPreset meadow = new LocationPreset("MEADOW", "Land", "Meadow");
    meadow.addSpawner(whiteTiger).addSpawner(wolf);
    meadow.addItem("STONE", 0.8).addItem("WATERMELON", 0.4).addItem("APPLE", 0.7);
    meadow.setLightPermittivity(1.0);
    meadow.finish();
    locationPresets.put(meadow.getID(), meadow);

    LocationPreset pond = new LocationPreset("POND", "Land", "Pond");
    pond.addSpawner(frog).addSpawner(komodoDragon).addSpawner(crocodile);
    pond.addItem("WATERMELON", 0.8).addItem("SPEAR", 0.3);
    pond.setLightPermittivity(0.96);
    pond.finish();
    locationPresets.put(pond.getID(), pond);

    LocationPreset swamp = new LocationPreset("SWAMP", "Land", "Swamp");
    swamp.addSpawner(frog).addSpawner(snake).addSpawner(komodoDragon).addSpawner(crocodile);
    swamp.addItem("STICK", 0.9).addItem("WATERMELON", 0.12);
    swamp.setLightPermittivity(0.7);
    swamp.finish();
    locationPresets.put(swamp.getID(), swamp);

    LocationPreset wasteland = new LocationPreset("WASTELAND", "Land", "Wasteland");
    wasteland.addSpawner(rat).addSpawner(spider).addSpawner(snake);
    wasteland.addItem("STONE", 0.3).addItem("STICK", 0.18);
    wasteland.setLightPermittivity(1.0);
    wasteland.finish();
    locationPresets.put(wasteland.getID(), wasteland);

    LocationPreset savannah = new LocationPreset("SAVANNAH", "Land", "Savannah");
    savannah.addSpawner(boar).addSpawner(snake).addSpawner(whiteTiger);
    savannah.addItem("APPLE", 0.8).addItem("AXE", 0.3);
    savannah.setLightPermittivity(1.0);
    savannah.finish();
    locationPresets.put(savannah.getID(), savannah);

    LocationPreset river = new LocationPreset("RIVER", "River", "River");
    river.block(Direction.WEST).block(Direction.EAST);
    river.setLightPermittivity(1.0);
    river.finish();
    locationPresets.put(river.getID(), river);

    LocationPreset bridge = new LocationPreset("BRIDGE", "Bridge", "Bridge");
    bridge.addItem("TOME_OF_FIREBALL", 0.15);
    bridge.block(Direction.NORTH).block(Direction.SOUTH);
    bridge.setLightPermittivity(1.0);
    bridge.finish();
    locationPresets.put(bridge.getID(), bridge);

    locationPresets = Collections.unmodifiableMap(locationPresets);

    DLogger.info("Created " + locationPresets.size() + " location presets.");
  }

  /**
   * Creates all Achievements.
   */
  private static void createAchievements() {
    // TODO: load this from a resource file.
    ACHIEVEMENTS = new HashMap<ID, Achievement>();

    ResourceReader reader = new ResourceReader(loader.getResourceAsStream("achievements.txt"), "achievements.txt");

    while (reader.readNextElement()) {
      String id = reader.getValue("ID");
      String name = reader.getValue("NAME");
      String info = reader.getValue("INFO");
      Achievement achievement = new Achievement(id, name, info);

      if (reader.hasValue("MINIMUM_BATTLE_COUNT")) {
        try {
          String minimumBattleCount = reader.getValue("MINIMUM_BATTLE_COUNT");
          achievement.setMinimumBattleCount(Integer.parseInt(minimumBattleCount));
        } catch (NumberFormatException ignore) {
        }
      }

      if (reader.hasValue("LONGEST_BATTLE_LENGTH")) {
        try {
          String longestBattleLength = reader.getValue("LONGEST_BATTLE_LENGTH");
          achievement.setLongestBattleLength(Integer.parseInt(longestBattleLength));
        } catch (NumberFormatException ignore) {
        }
      }

      if (reader.hasValue("KILLS_BY_LOCATION_ID")) {
        try {
          String killsByLocationID = reader.getValue("KILLS_BY_LOCATION_ID");
          String[] parts = killsByLocationID.split(",");
          achievement.incrementKillsByLocationID(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException ignore) {
        }
      }

      if (reader.hasValue("VISITS_TO_THE_SAME_LOCATION")) {
        try {
          String visitsToTheSameLocation = reader.getValue("VISITS_TO_THE_SAME_LOCATION");
          String[] parts = visitsToTheSameLocation.split(",");
          achievement.incrementVisitsToTheSameLocation(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException ignore) {
        }
      }

      if (reader.hasValue("VISITS_TO_DISTINCT_LOCATIONS")) {
        try {
          String visitsToDistinctLocations = reader.getValue("VISITS_TO_DISTINCT_LOCATIONS");
          String[] parts = visitsToDistinctLocations.split(",");
          achievement.incrementVisitsToDistinctLocations(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException ignore) {
        }
      }

      if (reader.hasValue("KILLS_BY_CREATURE_ID")) {
        try {
          String killsByCreatureID = reader.getValue("KILLS_BY_CREATURE_ID");
          String[] parts = killsByCreatureID.split(",");
          achievement.incrementKillsByCreatureID(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException ignore) {
        }
      }

      if (reader.hasValue("KILLS_BY_CREATURE_TYPE")) {
        try {
          String killsByCreatureType = reader.getValue("KILLS_BY_CREATURE_TYPE");
          String[] parts = killsByCreatureType.split(",");
          achievement.incrementKillsByCreatureType(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException ignore) {
        }
      }

      if (reader.hasValue("KILLS_BY_WEAPON")) {
        try {
          String killsByCreatureType = reader.getValue("KILLS_BY_WEAPON");
          String[] parts = killsByCreatureType.split(",");
          achievement.incrementKillsByWeapon(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException ignore) {
        }
      }

      // Get ID from the Achievement as it is an ID already (not a String).
      ACHIEVEMENTS.put(achievement.getID(), achievement);
    }
    DLogger.info("Created " + ACHIEVEMENTS.size() + " achievements.");
  }

  private static void loadLicense() {
    final int CHARACTERS_IN_LICENSE = 513;
    InputStreamReader isr = new InputStreamReader(loader.getResourceAsStream("license.txt"));
    StringBuilder sb = new StringBuilder(CHARACTERS_IN_LICENSE);
    char[] buffer = new char[CHARACTERS_IN_LICENSE];
    int length;
    try {
      while ((length = isr.read(buffer)) != -1) {
        sb.append(buffer, 0, length);
      }
    } catch (IOException ignore) {
    }
    LICENSE = sb.toString();
  }

  public static Map<ID, CreatureBlueprint> getCreatureBlueprints() {
    return creatureBlueprints;
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
