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
import org.dungeon.entity.items.ItemPreset;
import org.dungeon.game.Id;
import org.dungeon.game.NameFactory;

import org.junit.Test;

public class CorpsePresetFactoryTest {

  @Test
  public void testMakeCorpsePreset() throws Exception {
    CreaturePreset creaturePreset = new CreaturePreset();
    creaturePreset.setId(new Id("TESTER"));
    creaturePreset.setType("Tester");
    creaturePreset.setName(NameFactory.newInstance("Tester"));
    creaturePreset.setHealth(50);
    TagSet<Tag> tagSet = TagSet.makeEmptyTagSet(Creature.Tag.class);
    tagSet.addTag(Creature.Tag.CORPSE);
    creaturePreset.setTagSet(tagSet);
    ItemPreset corpsePreset = CorpsePresetFactory.makeCorpsePreset(creaturePreset);
    assertEquals(new Id("TESTER_CORPSE"), corpsePreset.getId());
    assertEquals("CORPSE", corpsePreset.getType());
    assertEquals(NameFactory.newInstance("Tester Corpse"), corpsePreset.getName());
    assertTrue(corpsePreset.getIntegrity().getMaximum() > 0);
    assertTrue(corpsePreset.getIntegrity().getCurrent() > 0);
    assertTrue(corpsePreset.getIntegrityDecrementOnHit() > 0);
    // Extreme cases.
    creaturePreset.setHealth(1);
    corpsePreset = CorpsePresetFactory.makeCorpsePreset(creaturePreset);
    assertTrue(corpsePreset.getIntegrity().getMaximum() > 0);
    assertTrue(corpsePreset.getIntegrity().getCurrent() > 0);
  }

}