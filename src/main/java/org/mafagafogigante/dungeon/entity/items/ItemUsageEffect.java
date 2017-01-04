package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

public class ItemUsageEffect implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final int healing;

  public ItemUsageEffect(int healing) {
    this.healing = healing;
  }

  public int getHealing() {
    return healing;
  }

}
