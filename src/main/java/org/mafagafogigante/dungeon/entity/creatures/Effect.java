package org.mafagafogigante.dungeon.entity.creatures;

import java.io.Serializable;

public abstract class Effect implements Serializable {

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    return getClass().getCanonicalName().equals(object.getClass().getCanonicalName());
  }

  @Override
  public int hashCode() {
    return getClass().getCanonicalName().hashCode();
  }

  /**
   * Affects a creature with this effect.
   *
   * <p>It is guaranteed that an effect cannot add multiple Condition objects of the same class to a creature.
   */
  public abstract void affect(Creature creature);

  /**
   * Returns the maximum number of simultaneous identical conditions that can be caused by this effect.
   *
   * <p>Zero indicates that there is no limit.
   */
  public int getMaximumStack() {
    return 0;
  }

}
