package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.Effect;
import org.mafagafogigante.dungeon.io.Version;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class DrinkableComponent implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final List<Effect> effects;
  private final int integrityDecrementPerDose;
  private int doses;

  DrinkableComponent(List<Effect> effects, int integrityDecrementPerDose, int doses) {
    this.effects = effects;
    this.integrityDecrementPerDose = integrityDecrementPerDose;
    this.doses = doses;
  }

  /**
   * Affect a creature with a dose. This automatically decrements the number of remaining doses.
   */
  public void affect(@NotNull Creature creature) {
    if (isDepleted()) {
      throw new IllegalStateException("attempted to drink a depleted item");
    }
    doses--;
    for (Effect effect : effects) {
      effect.affect(creature);
    }
  }

  public boolean isDepleted() {
    return doses == 0;
  }

  int getIntegrityDecrementPerDose() {
    return integrityDecrementPerDose;
  }

}
