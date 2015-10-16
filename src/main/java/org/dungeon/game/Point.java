/*
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.game;

import java.io.Serializable;

/**
 * A point in a tridimensional matrix.
 */
public class Point implements Serializable {

  private final int x;
  private final int y;
  private final int z;

  public Point(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

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
