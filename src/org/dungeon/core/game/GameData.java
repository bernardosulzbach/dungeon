package org.dungeon.core.game;

import org.dungeon.core.creatures.CreaturePreset;
import org.dungeon.core.items.ItemPreset;

import java.util.ArrayList;

/**
 * The class that stores all the game data that is loaded and not serialized.
 * <p/>
 * Created by Bernardo on 22/10/2014.
 */
public class GameData {

    public static LocationPreset[] LOCATION_PRESETS;
    public static CreaturePreset[] CREATURE_PRESETS;
    public static ItemPreset[] ITEM_PRESETS;

    public static void loadGameData() {
        LOCATION_PRESETS = loadLocationPresets();
        CREATURE_PRESETS = loadCreaturePresets();
        ITEM_PRESETS = loadItemPresets();
    }

    private static LocationPreset[] loadLocationPresets() {
        ArrayList<LocationPreset> locationPresets = new ArrayList<LocationPreset>();

        CreaturePreset[] clearingCreatures = {CreaturePreset.FROG, CreaturePreset.RABBIT, CreaturePreset.SPIDER};
        ItemPreset[] clearingItems = {ItemPreset.CHERRY};
        locationPresets.add(new LocationPreset("Clearing", 0.9, clearingCreatures, clearingItems));

        CreaturePreset[] desertCreatures = {CreaturePreset.RAT, CreaturePreset.RAT, CreaturePreset.WOLF};
        ItemPreset[] desertItems = {};
        locationPresets.add(new LocationPreset("Desert", 1.0, desertCreatures, desertItems));

        CreaturePreset[] forestCreatures = {CreaturePreset.RABBIT, CreaturePreset.FROG, CreaturePreset.BEAR};
        ItemPreset[] forestItems = {ItemPreset.APPLE, ItemPreset.CLOCK};
        locationPresets.add(new LocationPreset("Forest", 0.7, forestCreatures, forestItems));

        CreaturePreset[] graveyardCreatures = {CreaturePreset.SKELETON, CreaturePreset.RAT, CreaturePreset.ZOMBIE};
        ItemPreset[] graveyardItems = {};
        locationPresets.add(new LocationPreset("Graveyard", 0.7, graveyardCreatures, graveyardItems));

        CreaturePreset[] meadowCreatures = {CreaturePreset.SPIDER, CreaturePreset.SNAKE, CreaturePreset.FROG};
        ItemPreset[] meadowItems = {ItemPreset.LONGSWORD, ItemPreset.STICK};
        locationPresets.add(new LocationPreset("Meadow", 1.0, meadowCreatures, meadowItems));

        CreaturePreset[] pondCreatures = {CreaturePreset.SNAKE, CreaturePreset.SPIDER};
        ItemPreset[] pondItems = {ItemPreset.WATERMELON};
        locationPresets.add(new LocationPreset("Pond", 1.0, pondCreatures, pondItems));

        LocationPreset[] locationPresetsArray = new LocationPreset[locationPresets.size()];
        locationPresets.toArray(locationPresetsArray);
        return locationPresetsArray;
    }

    private static CreaturePreset[] loadCreaturePresets() {
        ArrayList<CreaturePreset> creaturePresets = new ArrayList<CreaturePreset>();

        CreaturePreset[] creaturePresetsArray = new CreaturePreset[creaturePresets.size()];
        return creaturePresetsArray;
    }

    private static ItemPreset[] loadItemPresets() {
        ArrayList<ItemPreset> itemPresets = new ArrayList<ItemPreset>();

        ItemPreset[] itemPresetsArray = new ItemPreset[itemPresets.size()];
        return itemPresetsArray;
    }

}
