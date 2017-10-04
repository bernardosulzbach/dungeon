package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.Weight;

interface LimitedInventory {

  int getItemLimit();

  Weight getWeightLimit();

}
