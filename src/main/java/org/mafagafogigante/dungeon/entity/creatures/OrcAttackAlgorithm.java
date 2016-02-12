package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.util.DungeonMath;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

/**
 * An implementation of AttackAlgorithm that takes into account the increased brutality of Orcs when endangered.
 *
 * <p>The critical chance increases as the creature gets closer to dying.
 */
class OrcAttackAlgorithm extends SimpleAttackAlgorithm {

  private static final double MIN_CRITICAL_CHANCE = 0.1;
  private static final double MAX_CRITICAL_CHANCE = 0.5;

  @Override
  Percentage getCriticalChance(@NotNull Creature creature) {
    Percentage healthiness = creature.getHealth().toPercentage();
    return new Percentage(DungeonMath.weightedAverage(MAX_CRITICAL_CHANCE, MIN_CRITICAL_CHANCE, healthiness));
  }

}
