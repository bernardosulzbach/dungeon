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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DungeonMathTest {

  @Test
  public void testWeightedAverage() throws Exception {
    assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(0.0, 1.0, new Percentage(0.0))));
    assertEquals(0, Double.compare(1.0, DungeonMath.weightedAverage(1.0, 0.0, new Percentage(0.0))));

    assertEquals(0, Double.compare(1.0, DungeonMath.weightedAverage(0.0, 1.0, new Percentage(1.0))));
    assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(1.0, 0.0, new Percentage(1.0))));

    assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.0))));
    assertEquals(0, Double.compare(0.1, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.2))));
    assertEquals(0, Double.compare(0.2, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.4))));
    assertEquals(0, Double.compare(0.3, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.6))));
    assertEquals(0, Double.compare(0.4, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.8))));
    assertEquals(0, Double.compare(0.5, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(1.0))));
  }

  @Test
  public void testSafeCastLongToInteger() throws Exception {
    assertEquals(-1, DungeonMath.safeCastLongToInteger(-1L));
    assertEquals(0, DungeonMath.safeCastLongToInteger(0L));
    assertEquals(1, DungeonMath.safeCastLongToInteger(1L));
    Long minimumInteger = (long) Integer.MIN_VALUE;
    assertEquals(Integer.MIN_VALUE, DungeonMath.safeCastLongToInteger(minimumInteger));
    minimumInteger -= 1;
    try {
      DungeonMath.safeCastLongToInteger(minimumInteger);
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
    Long maximumInteger = (long) Integer.MAX_VALUE;
    assertEquals(Integer.MAX_VALUE, DungeonMath.safeCastLongToInteger(maximumInteger));
    maximumInteger += 1;
    try {
      DungeonMath.safeCastLongToInteger(maximumInteger);
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void testDistribute() throws Exception {
    // Standard tests.
    int[] emptyArray = new int[0];
    try {
      DungeonMath.distribute(100, emptyArray);
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
    int[] array = {0, 0};
    DungeonMath.distribute(100, array);
    assertEquals(50, array[0]);
    assertEquals(50, array[1]);
    DungeonMath.distribute(-200, array);
    assertEquals(-50, array[0]);
    assertEquals(-50, array[1]);
    DungeonMath.distribute(0, array);
    assertEquals(-50, array[0]);
    assertEquals(-50, array[1]);
    DungeonMath.distribute(1, array);
    assertEquals(-49, array[0]);
    assertEquals(-50, array[1]);
    // Test the examples given in the Javadoc.
    int[] first = {2, 3, 4};
    DungeonMath.distribute(3, first);
    final int[] firstExpected = {3, 4, 5};
    assertTrue(Arrays.equals(firstExpected, first));
    int[] second = {5, 10};
    DungeonMath.distribute(-8, second);
    int[] secondExpected = {1, 6};
    assertTrue(Arrays.equals(secondExpected, second));
    int[] third = {2, 3};
    DungeonMath.distribute(3, third);
    final int[] thirdExpected = {4, 4};
    assertTrue(Arrays.equals(thirdExpected, third));
    int[] fourth = {5, 10, 15};
    DungeonMath.distribute(-8, fourth);
    int[] fourthExpected = {2, 7, 13};
    assertTrue(Arrays.equals(fourthExpected, fourth));
  }

}