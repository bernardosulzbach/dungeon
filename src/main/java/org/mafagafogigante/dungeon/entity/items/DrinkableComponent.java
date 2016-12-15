package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import java.io.Serializable;

public class DrinkableComponent implements Serializable {

  private static final long serialVersionUID = 6L;
  private final ItemUsageEffect effect;
  private int doses;
  private int integrityDecrementPerDose;

  DrinkableComponent(ItemUsageEffect effect, int integrityDecrementPerDose, int doses) {
    this.effect = effect;
    this.integrityDecrementPerDose = integrityDecrementPerDose;
    this.doses = doses;
  }

  public ItemUsageEffect getEffect() {
    return effect;
  }

  public boolean isDepleted() {
    return doses == 0;
  }

  /**
   * Decrements the amount of doses left in this component.
   */
  public void decrementDoses() {
    if (isDepleted()) {
      DungeonLogger.warning("Attempted to decrement doses after depletion!");
    } else {
      doses--;
    }
  }

  int getIntegrityDecrementPerDose() {
    return integrityDecrementPerDose;
  }

}
