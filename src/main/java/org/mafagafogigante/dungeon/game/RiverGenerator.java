package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The component of WorldGenerator that generates rivers.
 */
final class RiverGenerator implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int MIN_DIST_RIVER = 6;
  private static final int MAX_DIST_RIVER = 11;
  private static final int START = 10; // Rivers do not appear in x > 10 || x < 10.
  private final ExpandableIntegerSet lines;
  private final HashMap<Integer, River> rivers;

  public RiverGenerator() {
    lines = new ExpandableIntegerSet(MIN_DIST_RIVER, MAX_DIST_RIVER);
    rivers = new HashMap<>();
  }

  /**
   * Expand the river set to ensure that all points whose x coordinate is in the range {@code [point.x - chunkSide,
   * point.x + chunkSide]} will either correspond to a river or to a location that anticipates a river.
   *
   * @param point the point from which the expansion starts
   * @param chunkSide the current chunk side
   */
  void expand(Point point, int chunkSide) {
    for (int river : lines.expand(point.getX() - chunkSide)) {
      if (river <= -START) {
        rivers.put(river, new River());
      }
    }
    for (int river : lines.expand(point.getX() + chunkSide)) {
      if (river >= START) {
        rivers.put(river, new River());
      }
    }
  }

  /**
   * Returns if in this point there should be a river.
   */
  boolean isRiver(Point point) {
    River river = rivers.get(point.getX());
    return river != null && !river.isBridge(point.getY());
  }

  /**
   * Returns if in this point there should be a bridge.
   */
  boolean isBridge(Point point) {
    River river = rivers.get(point.getX());
    return river != null && river.isBridge(point.getY());
  }

}
