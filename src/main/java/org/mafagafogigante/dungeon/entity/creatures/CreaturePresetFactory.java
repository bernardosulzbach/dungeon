package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.items.ItemPreset;

import java.util.Collection;

/**
 * A factory of CreaturePresets.
 */
public interface CreaturePresetFactory {
  Collection<CreaturePreset> getCreaturePresets();
}
