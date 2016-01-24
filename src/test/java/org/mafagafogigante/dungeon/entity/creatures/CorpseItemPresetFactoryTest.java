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

import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.creatures.Creature.Tag;
import org.mafagafogigante.dungeon.entity.items.ItemPreset;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.NameFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class CorpseItemPresetFactoryTest {

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
    CreatureFactory creatureFactory = new CreatureFactory(Collections.singleton(creaturePreset));
    ItemPreset corpsePreset = new CorpseItemPresetFactory(creatureFactory).getItemPresets().iterator().next();
    Assert.assertEquals(new Id("TESTER_CORPSE"), corpsePreset.getId());
    Assert.assertEquals("CORPSE", corpsePreset.getType());
    Assert.assertEquals(NameFactory.newInstance("Tester Corpse"), corpsePreset.getName());
    Assert.assertTrue(corpsePreset.getIntegrity().getMaximum() > 0);
    Assert.assertTrue(corpsePreset.getIntegrity().getCurrent() > 0);
    Assert.assertTrue(corpsePreset.getIntegrityDecrementOnHit() > 0);
    // Extreme cases.
    creaturePreset.setHealth(1);
    corpsePreset = new CorpseItemPresetFactory(creatureFactory).getItemPresets().iterator().next();
    Assert.assertTrue(corpsePreset.getIntegrity().getMaximum() > 0);
    Assert.assertTrue(corpsePreset.getIntegrity().getCurrent() > 0);
  }

}