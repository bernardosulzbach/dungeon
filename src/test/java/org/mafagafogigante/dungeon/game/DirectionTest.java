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

public class DirectionTest {

  @Test
  public void invertShouldCorrectlyInvertTheDirection() throws Exception {
    Assert.assertEquals(Direction.UP, Direction.DOWN.invert());
    Assert.assertEquals(Direction.NORTH, Direction.SOUTH.invert());
    Assert.assertEquals(Direction.EAST, Direction.WEST.invert());
    Assert.assertEquals(Direction.DOWN, Direction.UP.invert());
    Assert.assertEquals(Direction.SOUTH, Direction.NORTH.invert());
    Assert.assertEquals(Direction.WEST, Direction.EAST.invert());
  }

  @Test
  public void invertTwiceShouldNotModifyTheDirection() throws Exception {
    for (Direction direction : Direction.values()) {
      Assert.assertEquals(direction, direction.invert().invert());
    }
  }

}
