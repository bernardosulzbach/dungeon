package org.mafagafogigante.dungeon.game;

/**
 * Direction enum that implements all the possible movement directions in the game.
 */
public enum Direction {

  // This order is of uttermost importance and some methods in this class DEPEND on it.
  // Do not change without checking all the methods of this enum.
  UP("Up", "U", new Point(0, 0, 1)),
  NORTH("North", "N", new Point(0, 1, 0)),
  EAST("East", "E", new Point(1, 0, 0)),
  DOWN("Down", "D", new Point(0, 0, -1)),
  SOUTH("South", "S", new Point(0, -1, 0)),
  WEST("West", "W", new Point(-1, 0, 0));

  private final String name;
  private final String abbreviation;
  private final Point offset;

  Direction(String name, String abbreviation, Point offset) {
    this.name = name;
    this.abbreviation = abbreviation;
    this.offset = offset;
  }

  /**
   * Returns the Direction that a given abbreviation corresponds to.
   *
   * @param abbreviation an abbreviation such as "N" or "E"
   * @return a Direction value or null if there is no match
   */
  public static Direction fromAbbreviation(String abbreviation) {
    for (Direction direction : values()) {
      if (direction.abbreviation.equals(abbreviation)) {
        return direction;
      }
    }
    return null;
  }

  public Point getOffset() {
    return offset;
  }

  /**
   * Returns the opposite direction.
   */
  public Direction invert() {
    return values()[(ordinal() + values().length / 2) % values().length];
  }

  public boolean equalsIgnoreCase(String str) {
    return name.equalsIgnoreCase(str) || abbreviation.equalsIgnoreCase(str);
  }

  @Override
  public String toString() {
    return name;
  }

}
