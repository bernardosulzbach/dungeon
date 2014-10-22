package org.dungeon.core.game;

import org.dungeon.core.creatures.CreaturePreset;
import org.dungeon.core.items.ItemPreset;

import java.util.ArrayList;

/**
 * The class that stores all the game data that is loaded and not serialized.
 *
 * Created by Bernardo on 22/10/2014.
 */
public class GameData {

    public static LocationPreset[] LOCATION_PRESETS;
    public static CreaturePreset[] CREATURE_PRESETS;
    public static ItemPreset[] ITEM_PRESETS;

    public static void load() {
        LOCATION_PRESETS = loadLocationPresets();
        loadCreaturePresets();
        loadItemPresets();
    }

    private static LocationPreset[] loadLocationPresets() {
        ArrayList<LocationPreset> locationPresets = new ArrayList<LocationPreset>();
        CreaturePreset[] forestCreatures = {CreaturePreset.RABBIT};
        ItemPreset[] forestItems = {ItemPreset.APPLE, ItemPreset.CLOCK};
        locationPresets.add(new LocationPreset("Forest", 0.8, forestCreatures, forestItems));
        LocationPreset[] locationPresetsArray = new LocationPreset[locationPresets.size()];
        locationPresets.toArray(locationPresetsArray);
        return locationPresetsArray;
    }

    private static void loadCreaturePresets() {

    }

    private static void loadItemPresets() {

    }

}
