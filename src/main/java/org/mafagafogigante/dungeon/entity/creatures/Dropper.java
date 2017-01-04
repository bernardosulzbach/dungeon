package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a component that enables item dropping once in a lifetime of a Creature.
 */
class Dropper implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
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
    final World world = creature.getLocation().getWorld();
    for (Drop drop : dropList) {
      if (drop.rollForDrop()) {
        if (world.getItemFactory().canMakeItem(drop.getItemId())) {
          Item item = world.getItemFactory().makeItem(drop.getItemId(), world.getWorldDate());
          creature.getLocation().addItem(item);
          getDroppedItemsList().add(item);
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
