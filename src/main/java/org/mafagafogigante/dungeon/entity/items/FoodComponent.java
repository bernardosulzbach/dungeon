package org.mafagafogigante.dungeon.entity.items;

import java.io.Serializable;

/**
 * The food component of some items.
 */
public class FoodComponent implements Serializable {

  private final int nutrition;
  private final int integrityDecrementOnEat;

  public FoodComponent(int nutrition, int integrityDecrementOnEat) {
    this.nutrition = nutrition;
    this.integrityDecrementOnEat = integrityDecrementOnEat;
  }

  public int getNutrition() {
    return nutrition;
  }

  public int getIntegrityDecrementOnEat() {
    return integrityDecrementOnEat;
  }

}
