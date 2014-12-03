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
package org.dungeon.core.game;

import org.dungeon.core.achievements.Achievement;
import org.dungeon.core.achievements.BattleAchievement;
import org.dungeon.core.achievements.ExplorationAchievement;
import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.CreatureBlueprint;
import org.dungeon.core.items.ItemBlueprint;
import org.dungeon.io.DLogger;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Poem;
import org.dungeon.utils.PoemBuilder;
import org.dungeon.utils.Utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class that stores all the game data that is loaded and not serialized.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public final class GameData {

    public static HashMap<String, CreatureBlueprint> CREATURE_BLUEPRINTS;
    public static HashMap<String, ItemBlueprint> ITEM_BLUEPRINTS;
    public static LocationPreset[] LOCATION_PRESETS;
    public static List<Achievement> ACHIEVEMENTS;
    public static List<Poem> POEMS;
    public static Font monospaced;

    public static String LICENSE;

    private static ClassLoader loader;

    static void loadGameData(boolean noPoems) {
        long milliseconds = System.nanoTime();
        DLogger.info("Started loading the game data.");
        loader = Thread.currentThread().getContextClassLoader();

        monospaced = new Font("Monospaced", Font.PLAIN, 14);

        loadItemBlueprints();
        loadCreatureBlueprints();
        loadLocationPresets();

        createAchievements();

        if (!noPoems) {
            loadPoems();
        }

        loadLicense();

        milliseconds = (System.nanoTime() - milliseconds) / 1000000;
        DLogger.info("Finished loading the game data. Took " + milliseconds + "ms.");
    }

    private static void loadItemBlueprints() {
        ITEM_BLUEPRINTS = new HashMap<String, ItemBlueprint>();
        BufferedReader br = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("items.txt")));
        String line;
        ItemBlueprint blueprint = new ItemBlueprint();
        try {
            while ((line = br.readLine()) != null) {
                if (Utils.isNotBlankString(line)) {
                    if (line.startsWith("ID:")) {
                        if (blueprint.getId() != null) {
                            ITEM_BLUEPRINTS.put(blueprint.getId(), blueprint);
                            blueprint = new ItemBlueprint();
                        }
                        blueprint.setId(Utils.getAfterColon(line).trim());
                    } else if (line.startsWith("TYPE:")) {
                        blueprint.setType(Utils.getAfterColon(line).trim());
                    } else if (line.startsWith("NAME:")) {
                        blueprint.setName(Utils.getAfterColon(line).trim());
                    } else if (line.startsWith("CUR_INTEGRITY:")) {
                        blueprint.setCurIntegrity(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("MAX_INTEGRITY:")) {
                        blueprint.setMaxIntegrity(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("REPAIRABLE:")) {
                        blueprint.setRepairable(Integer.parseInt(Utils.getAfterColon(line).trim()) == 1);
                    } else if (line.startsWith("WEAPON:")) {
                        blueprint.setWeapon(Integer.parseInt(Utils.getAfterColon(line).trim()) == 1);
                    } else if (line.startsWith("DAMAGE:")) {
                        blueprint.setDamage(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("HIT_RATE:")) {
                        blueprint.setHitRate(Double.parseDouble(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("INTEGRITY_DECREMENT_ON_HIT:")) {
                        blueprint.setIntegrityDecrementOnHit(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("FOOD:")) {
                        blueprint.setFood(Integer.parseInt(Utils.getAfterColon(line).trim()) == 1);
                    } else if (line.startsWith("NUTRITION:")) {
                        blueprint.setNutrition(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("INTEGRITY_DECREMENT_ON_EAT:")) {
                        blueprint.setIntegrityDecrementOnEat(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("CLOCK:")) {
                        blueprint.setClock(Integer.parseInt(Utils.getAfterColon(line).trim()) == 1);
                    }
                }
            }
            br.close();
        } catch (IOException exception) {
            DLogger.warning(exception.toString());
        }
        ITEM_BLUEPRINTS.put(blueprint.getId(), blueprint);
        DLogger.info("Loaded " + ITEM_BLUEPRINTS.size() + " item blueprints.");
    }

    private static void loadCreatureBlueprints() {
        CREATURE_BLUEPRINTS = new HashMap<String, CreatureBlueprint>();
        BufferedReader br = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("creatures.txt")));
        String line;
        CreatureBlueprint blueprint = new CreatureBlueprint();
        try {
            while ((line = br.readLine()) != null) {
                if (Utils.isNotBlankString(line)) {
                    if (line.startsWith("ID:")) {
                        if (blueprint.getId() != null) {
                            CREATURE_BLUEPRINTS.put(blueprint.getId(), blueprint);
                            blueprint = new CreatureBlueprint();
                        }
                        blueprint.setId(Utils.getAfterColon(line).trim());
                    } else if (line.startsWith("TYPE:")) {
                        blueprint.setType(Utils.getAfterColon(line).trim());
                    } else if (line.startsWith("NAME:")) {
                        blueprint.setName(Utils.getAfterColon(line).trim());
                    } else if (line.startsWith("CUR_HEALTH:")) {
                        blueprint.setCurHealth(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("MAX_HEALTH:")) {
                        blueprint.setMaxHealth(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("ATTACK:")) {
                        blueprint.setAttack(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("ATTACK_ALGORITHM_ID:")) {
                        blueprint.setAttackAlgorithmID(Utils.getAfterColon(line).trim());
                    }
                }
            }
            br.close();
        } catch (IOException exception) {
            DLogger.warning(exception.toString());
        }
        CREATURE_BLUEPRINTS.put(blueprint.getId(), blueprint);
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

        LocationPreset clearing = new LocationPreset("Clearing");
        clearing.addSpawner(frog).addSpawner(rabbit).addSpawner(spider).addSpawner(fox);
        clearing.addItem("CHERRY", 0.6).addItem("STICK", 0.9);
        clearing.setLightPermittivity(1.0);
        clearing.finish();
        locationPresets.add(clearing);

        LocationPreset desert = new LocationPreset("Desert");
        desert.addSpawner(rat).addSpawner(snake).addSpawner(zombie).addSpawner(boar);
        desert.addItem("MACE", 0.1).addItem("STAFF", 0.2).addItem("DAGGER", 0.15).addItem("SPEAR", 0.1);
        desert.setLightPermittivity(1.0);
        desert.finish();
        locationPresets.add(desert);

        LocationPreset forest = new LocationPreset("Forest");
        forest.addSpawner(bear).addSpawner(frog).addSpawner(rabbit).addSpawner(whiteTiger).addSpawner(zombie);
        forest.addItem("AXE", 0.2).addItem("POCKET_WATCH", 0.03).addItem("STICK", 0.5);
        forest.setLightPermittivity(0.7);
        forest.finish();
        locationPresets.add(forest);

        LocationPreset graveyard = new LocationPreset("Graveyard");
        graveyard.addSpawner(bat).addSpawner(skeleton).addSpawner(zombie).addSpawner(orc);
        graveyard.addItem("LONGSWORD", 0.15).addItem("WRIST_WATCH", 0.025);
        graveyard.setLightPermittivity(0.9);
        graveyard.finish();
        locationPresets.add(graveyard);

        LocationPreset meadow = new LocationPreset("Meadow");
        meadow.addSpawner(whiteTiger).addSpawner(wolf);
        meadow.addItem("STONE", 0.8).addItem("WATERMELON", 0.4).addItem("APPLE", 0.7);
        meadow.setLightPermittivity(1.0);
        meadow.finish();
        locationPresets.add(meadow);

        LocationPreset pond = new LocationPreset("Pond");
        pond.addSpawner(frog).addSpawner(komodoDragon).addSpawner(crocodile);
        pond.addItem("WATERMELON", 0.8).addItem("SPEAR", 0.3);
        pond.setLightPermittivity(0.96);
        pond.finish();
        locationPresets.add(pond);

        LocationPreset swamp = new LocationPreset("Swamp");
        swamp.addSpawner(frog).addSpawner(snake).addSpawner(komodoDragon).addSpawner(crocodile);
        swamp.addItem("STICK", 0.9).addItem("WATERMELON", 0.12);
        swamp.setLightPermittivity(0.7);
        swamp.finish();
        locationPresets.add(swamp);

        LocationPreset wasteland = new LocationPreset("Wasteland");
        wasteland.addSpawner(rat).addSpawner(spider).addSpawner(snake);
        wasteland.addItem("STONE", 0.3).addItem("STICK", 0.18);
        wasteland.setLightPermittivity(1.0);
        wasteland.finish();
        locationPresets.add(wasteland);

        LocationPreset savannah = new LocationPreset("Savannah");
        savannah.addSpawner(boar).addSpawner(snake).addSpawner(whiteTiger);
        savannah.addItem("APPLE", 0.8).addItem("AXE", 0.3);
        savannah.setLightPermittivity(1.0);
        savannah.finish();
        locationPresets.add(savannah);

        LOCATION_PRESETS = new LocationPreset[locationPresets.size()];
        locationPresets.toArray(LOCATION_PRESETS);
        DLogger.info("Created " + LOCATION_PRESETS.length + " location presets.");
    }

    private static void createAchievements() {
        ACHIEVEMENTS = new ArrayList<Achievement>();

        // Exploration achievements.
        ACHIEVEMENTS.add(new ExplorationAchievement("TRAVELER", "Traveler", "Visit 5 different locations of the same type.", 5, 0));
        ACHIEVEMENTS.add(new ExplorationAchievement("WIPE", "Wipe", "Kill 3 creatures in the same location.", 0, 3));

        // Battle achievements that do not require specific kills.
        ACHIEVEMENTS.add(new BattleAchievement("FIRST_BLOOD", "First Blood", "Kill a creature.", 1, 0, null, null, null));
        ACHIEVEMENTS.add(new BattleAchievement("KILLER", "Killer", "Kill 10 creatures.", 10, 0, null, null, null));
        ACHIEVEMENTS.add(new BattleAchievement("DIE_HARD", "Die hard", "Take 10 turns to kill a creature.", 0, 10, null, null, null));

        // Battle achievements that rely on the kill count of a specific creature ID.
        // Bane requires six battles against bats.
        CounterMap<String> baneRequirements = new CounterMap<String>("BAT", 6);
        ACHIEVEMENTS.add(new BattleAchievement("BANE", "Bane", "Kill 6 bats.",
                0, 0, baneRequirements, null, null));
        // Cat requires four battles against rats.
        CounterMap<String> catRequirements = new CounterMap<String>("RAT", 4);
        ACHIEVEMENTS.add(new BattleAchievement("CAT", "Cat", "Kill 4 rats.",
                0, 0, catRequirements, null, null));
        // Evil Bastard requires one battle against a rabbit.
        CounterMap<String> evilBastardRequirements = new CounterMap<String>("RABBIT", 1);
        ACHIEVEMENTS.add(new BattleAchievement("EVIL_BASTARD", "Evil Bastard", "Kill an innocent rabbit.",
                0, 0, evilBastardRequirements, null, null));
        // Stay Dead requires two battles against a zombie.
        CounterMap<String> stayDeadRequirements = new CounterMap<String>("ZOMBIE", 2);
        ACHIEVEMENTS.add(new BattleAchievement("STAY_DEAD", "Stay Dead", "Kill 2 zombies.",
                0, 0, stayDeadRequirements, null, null));

        CounterMap<String> dissectionRequirements = new CounterMap<String>("FROG", 5);
        ACHIEVEMENTS.add(new BattleAchievement("DISSECTION", "Dissection", "Kill 5 frogs.",
                0, 0, dissectionRequirements, null, null));

        // Battle achievements that rely on the kill count of a specific type.
        // Professional Coward requires killing 10 critters.
        CounterMap<String> professionalCowardRequirements = new CounterMap<String>("Critter", 10);
        ACHIEVEMENTS.add(new BattleAchievement("PROFESSIONAL_COWARD", "Professional Coward", "Kill 10 critters.",
                0, 0, null, professionalCowardRequirements, null));

        CounterMap<String> hunterRequirements = new CounterMap<String>("Beast", 10);
        ACHIEVEMENTS.add(new BattleAchievement("HUNTER", "Hunter", "Kill 10 beasts.",
                0, 0, null, hunterRequirements, null));

        // Battle achievements that rely on the number of kills with a specific weapon.
        // An empty string is used to to register unarmed kills.
        CounterMap<String> fiveFingerDeathPunchRequirements = new CounterMap<String>(Constants.UNARMED_ID, 1);
        ACHIEVEMENTS.add(new BattleAchievement("FIVE_FINGER_DEATH_PUNCH", "Five Finger Death Punch", "Kill a creature unarmed.",
                0, 0, null, null, fiveFingerDeathPunchRequirements));

        CounterMap<String> boxer = new CounterMap<String>(Constants.UNARMED_ID, 10);
        ACHIEVEMENTS.add(new BattleAchievement("BOXER", "Boxer", "Kill 10 creatures unarmed.",
                0, 0, null, null, boxer));

        CounterMap<String> onTheStickRequirements = new CounterMap<String>("STICK", 2);
        ACHIEVEMENTS.add(new BattleAchievement("ON_THE_STICK!", "On the Stick!", "Kill 2 creatures with the Stick.",
                0, 0, null, null, onTheStickRequirements));

        CounterMap<String> sticksAndStonesRequirements = new CounterMap<String>("STICK", 5);
        sticksAndStonesRequirements.incrementCounter("STONE", 5);
        ACHIEVEMENTS.add(new BattleAchievement("STICKS_AND_STONES", "Sticks and Stones", "Kill 5 creatures with the Stone and 5 with the Stick.",
                0, 0, null, null, sticksAndStonesRequirements));

        CounterMap<String> lumberjackRequirements = new CounterMap<String>("AXE", 10);
        ACHIEVEMENTS.add(new BattleAchievement("LUMBERJACK", "Lumberjack", "Kill 10 creatures with the Axe.",
                0, 0, null, null, lumberjackRequirements));
        DLogger.info("Created " + ACHIEVEMENTS.size() + " achievements.");
    }

    private static void loadPoems() {
        POEMS = new ArrayList<Poem>();

        String IDENTIFIER_TITLE = "TITLE:";
        String IDENTIFIER_AUTHOR = "AUTHOR:";
        String IDENTIFIER_CONTENT = "CONTENT:";

        BufferedReader br = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("poems.txt")));

        String line;

        StringBuilder contentBuilder = new StringBuilder();
        PoemBuilder pb = new PoemBuilder();

        try {
            while ((line = br.readLine()) != null) {
                if (line.startsWith(IDENTIFIER_TITLE)) {
                    if (pb.isComplete()) {
                        POEMS.add(pb.createPoem());
                        pb = new PoemBuilder();
                        contentBuilder.setLength(0);
                    }
                    pb.setTitle(Utils.getAfterColon(line).trim());
                } else if (line.startsWith(IDENTIFIER_AUTHOR)) {
                    pb.setAuthor(Utils.getAfterColon(line).trim());
                } else if (line.startsWith(IDENTIFIER_CONTENT)) {
                    contentBuilder.append(Utils.getAfterColon(line).trim());
                    while ((line = br.readLine()) != null && !line.isEmpty() && !line.startsWith(IDENTIFIER_TITLE)) {
                        contentBuilder.append('\n').append(line.trim());
                    }
                    pb.setContent(contentBuilder.toString());
                }
            }
            br.close();
        } catch (IOException exception) {
            DLogger.warning(exception.toString());
        }
        if (pb.isComplete()) {
            POEMS.add(pb.createPoem());
        }
        DLogger.info("Loaded " + POEMS.size() + " poems.");
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

    public static LocationPreset getRandomRiver() {
        LocationPreset river = new LocationPreset("River");
        river.block(Direction.EAST).block(Direction.WEST);
        river.setLightPermittivity(1.0);
        return river;
    }

    public static LocationPreset getRandomBridge() {
        LocationPreset bridge = new LocationPreset("Bridge");
        bridge.block(Direction.NORTH).block(Direction.SOUTH);
        bridge.setLightPermittivity(1.0);
        return bridge;
    }

}
