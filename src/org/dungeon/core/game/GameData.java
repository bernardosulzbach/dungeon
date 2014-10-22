package org.dungeon.core.game;

import org.dungeon.core.creatures.CreaturePreset;
import org.dungeon.core.items.ItemPreset;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that stores all the game data that is loaded and not serialized.
 *
 * Created by Bernardo on 22/10/2014.
 */
public class GameData {

    public static final List<CreaturePreset> CREATURE_PRESETS = new ArrayList<CreaturePreset>();
    public static final List<ItemPreset> ITEM_PRESETS = new ArrayList<ItemPreset>();

    public static void initializeGameData() {
        initializeCreaturePresets();
        initializeItemPresets();
    }

    private static void initializeCreaturePresets() {

    }

    private static void initializeItemPresets() {

    }

}
