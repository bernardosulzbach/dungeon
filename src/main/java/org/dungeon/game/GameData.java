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
import org.dungeon.items.ItemBlueprint;
import org.dungeon.util.Constants;
import org.dungeon.util.StopWatch;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class that stores all the game data that is loaded and not serialized.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public final class GameData {

  // Hardcoded HashMap initial capacities. Be sure to increase it if more items and creatures are added.
  // The second parameter ensures that unless the HashMap is full, it will not have its capacity increased.
  public static final HashMap<ID, CreatureBlueprint> CREATURE_BLUEPRINTS = new HashMap<ID, CreatureBlueprint>(20, 1f);
  public static final HashMap<ID, ItemBlueprint> ITEM_BLUEPRINTS = new HashMap<ID, ItemBlueprint>(20, 1f);
  private static final PoetryLibrary poetryLibrary = new PoetryLibrary();
  private static final HintLibrary hintLibrary = new HintLibrary();
  private static final LocationPreset riverPreset = new LocationPreset("RIVER", "River");
  private static final LocationPreset bridgePreset = new LocationPreset("BRIDGE", "Bridge");
  public static LocationPreset[] LOCATION_PRESETS;
  public static HashMap<ID, Achievement> ACHIEVEMENTS;
  public static Font monospaced;
  public static String LICENSE;
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
    loadLocationPresets();

    createAchievements();

    loadLicense();

    DLogger.info("Finished loading the game data. Took " + stopWatch.toString() + ".");
  }

  /**
   * Loads all ItemBlueprints to a HashMap.
   */
  private static void loadItemBlueprints() {
    ResourceReader resourceReader = new ResourceReader(loader.getResourceAsStream("items.txt"));
    while (resourceReader.readNextElement()) {
      ItemBlueprint blueprint = new ItemBlueprint();
      blueprint.setId(new ID(resourceReader.getValue("ID")));
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
      if (resourceReader.contains("NUTRITION")) {
        blueprint.setNutrition(Integer.parseInt(resourceReader.getValue("NUTRITION")));
      }
      if (resourceReader.contains("INTEGRITY_DECREMENT_ON_EAT")) {
        blueprint.setIntegrityDecrementOnEat(Integer.parseInt(resourceReader.getValue("INTEGRITY_DECREMENT_ON_EAT")));
      }
      if (resourceReader.contains("CLOCK")) {
        blueprint.setClock(Integer.parseInt(resourceReader.getValue("CLOCK")) == 1);
      }
      ITEM_BLUEPRINTS.put(blueprint.getId(), blueprint);
    }
    resourceReader.close();
    DLogger.info("Loaded " + ITEM_BLUEPRINTS.size() + " item blueprints.");
  }

  /**
   * Loads all CreatureBlueprints to a HashMap.
   */
  private static void loadCreatureBlueprints() {
    ResourceReader resourceReader = new ResourceReader(loader.getResourceAsStream("creatures.txt"));
    while (resourceReader.readNextElement()) {
      CreatureBlueprint blueprint = new CreatureBlueprint();
      blueprint.setId(new ID(resourceReader.getValue("ID")));
      blueprint.setType(resourceReader.getValue("TYPE"));
      blueprint.setName(resourceReader.getValue("NAME"));
      blueprint.setCurHealth(Integer.parseInt(resourceReader.getValue("CUR_HEALTH")));
      blueprint.setMaxHealth(Integer.parseInt(resourceReader.getValue("MAX_HEALTH")));
      blueprint.setAttack(Integer.parseInt(resourceReader.getValue("ATTACK")));
      blueprint.setAttackAlgorithmID(resourceReader.getValue("ATTACK_ALGORITHM_ID"));
      CREATURE_BLUEPRINTS.put(blueprint.getId(), blueprint);
    }
    resourceReader.close();
    DLogger.info("Loaded " + CREATURE_BLUEPRINTS.size() + " creature blueprints.");
  }

  private static void loadLocationPresets() {
    ArrayList<LocationPreset> locationPresets = new ArrayList<LocationPreset>();

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

    LocationPreset clearing = new LocationPreset("CLEARING", "Clearing");
    clearing.addSpawner(frog).addSpawner(rabbit).addSpawner(spider).addSpawner(fox);
    clearing.addItem("CHERRY", 0.6).addItem("STICK", 0.9);
    clearing.setLightPermittivity(1.0);
    clearing.finish();
    locationPresets.add(clearing);

    LocationPreset desert = new LocationPreset("DESERT", "Desert");
    desert.addSpawner(rat).addSpawner(snake).addSpawner(zombie).addSpawner(boar);
    desert.addItem("MACE", 0.1).addItem("STAFF", 0.2).addItem("DAGGER", 0.15).addItem("SPEAR", 0.1);
    desert.setLightPermittivity(1.0);
    desert.finish();
    locationPresets.add(desert);

    LocationPreset forest = new LocationPreset("FOREST", "Forest");
    forest.addSpawner(bear).addSpawner(frog).addSpawner(rabbit).addSpawner(whiteTiger).addSpawner(zombie);
    forest.addItem("AXE", 0.2).addItem("POCKET_WATCH", 0.03).addItem("STICK", 0.5);
    forest.setLightPermittivity(0.7);
    forest.finish();
    locationPresets.add(forest);

    LocationPreset graveyard = new LocationPreset("GRAVEYARD", "Graveyard");
    graveyard.addSpawner(bat).addSpawner(skeleton).addSpawner(zombie).addSpawner(orc);
    graveyard.addItem("LONGSWORD", 0.15).addItem("WRIST_WATCH", 0.025);
    graveyard.setLightPermittivity(0.9);
    graveyard.finish();
    locationPresets.add(graveyard);

    LocationPreset meadow = new LocationPreset("MEADOW", "Meadow");
    meadow.addSpawner(whiteTiger).addSpawner(wolf);
    meadow.addItem("STONE", 0.8).addItem("WATERMELON", 0.4).addItem("APPLE", 0.7);
    meadow.setLightPermittivity(1.0);
    meadow.finish();
    locationPresets.add(meadow);

    LocationPreset pond = new LocationPreset("POND", "Pond");
    pond.addSpawner(frog).addSpawner(komodoDragon).addSpawner(crocodile);
    pond.addItem("WATERMELON", 0.8).addItem("SPEAR", 0.3);
    pond.setLightPermittivity(0.96);
    pond.finish();
    locationPresets.add(pond);

    LocationPreset swamp = new LocationPreset("SWAMP", "Swamp");
    swamp.addSpawner(frog).addSpawner(snake).addSpawner(komodoDragon).addSpawner(crocodile);
    swamp.addItem("STICK", 0.9).addItem("WATERMELON", 0.12);
    swamp.setLightPermittivity(0.7);
    swamp.finish();
    locationPresets.add(swamp);

    LocationPreset wasteland = new LocationPreset("WASTELAND", "Wasteland");
    wasteland.addSpawner(rat).addSpawner(spider).addSpawner(snake);
    wasteland.addItem("STONE", 0.3).addItem("STICK", 0.18);
    wasteland.setLightPermittivity(1.0);
    wasteland.finish();
    locationPresets.add(wasteland);

    LocationPreset savannah = new LocationPreset("SAVANNAH", "Savannah");
    savannah.addSpawner(boar).addSpawner(snake).addSpawner(whiteTiger);
    savannah.addItem("APPLE", 0.8).addItem("AXE", 0.3);
    savannah.setLightPermittivity(1.0);
    savannah.finish();
    locationPresets.add(savannah);

    LOCATION_PRESETS = new LocationPreset[locationPresets.size()];
    locationPresets.toArray(LOCATION_PRESETS);

    makeHardcodedLocationPresets();

    DLogger.info("Created " + LOCATION_PRESETS.length + " location presets.");
  }

  /**
   * Makes the river and the bridge presets.
   */
  private static void makeHardcodedLocationPresets() {
    riverPreset.block(Direction.WEST).block(Direction.EAST);
    riverPreset.setLightPermittivity(1.0);

    bridgePreset.block(Direction.NORTH).block(Direction.SOUTH);
    bridgePreset.setLightPermittivity(1.0);
  }

  /**
   * Creates all Achievements.
   */
  private static void createAchievements() {
    // TODO: load this from a resource file.
    ACHIEVEMENTS = new HashMap<ID, Achievement>();

    // Observe that there is some ordering (similar Achievements should be grouped together).

    Achievement firstBlood = new Achievement("FIRST_BLOOD", "First Blood", "Kill a creature.");
    firstBlood.setMinimumBattleCount(1);
    ACHIEVEMENTS.put(firstBlood.getId(), firstBlood);

    Achievement killer = new Achievement("KILLER", "Killer", "Kill 10 creatures.");
    killer.setMinimumBattleCount(10);
    ACHIEVEMENTS.put(killer.getId(), killer);

    Achievement dieHard = new Achievement("DIE_HARD", "Die hard", "Take 10 turns to kill a creature.");
    dieHard.setLongestBattleLength(10);
    ACHIEVEMENTS.put(dieHard.getId(), dieHard);

    Achievement forestHunter = new Achievement("FOREST_HUNTER", "Forest Hunter", "Kill 10 creatures in forests.");
    forestHunter.incrementKillsByLocationID("FOREST", 10);
    ACHIEVEMENTS.put(forestHunter.getId(), forestHunter);

    Achievement iMissYou = new Achievement("I_MISS_YOU", "I miss you!", "Visit the same graveyard 10 times.");
    iMissYou.incrementVisitsToTheSameLocation("GRAVEYARD", 10);
    ACHIEVEMENTS.put(iMissYou.getId(), iMissYou);

    Achievement archaeologist = new Achievement("ARCHAEOLOGIST", "Archaeologist", "Visit 10 different deserts.");
    archaeologist.incrementVisitsToDistinctLocations("DESERT", 10);
    ACHIEVEMENTS.put(archaeologist.getId(), archaeologist);

    Achievement reaper = new Achievement("REAPER", "Reaper", "Visit 10 graveyards.");
    reaper.incrementVisitsToDistinctLocations("GRAVEYARD", 10);
    ACHIEVEMENTS.put(reaper.getId(), reaper);

    // TODO: implement kill count by creature type and let this count 25 critters.
    Achievement paranoid = new Achievement("PARANOID", "Paranoid", "Kill 25 rabbits.");
    paranoid.incrementKillsByCreatureId("RABBIT", 25);
    ACHIEVEMENTS.put(paranoid.getId(), paranoid);

    Achievement bane = new Achievement("BANE", "Bane", "Kill 6 bats.");
    bane.incrementKillsByCreatureId("BAT", 6);
    ACHIEVEMENTS.put(bane.getId(), bane);

    Achievement cat = new Achievement("CAT", "Cat", "Kill 4 rats.");
    cat.incrementKillsByCreatureId("RAT", 4);
    ACHIEVEMENTS.put(cat.getId(), cat);

    Achievement evilBastard = new Achievement("EVIL_BASTARD", "Evil Bastard", "Kill an innocent rabbit.");
    evilBastard.incrementKillsByCreatureId("RABBIT", 1);
    ACHIEVEMENTS.put(evilBastard.getId(), evilBastard);

    Achievement stayDead = new Achievement("STAY_DEAD", "Stay Dead", "Kill 2 zombies.");
    stayDead.incrementKillsByCreatureId("ZOMBIE", 2);
    ACHIEVEMENTS.put(stayDead.getId(), stayDead);

    Achievement dissection = new Achievement("DISSECTION", "Dissection", "Kill 5 frogs.");
    dissection.incrementKillsByCreatureId("FROG", 5);
    ACHIEVEMENTS.put(dissection.getId(), dissection);

    Achievement proCoward = new Achievement("PROFESSIONAL_COWARD", "Professional Coward", "Kill 10 critters.");
    proCoward.incrementKillsByCreatureType("Critter", 10);
    ACHIEVEMENTS.put(proCoward.getId(), proCoward);

    Achievement hunter = new Achievement("HUNTER", "Hunter", "Kill 10 beasts.");
    hunter.incrementKillsByCreatureType("Beast", 10);
    ACHIEVEMENTS.put(hunter.getId(), hunter);

    Achievement deathPunch = new Achievement("DEATH_PUNCH", "Death Punch", "Kill a creature unarmed.");
    deathPunch.incrementKillsByWeapon(Constants.UNARMED_ID.getId(), 1);
    ACHIEVEMENTS.put(deathPunch.getId(), deathPunch);

    Achievement boxer = new Achievement("BOXER", "Boxer", "Kill 10 creatures unarmed.");
    boxer.incrementKillsByWeapon(Constants.UNARMED_ID.getId(), 10);
    ACHIEVEMENTS.put(boxer.getId(), boxer);

    Achievement onTheStick = new Achievement("ON_THE_STICK", "On the Stick!", "Kill 2 creatures with the Stick.");
    onTheStick.incrementKillsByWeapon("STICK", 2);
    ACHIEVEMENTS.put(onTheStick.getId(), onTheStick);

    Achievement sticksAndStones = new Achievement("STICKS_AND_STONES", "Sticks and Stones",
        "Kill 5 creatures with the Stone and 5 with the Stick.");
    sticksAndStones.incrementKillsByWeapon("STICK", 5);
    sticksAndStones.incrementKillsByWeapon("STONE", 5);
    ACHIEVEMENTS.put(sticksAndStones.getId(), sticksAndStones);

    Achievement lumberjack = new Achievement("LUMBERJACK", "Lumberjack", "Kill 10 creatures with the Axe.");
    lumberjack.incrementKillsByWeapon("AXE", 10);
    ACHIEVEMENTS.put(lumberjack.getId(), lumberjack);

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

  /**
   * Returns a LocationPreset for a river.
   *
   * @return a LocationPreset.
   */
  public static LocationPreset getRandomRiver() {
    return riverPreset;
  }

  /**
   * Returns a LocationPreset for a bridge than can be accessed by west or east.
   *
   * @return a LocationPreset.
   */
  public static LocationPreset getRandomBridge() {
    return bridgePreset;
  }

}
