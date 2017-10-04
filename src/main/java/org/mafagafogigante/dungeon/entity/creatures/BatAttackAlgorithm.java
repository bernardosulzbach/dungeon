package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.util.DungeonMath;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

/**
 * The AttackAlgorithm bats that use echolocation should use.
 *
 * <p>Both the hit rate and the critical chance of bats are inversely proportional to the location's luminosity.
 */
class BatAttackAlgorithm extends SimpleAttackAlgorithm {

  private static final double BAT_MAX_HIT_RATE = 0.9;
  private static final double BAT_MIN_HIT_RATE = 0.1;

  private static final double BAT_MAX_CRITICAL_CHANCE = 0.8;
  private static final double BAT_MIN_CRITICAL_CHANCE = 0.0;

  @Override
  Percentage getHitRate(@NotNull Creature creature) {
    Percentage luminosity = creature.getLocation().getLuminosity().toPercentage();
    return new Percentage(DungeonMath.weightedAverage(BAT_MAX_HIT_RATE, BAT_MIN_HIT_RATE, luminosity));
  }

  @Override
  Percentage getCriticalChance(@NotNull Creature creature) {
    Percentage luminosity = creature.getLocation().getLuminosity().toPercentage();
    return new Percentage(DungeonMath.weightedAverage(BAT_MAX_CRITICAL_CHANCE, BAT_MIN_CRITICAL_CHANCE, luminosity));
  }

}
