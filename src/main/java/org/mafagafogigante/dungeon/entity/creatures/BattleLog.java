package org.mafagafogigante.dungeon.entity.creatures;

public interface BattleLog {

  /**
   * Increments the inflicted damage by the specified amount.
   *
   * @param damage a nonnegative long
   */
  void incrementInflicted(long damage);

  /**
   * Increments the taken damage by the specified amount.
   *
   * @param damage a nonnegative long
   */
  void incrementTaken(long damage);

  /**
   * Gets and resets the inflicted damage.
   */
  long getAndResetInflicted();

  /**
   * Gets and resets the taken damage.
   */
  long getAndResetTaken();

}
