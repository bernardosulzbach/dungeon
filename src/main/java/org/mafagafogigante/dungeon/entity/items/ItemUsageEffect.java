package org.mafagafogigante.dungeon.entity.items;

import java.io.Serializable;

public class ItemUsageEffect implements Serializable {

  private static final long serialVersionUID = 6L;
  private final int healing;

  public ItemUsageEffect(int healing) {
    this.healing = healing;
  }

  public int getHealing() {
    return healing;
  }

}
