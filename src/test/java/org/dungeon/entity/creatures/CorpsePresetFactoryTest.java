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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.dungeon.entity.TagSet;
import org.dungeon.entity.creatures.Creature.Tag;
import org.dungeon.entity.items.ItemBlueprint;
import org.dungeon.game.ID;
import org.dungeon.game.Name;

import org.junit.Test;

public class CorpsePresetFactoryTest {

  @Test
  public void testMakeCorpseBlueprint() throws Exception {
    CreaturePreset preset = new CreaturePreset();
    preset.setID(new ID("TESTER"));
    preset.setType("Tester");
    preset.setName(Name.newInstance("Tester"));
    preset.setHealth(50);
    TagSet<Tag> tagSet = TagSet.makeEmptyTagSet(Creature.Tag.class);
    tagSet.addTag(Creature.Tag.CORPSE);
    preset.setTagSet(tagSet);
    ItemBlueprint blueprint = CorpsePresetFactory.makeCorpseBlueprint(preset);
    assertEquals(new ID("TESTER_CORPSE"), blueprint.getID());
    assertEquals("CORPSE", blueprint.getType());
    assertEquals(Name.newInstance("Tester Corpse"), blueprint.getName());
    assertTrue(blueprint.getMaxIntegrity() > 0);
    assertTrue(blueprint.getCurIntegrity() > 0);
    assertTrue(blueprint.getIntegrityDecrementOnHit() > 0);
    // Extreme cases.
    preset.setHealth(1);
    blueprint = CorpsePresetFactory.makeCorpseBlueprint(preset);
    assertTrue(blueprint.getMaxIntegrity() > 0);
    assertTrue(blueprint.getCurIntegrity() > 0);
  }

}