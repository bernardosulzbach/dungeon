package org.dungeon.game;

/**
 * Abstract Preset class that defines common methods for all presets.
 * <p/>
 * Created by Bernardo Sulzbach on 03/12/14.
 */
abstract class Preset {

  private boolean finished;

  void lock() {
    finished = true;
  }

  boolean isLocked() {
    return finished;
  }

}
