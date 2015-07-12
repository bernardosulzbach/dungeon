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

package org.dungeon.entity.creatures;

import static org.dungeon.date.DungeonTimeUnit.DAY;
import static org.dungeon.date.DungeonTimeUnit.SECOND;

import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemBlueprint;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.game.Name;

/**
 * A factory of corpse presets.
 */
public final class CorpsePresetFactory {

  private static final int CORPSE_DAMAGE = 2;
  private static final int CORPSE_INTEGRITY_DECREMENT_ON_HIT = 5;
  private static final long CORPSE_PUTREFACTION_PERIOD = DAY.as(SECOND);
  private static final double CORPSE_HIT_RATE = 0.5;

  private CorpsePresetFactory() {
    throw new AssertionError();
  }

  /**
   * Makes a corpse preset from a creature preset.
   */
  public static ItemBlueprint makeCorpseBlueprint(CreaturePreset preset) {
    if (!preset.hasTag(Creature.Tag.CORPSE)) {
      throw new IllegalArgumentException("preset does not have the CORPSE tag.");
    }
    ItemBlueprint corpse = new ItemBlueprint();
    corpse.setID(ItemFactory.makeCorpseIDFromCreatureID(preset.getID()));
    corpse.setType("CORPSE");
    corpse.setName(Name.newCorpseName(preset.getName()));
    corpse.setWeight(preset.getWeight());
    corpse.setPutrefactionPeriod(CORPSE_PUTREFACTION_PERIOD);
    int integrity = (int) Math.ceil(preset.getHealth() / (double) 2); // The health of the preset over two rounded up.
    corpse.setMaxIntegrity(integrity);
    corpse.setCurIntegrity(integrity);
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
