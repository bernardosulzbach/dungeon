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

import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.entity.items.ItemFactory;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a component that enables item dropping once in a lifetime of a Creature.
 */
class Dropper implements Serializable {

  private final Creature creature;
  private final List<Drop> dropList;
  private final List<Item> droppedItemsList = new ArrayList<>();
  private boolean hasAlreadyCalledDropEverything = false;

  public Dropper(Creature creature, List<Drop> dropList) {
    this.creature = creature;
    this.dropList = dropList;
  }

  @NotNull
  List<Item> getDroppedItemsList() {
    return droppedItemsList;
  }

  /**
   * Drops everything that is in the Creature's inventory on the ground. Also rolls for each drop law and creates the
   * items that must be created.
   */
  public void dropEverything() {
    if (!hasAlreadyCalledDropEverything) {
      hasAlreadyCalledDropEverything = true;
      dropInventory();
      dropVariableDrops();
    } else {
      DungeonLogger.warning("Called Dropper.dropEverything more than once in " + toString() + ". Ignored this call.");
    }
  }

  private void dropInventory() {
    for (Item item : new ArrayList<>(creature.getInventory().getItems())) {
      creature.dropItem(item);
      getDroppedItemsList().add(item);
    }
  }

  private void dropVariableDrops() {
    for (Drop drop : dropList) {
      if (drop.rollForDrop()) {
        Item item = ItemFactory.makeItem(drop.getItemId(), creature.getLocation().getWorld().getWorldDate());
        if (item != null) {
          creature.getLocation().addItem(item);
          getDroppedItemsList().add(item);
        } else {
          DungeonLogger.warning("Got invalid Id (" + drop.getItemId() + ") from drop law.");
        }
      }
    }
  }

  @Override
  public String toString() {
    return "Dropper{" +
        "creature=" + creature +
        ", dropList=" + dropList +
        ", droppedItemsList=" + droppedItemsList +
        '}';
  }

}
