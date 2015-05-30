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

import junit.framework.TestCase;

public class MathTest extends TestCase {

  public void testWeightedAverage() throws Exception {
    assertEquals(0.0, Math.weightedAverage(0.0, 1.0, new Percentage(0.0)));
    assertEquals(1.0, Math.weightedAverage(1.0, 0.0, new Percentage(0.0)));

    assertEquals(1.0, Math.weightedAverage(0.0, 1.0, new Percentage(1.0)));
    assertEquals(0.0, Math.weightedAverage(1.0, 0.0, new Percentage(1.0)));

    assertEquals(0.0, Math.weightedAverage(0.0, 0.5, new Percentage(0.0)));
    assertEquals(0.1, Math.weightedAverage(0.0, 0.5, new Percentage(0.2)));
    assertEquals(0.2, Math.weightedAverage(0.0, 0.5, new Percentage(0.4)));
    assertEquals(0.3, Math.weightedAverage(0.0, 0.5, new Percentage(0.6)));
    assertEquals(0.4, Math.weightedAverage(0.0, 0.5, new Percentage(0.8)));
    assertEquals(0.5, Math.weightedAverage(0.0, 0.5, new Percentage(1.0)));
  }

}