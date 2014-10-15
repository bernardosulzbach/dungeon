package org.dungeon.core.game;

import org.dungeon.core.creatures.CreaturePreset;
import org.dungeon.core.items.ItemPreset;

import java.io.Serializable;

public class LocationPreset implements Serializable {

    private final String name;
    private final double lightPermittivity;
    private final CreaturePreset[] creatures;
    private final ItemPreset[] items;

    public LocationPreset(String name, double lightPermittivity, CreaturePreset[] creatures, ItemPreset[] items) {
        this.name = name;
        this.lightPermittivity = lightPermittivity;
        this.creatures = creatures;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public double getLightPermittivity() {
        return lightPermittivity;
    }

    public CreaturePreset[] getCreatures() {
        return creatures;
    }

    public ItemPreset[] getItems() {
        return items;
    }
}
