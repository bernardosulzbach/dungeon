package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * The simplest possible implementation of a BattleLog.
 */
public class SimpleBattleLog implements BattleLog, Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  private long inflicted;
  private long taken;

  @Override
  public void incrementInflicted(long damage) {
    this.inflicted += damage;
  }

  @Override
  public void incrementTaken(long damage) {
    this.taken += damage;
  }

  @Override
  public long getAndResetInflicted() {
    long inflicted = this.inflicted;
    this.inflicted = 0;
    return inflicted;
  }

  @Override
  public long getAndResetTaken() {
    long taken = this.taken;
    this.taken = 0;
    return taken;
  }

  @Override
  public String toString() {
    return "SimpleBattleLog{" + "taken=" + taken + ", inflicted=" + inflicted + '}';
  }

}
