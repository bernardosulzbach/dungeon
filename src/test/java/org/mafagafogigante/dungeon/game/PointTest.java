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

package org.mafagafogigante.dungeon.game;

import org.junit.Assert;
import org.junit.Test;

public class PointTest {

  @Test
  public void equalsShouldReturnTrueToEqualPoints() throws Exception {
    Assert.assertTrue(new Point(0, 0, 0).equals(new Point(0, 0, 0)));
    Assert.assertTrue(new Point(1, 0, 0).equals(new Point(1, 0, 0)));
    Assert.assertTrue(new Point(0, 1, 0).equals(new Point(0, 1, 0)));
    Assert.assertTrue(new Point(0, 0, 1).equals(new Point(0, 0, 1)));
  }

  @Test
  public void equalsShouldReturnFalseToDifferentPoints() throws Exception {
    Assert.assertFalse(new Point(1, 0, 0).equals(new Point(0, 0, 0)));
    Assert.assertFalse(new Point(0, 1, 0).equals(new Point(0, 0, 0)));
    Assert.assertFalse(new Point(0, 0, 1).equals(new Point(0, 0, 0)));
    Assert.assertFalse(new Point(0, 0, 0).equals(new Point(1, 0, 0)));
    Assert.assertFalse(new Point(0, 0, 0).equals(new Point(0, 1, 0)));
    Assert.assertFalse(new Point(0, 0, 0).equals(new Point(0, 0, 1)));
  }

}