/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.dungeon.util;

/**
 * Array utilities.
 */
public final class ArrayUtils {

  private ArrayUtils() {
    throw new AssertionError();
  }

  /**
   * Finds the first occurrence of a value in an array. Returns the length of the array if the element could not be
   * found.
   */
  public static <T> int findFirstOccurrence(T[] array, T element) {
    int index = 0;
    for (T token : array) {
      if (token.equals(element)) {
        return index;
      }
      index++;
    }
    return index;
  }

}
