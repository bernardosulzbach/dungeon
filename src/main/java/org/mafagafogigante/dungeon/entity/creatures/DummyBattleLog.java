package org.mafagafogigante.dungeon.entity.creatures;

import java.io.Serializable;

/**
 * A dummy implementation of the BattleLog interface.
 */
public class DummyBattleLog implements BattleLog, Serializable {

  private static final DummyBattleLog INSTANCE = new DummyBattleLog();

  // Do not allow instantiation by other classes.
  private DummyBattleLog() {

  }

  public static DummyBattleLog getInstance() {
    return INSTANCE;
  }

  @Override
  public void incrementInflicted(long damage) {
  }

  @Override
  public void incrementTaken(long damage) {
  }

  @Override
  public long getAndResetInflicted() {
    return 0;
  }

  @Override
  public long getAndResetTaken() {
    return 0;
  }

  @Override
  public String toString() {
    return "DummyBattleLog";
  }

}
