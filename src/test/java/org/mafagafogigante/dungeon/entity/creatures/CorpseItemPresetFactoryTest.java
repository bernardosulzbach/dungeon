package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.creatures.Creature.Tag;
import org.mafagafogigante.dungeon.entity.items.ItemPreset;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.NameFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

public class CorpseItemPresetFactoryTest {

  @Test
  public void testCorpseItemPresetFactoryShouldProduceCorpsesForCreaturePresetsWithTheCorpseTag() {
    final CreaturePreset creaturePreset = new CreaturePreset();
    creaturePreset.setId(new Id("TESTER"));
    creaturePreset.setType("Tester");
    creaturePreset.setName(NameFactory.newInstance("Tester"));
    creaturePreset.setHealth(10);
    TagSet<Tag> tagSet = TagSet.makeEmptyTagSet(Creature.Tag.class);
    tagSet.addTag(Creature.Tag.CORPSE);
    creaturePreset.setTagSet(tagSet);
    CreatureFactory creatureFactory = new CreatureFactory(new CreaturePresetFactory() {
      @Override
      public Collection<CreaturePreset> getCreaturePresets() {
        return Collections.singleton(creaturePreset);
      }
    });
    Assert.assertNotNull(new CorpseItemPresetFactory(creatureFactory).getItemPresets().iterator().next());
  }

  @Test
  public void testCorpseItemPresetFactoryShouldNotProduceCorpsesForCreaturePresetsWithoutTheCorpseTag() {
    final CreaturePreset creaturePreset = new CreaturePreset();
    creaturePreset.setId(new Id("TESTER"));
    creaturePreset.setType("Tester");
    creaturePreset.setName(NameFactory.newInstance("Tester"));
    creaturePreset.setHealth(10);
    CreatureFactory creatureFactory = new CreatureFactory(new CreaturePresetFactory() {
      @Override
      public Collection<CreaturePreset> getCreaturePresets() {
        return Collections.singleton(creaturePreset);
      }
    });
    Assert.assertTrue(new CorpseItemPresetFactory(creatureFactory).getItemPresets().isEmpty());
  }

  @Test
  public void testCorpseItemPresetFactoryShouldProduceValidCorpsePresets() {
    final CreaturePreset creaturePreset = new CreaturePreset();
    creaturePreset.setId(new Id("TESTER"));
    creaturePreset.setType("Tester");
    creaturePreset.setName(NameFactory.newInstance("Tester"));
    creaturePreset.setHealth(10);
    TagSet<Tag> tagSet = TagSet.makeEmptyTagSet(Creature.Tag.class);
    tagSet.addTag(Creature.Tag.CORPSE);
    creaturePreset.setTagSet(tagSet);
    CreatureFactory creatureFactory = new CreatureFactory(new CreaturePresetFactory() {
      @Override
      public Collection<CreaturePreset> getCreaturePresets() {
        return Collections.singleton(creaturePreset);
      }
    });
    ItemPreset corpsePreset = new CorpseItemPresetFactory(creatureFactory).getItemPresets().iterator().next();
    Assert.assertEquals(new Id("TESTER_CORPSE"), corpsePreset.getId());
    Assert.assertEquals("CORPSE", corpsePreset.getType());
    Assert.assertEquals(NameFactory.newInstance("Tester Corpse"), corpsePreset.getName());
    Assert.assertTrue(corpsePreset.getIntegrity().getMaximum() > 0);
    Assert.assertTrue(corpsePreset.getIntegrity().getCurrent() > 0);
    Assert.assertTrue(corpsePreset.getIntegrityDecrementOnHit() > 0);
  }

  @Test
  public void testCorpseItemPresetFactoryShouldProduceValidCorpsePresetsForExtremeCases() {
    final CreaturePreset creaturePreset = new CreaturePreset();
    creaturePreset.setId(new Id("TESTER"));
    creaturePreset.setType("Tester");
    creaturePreset.setName(NameFactory.newInstance("Tester"));
    creaturePreset.setHealth(1); // Extreme case that may bring up rounding issues.
    TagSet<Tag> tagSet = TagSet.makeEmptyTagSet(Creature.Tag.class);
    tagSet.addTag(Creature.Tag.CORPSE);
    creaturePreset.setTagSet(tagSet);
    CreatureFactory creatureFactory = new CreatureFactory(new CreaturePresetFactory() {
      @Override
      public Collection<CreaturePreset> getCreaturePresets() {
        return Collections.singleton(creaturePreset);
      }
    });
    ItemPreset corpsePreset = new CorpseItemPresetFactory(creatureFactory).getItemPresets().iterator().next();
    Assert.assertEquals(new Id("TESTER_CORPSE"), corpsePreset.getId());
    Assert.assertEquals("CORPSE", corpsePreset.getType());
    Assert.assertEquals(NameFactory.newInstance("Tester Corpse"), corpsePreset.getName());
    Assert.assertTrue(corpsePreset.getIntegrity().getMaximum() > 0);
    Assert.assertTrue(corpsePreset.getIntegrity().getCurrent() > 0);
    Assert.assertTrue(corpsePreset.getIntegrityDecrementOnHit() > 0);
  }

}
