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
 * Point class that defines a a point in a 2D plane.
 * <p/>
 * Its objects are used by the World to map coordinates to Location objects. Campaign also uses Point objects to store
 * the position of some creatures in the world.
 *
 * @author Bernardo Sulzbach
 */
public class Point implements Serializable {

  private static final long serialVersionUID = 1L;

  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point(Point originalPoint, Direction shift) {
    this.x = originalPoint.getX() + shift.getX();
    this.y = originalPoint.getY() + shift.getY();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object anotherObject) {
    if (this == anotherObject) {
      return true;
    }
    if (anotherObject instanceof Point) {
      Point anotherPoint = (Point) anotherObject;
      return this.x == anotherPoint.getX() && this.y == anotherPoint.getY();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + this.x;
    hash = 29 * hash + this.y;
    return hash;
  }

  @Override
  public String toString() {
    return String.format("{%d,%d}", getX(), getY());
  }
}
