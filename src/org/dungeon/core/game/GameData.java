package org.dungeon.core.game;

import org.dungeon.core.creatures.CreatureBlueprint;
import org.dungeon.core.items.ItemPreset;
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
    public static final HashMap<String, CreatureBlueprint> CREATURE_BLUEPRINTS = new HashMap<String, CreatureBlueprint>();
    // TODO: do with ITEM_PRESETS what I already did for the creatures.
    // Remember to change ItemPreset to ItemBlueprint after creating the ItemBluePrint class.
    private static final HashMap<String, ItemPreset> ITEM_BLUEPRINTS = new HashMap<String, ItemPreset>();

    static void loadGameData() {
        loadItemBlueprints();
        loadCreatureBlueprints();
        LOCATION_PRESETS = loadLocationPresets();
    }

    private static LocationPreset[] loadLocationPresets() {
        ArrayList<LocationPreset> locationPresets = new ArrayList<LocationPreset>();

        String[] clearingCreatures = {"BAT"};
        ItemPreset[] clearingItems = {ItemPreset.CHERRY};
        locationPresets.add(new LocationPreset("Clearing", 0.9, clearingCreatures, clearingItems));

        String[] desertCreatures = {"BAT"};
        ItemPreset[] desertItems = {};
        locationPresets.add(new LocationPreset("Desert", 1.0, desertCreatures, desertItems));

        String[] forestCreatures = {"BAT"};
        ItemPreset[] forestItems = {ItemPreset.APPLE, ItemPreset.CLOCK};
        locationPresets.add(new LocationPreset("Forest", 0.7, forestCreatures, forestItems));

        String[] graveyardCreatures = {"BAT"};
        ItemPreset[] graveyardItems = {};
        locationPresets.add(new LocationPreset("Graveyard", 0.7, graveyardCreatures, graveyardItems));

        String[] meadowCreatures = {"BAT"};
        ItemPreset[] meadowItems = {ItemPreset.LONGSWORD, ItemPreset.STICK};
        locationPresets.add(new LocationPreset("Meadow", 1.0, meadowCreatures, meadowItems));

        String[] pondCreatures = {"BAT"};
        ItemPreset[] pondItems = {ItemPreset.WATERMELON};
        locationPresets.add(new LocationPreset("Pond", 1.0, pondCreatures, pondItems));

        LocationPreset[] locationPresetsArray = new LocationPreset[locationPresets.size()];
        locationPresets.toArray(locationPresetsArray);
        return locationPresetsArray;
    }

    private static void loadCreatureBlueprints() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        BufferedReader br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("res/creatures.dscript")));
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

    private static void loadItemBlueprints() {
    }

}
