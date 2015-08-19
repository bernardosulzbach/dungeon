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

import org.junit.Assert;
import org.junit.Test;

public class DungeonMathTest {

  @Test
  public void testWeightedAverage() throws Exception {
    Assert.assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(0.0, 1.0, new Percentage(0.0))));
    Assert.assertEquals(0, Double.compare(1.0, DungeonMath.weightedAverage(1.0, 0.0, new Percentage(0.0))));

    Assert.assertEquals(0, Double.compare(1.0, DungeonMath.weightedAverage(0.0, 1.0, new Percentage(1.0))));
    Assert.assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(1.0, 0.0, new Percentage(1.0))));

    Assert.assertEquals(0, Double.compare(0.0, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.0))));
    Assert.assertEquals(0, Double.compare(0.1, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.2))));
    Assert.assertEquals(0, Double.compare(0.2, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.4))));
    Assert.assertEquals(0, Double.compare(0.3, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.6))));
    Assert.assertEquals(0, Double.compare(0.4, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(0.8))));
    Assert.assertEquals(0, Double.compare(0.5, DungeonMath.weightedAverage(0.0, 0.5, new Percentage(1.0))));
  }

  @Test
  public void testSafeCastLongToInteger() throws Exception {
    Assert.assertEquals(-1, DungeonMath.safeCastLongToInteger(-1L));
    Assert.assertEquals(0, DungeonMath.safeCastLongToInteger(0L));
    Assert.assertEquals(1, DungeonMath.safeCastLongToInteger(1L));
    Long minimumInteger = (long) Integer.MIN_VALUE;
    Assert.assertEquals(Integer.MIN_VALUE, DungeonMath.safeCastLongToInteger(minimumInteger));
    minimumInteger -= 1;
    try {
      DungeonMath.safeCastLongToInteger(minimumInteger);
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
    Long maximumInteger = (long) Integer.MAX_VALUE;
    Assert.assertEquals(Integer.MAX_VALUE, DungeonMath.safeCastLongToInteger(maximumInteger));
    maximumInteger += 1;
    try {
      DungeonMath.safeCastLongToInteger(maximumInteger);
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

}