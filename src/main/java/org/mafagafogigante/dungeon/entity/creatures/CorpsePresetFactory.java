/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.entity.creatures;

import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.DAY;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.SECOND;

import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.entity.items.ItemFactory;
import org.mafagafogigante.dungeon.entity.items.ItemPreset;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.util.Percentage;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory of corpse presets.
 */
public final class CorpsePresetFactory {

  private static final int CORPSE_DAMAGE = 2;
  private static final int CORPSE_INTEGRITY_DECREMENT_ON_HIT = 5;
  private static final long CORPSE_PUTREFACTION_PERIOD = DAY.as(SECOND);
  private static final Percentage CORPSE_HIT_RATE = new Percentage(0.5);

  private final CreatureFactory creatureFactory;

  /**
   * Creates a CorpsePresetFactory from the specified CreatureFactory.
   */
  public CorpsePresetFactory(CreatureFactory creatureFactory) {
    this.creatureFactory = creatureFactory;
  }

  /**
   * Makes all CorpsePresets that this CorpsePresetFactory should make.
   */
  public List<ItemPreset> makeCorpsePresets() {
    List<ItemPreset> itemPresets = new ArrayList<>();
    for (CreaturePreset creaturePreset : creatureFactory.getPresets()) {
      if (creaturePreset.hasTag(Creature.Tag.CORPSE)) {
        itemPresets.add(makeCorpsePreset(creaturePreset));
      }
    }
    return itemPresets;
  }

  /**
   * Makes a corpse preset from a creature preset.
   */
  private ItemPreset makeCorpsePreset(CreaturePreset preset) {
    if (!preset.hasTag(Creature.Tag.CORPSE)) {
      throw new IllegalArgumentException("preset does not have the CORPSE tag.");
    }
    ItemPreset corpse = new ItemPreset();
    corpse.setId(ItemFactory.makeCorpseIdFromCreatureId(preset.getId()));
    corpse.setType("CORPSE");
    corpse.setName(NameFactory.newCorpseName(preset.getName()));
    corpse.setWeight(preset.getWeight());
    corpse.setPutrefactionPeriod(CORPSE_PUTREFACTION_PERIOD);
    int integrity = (int) Math.ceil(preset.getHealth() / (double) 2); // The health of the preset over two rounded up.
    corpse.setIntegrity(new Integrity(integrity, integrity));
    corpse.setVisibility(preset.getVisibility());
    corpse.setLuminosity(preset.getLuminosity());
    corpse.setHitRate(CORPSE_HIT_RATE);
    corpse.setIntegrityDecrementOnHit(CORPSE_INTEGRITY_DECREMENT_ON_HIT);
    corpse.setDamage(CORPSE_DAMAGE);
    corpse.addTag(Item.Tag.WEAPON);
    corpse.addTag(Item.Tag.WEIGHT_PROPORTIONAL_TO_INTEGRITY);
    corpse.addTag(Item.Tag.DECOMPOSES);
    return corpse;
  }

}
