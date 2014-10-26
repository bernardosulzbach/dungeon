package org.dungeon.core.game;

import org.dungeon.core.creatures.CreatureBlueprint;
import org.dungeon.core.items.ItemBlueprint;
import org.dungeon.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class that stores all the game data that is loaded and not serialized.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
final class GameData {

    static LocationPreset[] LOCATION_PRESETS;
    private static ClassLoader loader;
    public static final HashMap<String, CreatureBlueprint> CREATURE_BLUEPRINTS = new HashMap<String, CreatureBlueprint>();
    public static final HashMap<String, ItemBlueprint> ITEM_BLUEPRINTS = new HashMap<String, ItemBlueprint>();

    static void loadGameData() {
        loader = Thread.currentThread().getContextClassLoader();

        loadItemBlueprints();
        loadCreatureBlueprints();

        LOCATION_PRESETS = loadLocationPresets();
    }

    private static void loadItemBlueprints() {
        BufferedReader br = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("res/items.txt")));
        String line;
        ItemBlueprint blueprint = new ItemBlueprint();
        try {
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlankString(line)) {
                    if (line.startsWith("ID:")) {
                        if (blueprint.getId() != null) {
                            ITEM_BLUEPRINTS.put(blueprint.getId(), blueprint);
                            blueprint = new ItemBlueprint();
                        }
                        blueprint.setId(line.split(":")[1].trim());
                    } else if (line.startsWith("TYPE:")) {
                        blueprint.setType(line.split(":")[1].trim());
                    } else if (line.startsWith("NAME:")) {
                        blueprint.setName(line.split(":")[1].trim());
                    } else if (line.startsWith("CUR_INTEGRITY:")) {
                        blueprint.setCurIntegrity(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("MAX_INTEGRITY:")) {
                        blueprint.setMaxIntegrity(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("REPAIRABLE:")) {
                        blueprint.setRepairable(Integer.parseInt(line.split(":")[1].trim()) == 1);
                    } else if (line.startsWith("WEAPON:")) {
                        blueprint.setWeapon(Integer.parseInt(line.split(":")[1].trim()) == 1);
                    } else if (line.startsWith("DAMAGE:")) {
                        blueprint.setDamage(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("HIT_RATE:")) {
                        blueprint.setHitRate(Double.parseDouble(line.split(":")[1].trim()));
                    } else if (line.startsWith("INTEGRITY_DECREMENT_ON_HIT:")) {
                        blueprint.setIntegrityDecrementOnHit(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("NUTRITION:")) {
                        blueprint.setNutrition(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("EXPERIENCE_ON_EAT:")) {
                        blueprint.setExperienceOnEat(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("INTEGRITY_DECREMENT_ON_EAT:")) {
                        blueprint.setIntegrityDecrementOnEat(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("CLOCK:")) {
                        blueprint.setClock(Integer.parseInt(line.split(":")[1].trim()) == 1);
                    }
                }
            }
        } catch (IOException ignored) {
        }
        ITEM_BLUEPRINTS.put(blueprint.getId(), blueprint);
    }

    private static void loadCreatureBlueprints() {
        BufferedReader br = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("res/creatures.txt")));
        String line;
        CreatureBlueprint blueprint = new CreatureBlueprint();
        try {
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlankString(line)) {
                    if (line.startsWith("ID:")) {
                        if (blueprint.getId() != null) {
                            CREATURE_BLUEPRINTS.put(blueprint.getId(), blueprint);
                            blueprint = new CreatureBlueprint();
                        }
                        blueprint.setId(line.split(":")[1].trim());
                    } else if (line.startsWith("TYPE:")) {
                        blueprint.setType(line.split(":")[1].trim());
                    } else if (line.startsWith("NAME:")) {
                        blueprint.setName(line.split(":")[1].trim());
                    } else if (line.startsWith("CUR_HEALTH:")) {
                        blueprint.setCurHealth(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("MAX_HEALTH:")) {
                        blueprint.setMaxHealth(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("MAX_HEALTH_INCREMENT:")) {
                        blueprint.setMaxHealthIncrement(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("ATTACK:")) {
                        blueprint.setAttack(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("ATTACK_INCREMENT:")) {
                        blueprint.setAttackIncrement(Integer.parseInt(line.split(":")[1].trim()));
                    } else if (line.startsWith("ATTACK_ALGORITHM_ID:")) {
                        blueprint.setAttackAlgorithmID(line.split(":")[1].trim());
                    } else if (line.startsWith("EXPERIENCE_DROP_FACTOR:")) {
                        blueprint.setExperienceDropFactor(Integer.parseInt(line.split(":")[1].trim()));
                    }
                }
            }
        } catch (IOException ignored) {
        }
        CREATURE_BLUEPRINTS.put(blueprint.getId(), blueprint);
    }


    private static LocationPreset[] loadLocationPresets() {
        ArrayList<LocationPreset> locationPresets = new ArrayList<LocationPreset>();

        String[] clearingCreatures = {"BAT"};
        String[] clearingItems = {"STONE"};
        locationPresets.add(new LocationPreset("Clearing", 0.9, clearingCreatures, clearingItems));

        String[] desertCreatures = {"BAT"};
        String[] desertItems = {};
        locationPresets.add(new LocationPreset("Desert", 1.0, desertCreatures, desertItems));

        String[] forestCreatures = {"BAT"};
        String[] forestItems = {"CLOCK"};
        locationPresets.add(new LocationPreset("Forest", 0.7, forestCreatures, forestItems));

        String[] graveyardCreatures = {"BAT"};
        String[] graveyardItems = {};
        locationPresets.add(new LocationPreset("Graveyard", 0.7, graveyardCreatures, graveyardItems));

        String[] meadowCreatures = {"BAT"};
        String[] meadowItems = {"STONE"};
        locationPresets.add(new LocationPreset("Meadow", 1.0, meadowCreatures, meadowItems));

        String[] pondCreatures = {"BAT"};
        String[] pondItems = {"WATERMELON"};
        locationPresets.add(new LocationPreset("Pond", 1.0, pondCreatures, pondItems));

        LocationPreset[] locationPresetsArray = new LocationPreset[locationPresets.size()];
        locationPresets.toArray(locationPresetsArray);
        return locationPresetsArray;
    }
}
