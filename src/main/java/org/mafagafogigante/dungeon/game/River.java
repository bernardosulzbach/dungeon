package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * River class that implements a river (a line of water in the World).
 */
class River implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int MIN_BRIDGE_DIST = 4;
  private static final int MAX_BRIDGE_DIST = 20;

  private final ExpandableIntegerSet bridges;

  /**
   * Make a river.
   */
  River() {
    bridges = new ExpandableIntegerSet(MIN_BRIDGE_DIST, MAX_BRIDGE_DIST);
  }

  /**
   * Expand the set of bridges towards a value of y until there is a bridge at y or after y.
   *
   * @param y the y coordinate
   */
  private void expand(int y) {
    bridges.expand(y);
  }

  /**
   * Evaluates if a given value of y corresponds to a bridge.
   *
   * @param y the y coordinate
   * @return true if there should be a bridge in this y coordinate
   */
  boolean isBridge(int y) {
    expand(y);
    return bridges.contains(y);
  }

  @Override
  public String toString() {
    return "River with bridges in " + bridges;
  }

}
