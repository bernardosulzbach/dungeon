package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.io.Version;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of uniqueness restrictions regarding the items produced by a factory.
 */
class UniquenessRestrictions implements ItemFactoryRestrictions, Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Set<Id> uniqueIds;
  private final Set<Id> alreadyCreatedUniqueIds = new HashSet<>();

  public UniquenessRestrictions(Collection<Id> uniqueIds) {
    this.uniqueIds = new HashSet<>(uniqueIds);
  }

  @Override
  public boolean canMakeItem(@NotNull Id id) {
    return !alreadyCreatedUniqueIds.contains(id);
  }

  @Override
  public void registerItem(@NotNull Id id) {
    if (uniqueIds.contains(id)) {
      if (alreadyCreatedUniqueIds.contains(id)) {
        throw new IllegalStateException("Created a unique item again");
      }
      alreadyCreatedUniqueIds.add(id);
    }
  }

}
