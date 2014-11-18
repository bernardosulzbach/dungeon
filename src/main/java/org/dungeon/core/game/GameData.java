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
    private static ClassLoader loader;

    static void loadGameData(boolean noPoems) {
        long milliseconds = System.nanoTime();
        DLogger.finest("Started loading the game data.");
        loader = Thread.currentThread().getContextClassLoader();

        monospaced = new Font("Monospaced", Font.PLAIN, 14);

        loadItemBlueprints();
        loadCreatureBlueprints();
        loadLocationPresets();

        createAchievements();

        if (!noPoems) {
            loadPoems();
        }

        milliseconds = (System.nanoTime() - milliseconds) / 1000000;
        DLogger.finest("Finished loading the game data. Took " + milliseconds + "ms.");
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
                    } else if (line.startsWith("EXPERIENCE_ON_EAT:")) {
                        blueprint.setExperienceOnEat(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("INTEGRITY_DECREMENT_ON_EAT:")) {
                        blueprint.setIntegrityDecrementOnEat(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("CLOCK:")) {
                        blueprint.setClock(Integer.parseInt(Utils.getAfterColon(line).trim()) == 1);
                    }
                }
            }
            br.close();
        } catch (IOException exception) {
            DLogger.severe(exception.toString());
        }
        ITEM_BLUEPRINTS.put(blueprint.getId(), blueprint);
        DLogger.finest("Loaded " + ITEM_BLUEPRINTS.size() + " item blueprints.");
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
                    } else if (line.startsWith("MAX_HEALTH_INCREMENT:")) {
                        blueprint.setMaxHealthIncrement(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("ATTACK:")) {
                        blueprint.setAttack(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("ATTACK_INCREMENT:")) {
                        blueprint.setAttackIncrement(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    } else if (line.startsWith("ATTACK_ALGORITHM_ID:")) {
                        blueprint.setAttackAlgorithmID(Utils.getAfterColon(line).trim());
                    } else if (line.startsWith("EXPERIENCE_DROP_FACTOR:")) {
                        blueprint.setExperienceDropFactor(Integer.parseInt(Utils.getAfterColon(line).trim()));
                    }
                }
            }
            br.close();
        } catch (IOException exception) {
            DLogger.severe(exception.toString());
        }
        CREATURE_BLUEPRINTS.put(blueprint.getId(), blueprint);
        DLogger.finest("Loaded " + CREATURE_BLUEPRINTS.size() + " creature blueprints.");
    }

    private static void loadLocationPresets() {
        ArrayList<LocationPreset> locationPresets = new ArrayList<LocationPreset>();

        SpawnerPreset bat = new SpawnerPreset("BAT", 1, 4);
        SpawnerPreset bear = new SpawnerPreset("BEAR", 1, 2);
        SpawnerPreset frog = new SpawnerPreset("FROG", 2, 16);
        SpawnerPreset rabbit = new SpawnerPreset("RABBIT", 8, 24); // They fuck a lot.
        SpawnerPreset rat = new SpawnerPreset("RAT", 6, 10);
        SpawnerPreset skeleton = new SpawnerPreset("SKELETON", 1, 2);
        SpawnerPreset snake = new SpawnerPreset("SNAKE", 3, 6);
        SpawnerPreset spider = new SpawnerPreset("SPIDER", 2, 6);
        SpawnerPreset whiteTiger = new SpawnerPreset("WHITE_TIGER", 1, 1);
        SpawnerPreset wolf = new SpawnerPreset("WOLF", 3, 2);
        SpawnerPreset zombie = new SpawnerPreset("ZOMBIE", 2, 8);

        SpawnerPreset[] clearingPresets = {frog, rabbit, spider};
        ItemFrequencyPair[] clearingItems = {new ItemFrequencyPair("CHERRY", 0.6), new ItemFrequencyPair("STICK", 0.9)};
        locationPresets.add(new LocationPreset("Clearing", clearingPresets, clearingItems, 0.9));

        SpawnerPreset[] desertPresets = {rat, snake, zombie};
        ItemFrequencyPair[] desertItems = {new ItemFrequencyPair("CHERRY", 0.6)};
        locationPresets.add(new LocationPreset("Desert", desertPresets, desertItems, 1.0));

        SpawnerPreset[] forestPresets = {bear, frog, rabbit, whiteTiger, zombie};
        ItemFrequencyPair[] forestItems = {new ItemFrequencyPair("AXE", 0.1), new ItemFrequencyPair("CLOCK", 0.05)};
        locationPresets.add(new LocationPreset("Forest", forestPresets, forestItems, 0.7));

        SpawnerPreset[] graveyardPresets = {bat, skeleton, zombie};
        ItemFrequencyPair[] graveyardItems = {new ItemFrequencyPair("LONGSWORD", 0.15)};
        locationPresets.add(new LocationPreset("Graveyard", graveyardPresets, graveyardItems, 0.9));

        SpawnerPreset[] meadowPresets = {whiteTiger, wolf};
        ItemFrequencyPair[] meadowItems = {new ItemFrequencyPair("STONE", 0.8)};
        locationPresets.add(new LocationPreset("Meadow", meadowPresets, meadowItems, 1.0));

        SpawnerPreset[] pondPresets = {frog, snake};
        ItemFrequencyPair[] pondItems = {new ItemFrequencyPair("WATERMELON", 0.8)};
        locationPresets.add(new LocationPreset("Pond", pondPresets, pondItems, 1.0));

        LOCATION_PRESETS = new LocationPreset[locationPresets.size()];
        locationPresets.toArray(LOCATION_PRESETS);
        DLogger.finest("Created " + LOCATION_PRESETS.length + " location presets.");
    }

    private static void createAchievements() {
        // TODO: Write a builder for teh achievements. They will not be loaded.
        ACHIEVEMENTS = new ArrayList<Achievement>();

        // Exploration achievements.
        ACHIEVEMENTS.add(new ExplorationAchievement("TRAVELER", "Traveler", "Visit 5 different locations of the same type.", 25, 5, 0));
        ACHIEVEMENTS.add(new ExplorationAchievement("WIPE", "Wipe", "Kill 3 creatures in the same location.", 50, 0, 3));

        // Battle achievements that do not require specific kills.
        ACHIEVEMENTS.add(new BattleAchievement("FIRST_BLOOD", "First Blood", "Kill a creature.", 10, 1, 0, null, null, null));
        ACHIEVEMENTS.add(new BattleAchievement("KILLER", "Killer", "Kill 10 creatures.", 100, 10, 0, null, null, null));
        ACHIEVEMENTS.add(new BattleAchievement("DIE_HARD", "Die hard", "Take 10 turns to kill a creature.", 150, 0, 10, null, null, null));

        // Battle achievements that rely on the kill count of a specific creature ID.
        // Bane requires six battles against bats.
        CounterMap<String> baneRequirements = new CounterMap<String>("BAT", 6);
        ACHIEVEMENTS.add(new BattleAchievement("BANE", "Bane", "Kill 6 bats.",
                50, 0, 0, baneRequirements, null, null));
        // Cat requires four battles against rats.
        CounterMap<String> catRequirements = new CounterMap<String>("RAT", 4);
        ACHIEVEMENTS.add(new BattleAchievement("CAT", "Cat", "Kill 4 rats.",
                40, 0, 0, catRequirements, null, null));
        // Evil Bastard requires one battle against a rabbit.
        CounterMap<String> evilBastardRequirements = new CounterMap<String>("RABBIT", 1);
        ACHIEVEMENTS.add(new BattleAchievement("EVIL_BASTARD", "Evil Bastard", "Kill an innocent rabbit.",
                5, 0, 0, evilBastardRequirements, null, null));
        // Stay Dead requires two battles against a zombie.
        CounterMap<String> stayDeadRequirements = new CounterMap<String>("ZOMBIE", 2);
        ACHIEVEMENTS.add(new BattleAchievement("STAY_DEAD", "Stay Dead", "Kill 2 zombies.",
                50, 0, 0, stayDeadRequirements, null, null));

        CounterMap<String> dissectionRequirements = new CounterMap<String>("FROG", 5);
        ACHIEVEMENTS.add(new BattleAchievement("DISSECTION", "Dissection", "Kill 5 frogs.",
                25, 0, 0, dissectionRequirements, null, null));

        // Battle achievements that rely on the kill count of a specific type.
        // Professional Coward requires killing 10 critters.
        CounterMap<String> professionalCowardRequirements = new CounterMap<String>("Critter", 10);
        ACHIEVEMENTS.add(new BattleAchievement("PROFESSIONAL_COWARD", "Professional Coward", "Kill 10 critters.",
                100, 0, 0, null, professionalCowardRequirements, null));

        CounterMap<String> hunterRequirements = new CounterMap<String>("Beast", 10);
        ACHIEVEMENTS.add(new BattleAchievement("HUNTER", "Hunter", "Kill 10 beasts.",
                125, 0, 0, null, hunterRequirements, null));

        // Battle achievements that rely on the number of kills with a specific weapon.
        // An empty string is used to to register unarmed kills.
        CounterMap<String> fiveFingerDeathPunchRequirements = new CounterMap<String>(Constants.UNARMED_ID, 1);
        ACHIEVEMENTS.add(new BattleAchievement("FIVE_FINGER_DEATH_PUNCH", "Five Finger Death Punch", "Kill a creature unarmed.",
                10, 0, 0, null, null, fiveFingerDeathPunchRequirements));

        CounterMap<String> boxer = new CounterMap<String>(Constants.UNARMED_ID, 10);
        ACHIEVEMENTS.add(new BattleAchievement("BOXER", "Boxer", "Kill 10 creatures unarmed.",
                100, 0, 0, null, null, boxer));

        CounterMap<String> onTheStickRequirements = new CounterMap<String>("STICK", 2);
        ACHIEVEMENTS.add(new BattleAchievement("ON_THE_STICK!", "On the Stick!", "Kill 2 creatures with the Stick.",
                20, 0, 0, null, null, onTheStickRequirements));

        CounterMap<String> sticksAndStonesRequirements = new CounterMap<String>("STICK", 5);
        sticksAndStonesRequirements.incrementCounter("STONE", 5);
        ACHIEVEMENTS.add(new BattleAchievement("STICKS_AND_STONES", "Sticks and Stones", "Kill 5 creatures with the Stone and 5 with the Stick.",
                40, 0, 0, null, null, sticksAndStonesRequirements));

        CounterMap<String> lumberjackRequirements = new CounterMap<String>("AXE", 10);
        ACHIEVEMENTS.add(new BattleAchievement("LUMBERJACK", "Lumberjack", "Kill 10 creatures with the Axe.",
                50, 0, 0, null, null, lumberjackRequirements));
        DLogger.finest("Created " + ACHIEVEMENTS.size() + " achievements.");
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
            DLogger.severe(exception.toString());
        }
        if (pb.isComplete()) {
            POEMS.add(pb.createPoem());
        }
        DLogger.finest("Loaded " + POEMS.size() + " poems.");
    }

}
