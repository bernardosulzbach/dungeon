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

import java.awt.Color;

public class DungeonStringTest {

  @Test
  public void testGetLengthShouldWorkWithDefaultColor() throws Exception {
    DungeonString dungeonString = new DungeonString();
    Assert.assertEquals(0, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(1, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(2, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(3, dungeonString.getLength());
  }

  @Test
  public void testGetLengthShouldWorkWithMultipleColors() throws Exception {
    DungeonString dungeonString = new DungeonString();
    Assert.assertEquals(0, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(1, dungeonString.getLength());
    dungeonString.setColor(Color.BLUE);
    Assert.assertEquals(1, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(2, dungeonString.getLength());
    dungeonString.setColor(Color.RED);
    Assert.assertEquals(2, dungeonString.getLength());
    dungeonString.append("A");
    Assert.assertEquals(3, dungeonString.getLength());
  }

}