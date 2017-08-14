package org.mafagafogigante.dungeon.entity.creatures;

import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.DAY;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.SECOND;

import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.entity.items.ItemPreset;
import org.mafagafogigante.dungeon.entity.items.ItemPresetFactory;
import org.mafagafogigante.dungeon.entity.items.Rarity;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A factory of ItemPreset for corpses.
 */
public final class CorpseItemPresetFactory implements ItemPresetFactory {

  private static final Rarity CORPSE_RARITY = Rarity.POOR;
  private static final int CORPSE_DAMAGE = 2;
  private static final int CORPSE_INTEGRITY_DECREMENT_ON_HIT = 5;
  private static final long CORPSE_PUTREFACTION_PERIOD = DAY.as(SECOND);
  private static final Percentage CORPSE_HIT_RATE = new Percentage(0.5);
  private final CreatureFactory creatureFactory;

  /**
   * Creates a CorpseItemPresetFactory from the specified CreatureFactory.
   */
  public CorpseItemPresetFactory(CreatureFactory creatureFactory) {
    this.creatureFactory = creatureFactory;
  }

  /**
   * Given a Creature ID, this method returns the corresponding corpse's ID.
   */
  public static Id makeCorpseIdFromCreatureId(Id id) {
    return new Id(id + "_CORPSE");
  }

  /**
   * Makes all CorpsePresets that this CorpseItemPresetFactory should make.
   */
  @Override
  public Collection<ItemPreset> getItemPresets() {
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
    corpse.setId(makeCorpseIdFromCreatureId(preset.getId()));
    corpse.setType("CORPSE");
    corpse.setName(NameFactory.newCorpseName(preset.getName()));
    corpse.setRarity(CORPSE_RARITY);
    corpse.setWeight(preset.getWeight());
    corpse.setPutrefactionPeriod(CORPSE_PUTREFACTION_PERIOD);
    corpse.setIntegrity(makeCorpseIntegrity(preset));
    corpse.setVisibility(preset.getVisibility());
    corpse.setLuminosity(preset.getLuminosity());
    corpse.setHitRate(CORPSE_HIT_RATE);
    corpse.setIntegrityDecrementOnHit(CORPSE_INTEGRITY_DECREMENT_ON_HIT);
    corpse.setDamage(CORPSE_DAMAGE);
    corpse.getTagSet().addTag(Item.Tag.WEAPON);
    corpse.getTagSet().addTag(Item.Tag.WEIGHT_PROPORTIONAL_TO_INTEGRITY);
    corpse.getTagSet().addTag(Item.Tag.DECOMPOSES);
    return corpse;
  }

  private Integrity makeCorpseIntegrity(@NotNull final CreaturePreset preset) {
    // The health of the preset over two rounded up.
    final int integrity = (int) Math.ceil(preset.getHealth() / 2.0);
    return new Integrity(integrity, integrity);
  }

}
