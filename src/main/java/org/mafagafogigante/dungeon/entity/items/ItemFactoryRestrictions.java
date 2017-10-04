package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;

import org.jetbrains.annotations.NotNull;

/**
 * Restrictions imposed upon an ItemFactory.
 *
 * <p>It is of the ItemFactory discretion to call {@code registerItem()} on an ItemFactoryRestriction.
 */
interface ItemFactoryRestrictions {

  /**
   * Evaluates if an item with the provided Id may be made.
   */
  boolean canMakeItem(@NotNull Id id);

  /**
   * Registers a new Item made by the ItemFactory.
   */
  void registerItem(@NotNull Id id);

}
