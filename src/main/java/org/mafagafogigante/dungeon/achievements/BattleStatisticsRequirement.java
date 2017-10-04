package org.mafagafogigante.dungeon.achievements;

import org.jetbrains.annotations.NotNull;

/**
 * The statistical battle-related requirements for the unlock of an achievement.
 */
public class BattleStatisticsRequirement {

  private final BattleStatisticsQuery query;
  private final int count;

  /**
   * Constructs a new BattleStatisticsRequirement.
   *
   * @param query the query, not null
   * @param count the minimum amount of count, a positive integer
   */
  public BattleStatisticsRequirement(@NotNull BattleStatisticsQuery query, int count) {
    if (count < 1) {
      throw new IllegalArgumentException("count must be positive.");
    }
    this.query = query;
    this.count = count;
  }

  public BattleStatisticsQuery getQuery() {
    return query;
  }

  public int getCount() {
    return count;
  }

  @Override
  public String toString() {
    return String.format("BattleStatisticsRequirement{query=%s, count=%d}", query, count);
  }

}
