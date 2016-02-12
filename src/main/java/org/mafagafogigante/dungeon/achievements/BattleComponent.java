package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.stats.BattleStatistics;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The battle component of the achievements.
 */
final class BattleComponent {

  private final Collection<BattleStatisticsRequirement> requirements;

  BattleComponent(@NotNull Collection<BattleStatisticsRequirement> requirements) {
    this.requirements = requirements;
  }

  /**
   * Checks if this component of the Achievement is fulfilled or not.
   */
  public boolean isFulfilled(BattleStatistics battleStatistics) {
    for (BattleStatisticsRequirement requirement : requirements) {
      if (!battleStatistics.satisfies(requirement)) {
        return false;
      }
    }
    return true;
  }

}
