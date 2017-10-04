package org.mafagafogigante.dungeon.entity.creatures;

import java.util.Collection;

/**
 * A factory of CreaturePresets.
 */
public interface CreaturePresetFactory {
  Collection<CreaturePreset> getCreaturePresets();
}
