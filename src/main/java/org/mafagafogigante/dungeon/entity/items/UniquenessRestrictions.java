/*
 * Copyright (C) 2016 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of uniqueness restrictions regarding the items produced by a factory.
 */
class UniquenessRestrictions implements ItemFactoryRestrictions, Serializable {

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
