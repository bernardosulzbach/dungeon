package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

/**
 * The CreatureInventory class.
 */
public class CreatureInventory extends BaseInventory implements LimitedInventory {

  private static final long serialVersionUID = Version.MAJOR;
  private final Creature owner;
  private final int itemLimit;
  private final Weight weightLimit;

  /**
   * Constructs a new CreatureInventory.
   */
  public CreatureInventory(Creature owner, int itemLimit, double weightLimit) {
    this.owner = owner;
    this.itemLimit = itemLimit;
    this.weightLimit = Weight.newInstance(weightLimit);
  }

  @Override
  public int getItemLimit() {
    return itemLimit;
  }

  @Override
  public Weight getWeightLimit() {
    return weightLimit;
  }

  /**
   * Retrieves the sum of the weights of the individual items on this inventory.
   */
  public Weight getWeight() {
    Weight sum = Weight.ZERO;
    for (Item item : getItems()) {
      sum = sum.add(item.getWeight());
    }
    return sum;
  }

  /**
   * Attempts to add an Item to this Inventory. As a precondition, simulateItemAddition should return SUCCESSFUL.
   *
   * @param item the Item to be added, not null
   */
  public void addItem(Item item) {
    if (simulateItemAddition(item) == SimulationResult.SUCCESSFUL) {
      items.add(item);
      item.setInventory(this);
      String format = "Added %s to the inventory of %s.";
      DungeonLogger.fine(String.format(format, item.getQualifiedName(), owner));
    } else {
      throw new IllegalStateException("simulateItemAddition did not return SimulationResult.SUCCESSFUL.");
    }
  }

  /**
   * Simulates the addition of an Item to this CreatureInventory and returns the result.
   *
   * @param item the Item to be added, not null
   * @return a SimulationResult value
   */
  public SimulationResult simulateItemAddition(Item item) {
    if (hasItem(item)) { // Check that the new item is not already in the inventory.
      DungeonLogger.warning("Tried to add an item to a CreatureInventory that already has it.");
      return SimulationResult.ALREADY_IN_THE_INVENTORY;
    }
    if (isFull()) {
      return SimulationResult.AMOUNT_LIMIT;
    } else if (willExceedWeightLimitAfterAdding(item)) {
      return SimulationResult.WEIGHT_LIMIT;
    } else {
      return SimulationResult.SUCCESSFUL;
    }
  }

  private boolean isFull() {
    return getItemCount() == getItemLimit();
  }

  private boolean willExceedWeightLimitAfterAdding(Item item) {
    return getWeight().add(item.getWeight()).compareTo(getWeightLimit()) > 0;
  }

  /**
   * Removes an Item from the CreatureInventory, unequipping it if it is the currently equipped weapon.
   *
   * @param item the Item to be removed from the CreatureInventory
   */
  public void removeItem(Item item) {
    if (owner.getWeapon() == item) {
      owner.unsetWeapon();
    }
    items.remove(item);
    item.setInventory(null);
    String format = "Removed %s from the inventory of %s.";
    DungeonLogger.fine(String.format(format, item.getQualifiedName(), owner));
  }

  public enum SimulationResult {ALREADY_IN_THE_INVENTORY, AMOUNT_LIMIT, WEIGHT_LIMIT, SUCCESSFUL}

}
