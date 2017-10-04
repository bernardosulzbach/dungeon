package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.EnchantmentFactory;
import org.mafagafogigante.dungeon.entity.creatures.CorpseItemPresetFactory;
import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.io.DungeonResource;
import org.mafagafogigante.dungeon.io.ResourceNameResolver;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides methods to create different items for the game.
 */
public final class ItemFactory implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Map<Id, ItemPreset> itemPresets = new HashMap<>();
  private final EnchantmentFactory enchantmentFactory;
  private ItemFactoryRestrictions restrictions;

  // The flag that indicates we have already refreshed this ItemFactory with the resource files.
  // An ItemFactory is refreshed after construction, but not after deserialization.
  private transient boolean refreshed = true;

  /**
   * Constructs an ItemFactory from one or more ItemPresetFactories.
   */
  public ItemFactory(EnchantmentFactory enchantmentFactory, @NotNull ItemPresetFactory... itemPresetFactories) {
    this.enchantmentFactory = enchantmentFactory;
    for (ItemPresetFactory itemPresetFactory : itemPresetFactories) {
      addAllPresets(itemPresetFactory.getItemPresets());
    }
    createUniquenessRestrictions();
  }

  private Object readResolve() throws ObjectStreamException {
    refreshed = false;
    return this;
  }

  /**
   * Iterates over all presets of a Collection, adding them to this factory after they are validated.
   */
  private void addAllPresets(Collection<ItemPreset> presets) {
    for (ItemPreset preset : presets) {
      Id id = preset.getId();
      if (itemPresets.containsKey(id)) {
        throw new IllegalArgumentException("factory already contains a preset with the Id " + preset.getId() + ".");
      }
      itemPresets.put(id, preset);
    }
  }

  private void createUniquenessRestrictions() {
    Set<Id> uniqueIds = new HashSet<>();
    for (ItemPreset itemPreset : getItemPresets().values()) {
      if (itemPreset.isUnique()) {
        uniqueIds.add(itemPreset.getId());
      }
    }
    restrictions = new UniquenessRestrictions(uniqueIds);
  }

  /**
   * Returns an unmodifiable view of the ItemPreset map.
   */
  private Map<Id, ItemPreset> getItemPresets() {
    if (!refreshed) {
      refreshItemPresets();
    }
    return Collections.unmodifiableMap(itemPresets);
  }

  /**
   * Returns whether or not this ItemFactory can make an Item with the specified Id based on its restrictions.
   */
  public boolean canMakeItem(@NotNull Id id) {
    return restrictions.canMakeItem(id);
  }

  /**
   * Attempts to create an item from the ItemPreset specified by an ID with the provided creation date.
   *
   * <p>The caller should ensure that this ItemFactory can make this item after taking its restrictions into account by
   * calling canMakeItem with this Id.
   *
   * @param id the ID of the preset, not null
   * @param date the creation date of the item, not null
   * @return an Item with the specified creation date
   */
  public Item makeItem(@NotNull Id id, @NotNull Date date) {
    ItemPreset itemPreset = getItemPresets().get(id);
    if (itemPreset == null) {
      throw new IllegalArgumentException("id (" + id + ") does not correspond to an ItemPreset.");
    }
    Item item = new Item(itemPreset, date, enchantmentFactory);
    restrictions.registerItem(item.getId());
    return item;
  }

  private void refreshItemPresets() {
    DungeonLogger.info("Refreshing item presets.");
    String filename = ResourceNameResolver.resolveName(DungeonResource.ITEMS);
    ItemPresetFactory jsonItemPresetFactory = new JsonItemPresetFactory(filename);
    for (ItemPreset preset : jsonItemPresetFactory.getItemPresets()) {
      if (!itemPresets.containsKey(preset.getId())) {
        itemPresets.put(preset.getId(), preset);
      }
    }
    refreshed = true;
  }

  /**
   * Makes a corpse from the provided Creature. The creation Date of the corpse should be the Date of death.
   *
   * @param creature the Creature object
   * @param date the Date when the Creature died
   * @return an Item that represents the corpse of the Creature
   */
  public Item makeCorpse(Creature creature, Date date) {
    if (!creature.hasTag(Creature.Tag.CORPSE)) {
      throw new AssertionError("Called makeCorpse for Creature that does not have the CORPSE tag!");
    }
    // Corpses should never be restricted.
    return makeItem(CorpseItemPresetFactory.makeCorpseIdFromCreatureId(creature.getId()), date);
  }

}
