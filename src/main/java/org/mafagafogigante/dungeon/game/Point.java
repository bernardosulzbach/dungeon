package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * A point in a tridimensional matrix.
 */
public class Point implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final int x;
  private final int y;
  private final int z;

  /**
   * Constructs a Point from three integers representing x, y, and z, respectively.
   */
  public Point(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Constructs a Point from another Point and a Direction that is equivalent to the specified Point moved towards the
   * provided Direction.
   */
  public Point(Point originalPoint, Direction shift) {
    this.x = originalPoint.getX() + shift.getOffset().getX();
    this.y = originalPoint.getY() + shift.getOffset().getY();
    this.z = originalPoint.getZ() + shift.getOffset().getZ();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Point point = (Point) object;
    return getX() == point.getX() && getY() == point.getY() && getZ() == point.getZ();
  }

  @Override
  public int hashCode() {
    int result = getX();
    result = 31 * result + getY();
    result = 31 * result + getZ();
    return result;
  }

  @Override
  public String toString() {
    return String.format("{%d, %d, %d}", getX(), getY(), getZ());
  }
}
