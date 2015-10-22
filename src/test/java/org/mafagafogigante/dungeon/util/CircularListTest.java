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

package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class CircularListTest {

  @Test
  public void testAdd() throws Exception {
    CircularList<Integer> circularList = new CircularList<Integer>(2);
    circularList.add(1);
    Assert.assertTrue(circularList.get(0).equals(1));
    circularList.add(2);
    Assert.assertTrue(circularList.get(0).equals(1));
    Assert.assertTrue(circularList.get(1).equals(2));
    circularList.add(3);
    Assert.assertTrue(circularList.get(0).equals(2));
    Assert.assertTrue(circularList.get(1).equals(3));
    circularList.add(4);
    Assert.assertTrue(circularList.get(0).equals(3));
    Assert.assertTrue(circularList.get(1).equals(4));
  }

  @Test
  public void testSize() throws Exception {
    final int CAPACITY = 10;
    CircularList<Integer> circularList = new CircularList<Integer>(CAPACITY);
    Assert.assertEquals(0, circularList.size());
    for (int i = 0; i <= 2 * CAPACITY; i++) { // i is how many elements we have already added.
      Assert.assertEquals(java.lang.Math.min(i, CAPACITY), circularList.size());
      circularList.add(i);
    }
  }

  @Test
  public void testIsFull() throws Exception {
    CircularList<Integer> circularList = new CircularList<Integer>(2);
    Assert.assertFalse(circularList.isFull());
    circularList.add(1);
    Assert.assertFalse(circularList.isFull());
    circularList.add(2);
    Assert.assertTrue(circularList.isFull());
    circularList.add(3);
    Assert.assertTrue(circularList.isFull());
  }

  @Test
  public void testIsEmpty() throws Exception {
    CircularList<Integer> circularList = new CircularList<Integer>(1);
    Assert.assertTrue(circularList.isEmpty());
    circularList.add(1);
    Assert.assertFalse(circularList.isEmpty());
  }

  @Test
  public void testGet() throws Exception {
    final int CAPACITY = 10;
    CircularList<Integer> circularList = new CircularList<Integer>(CAPACITY);
    for (int i = 0; i <= 2 * CAPACITY; i++) {
      circularList.add(i);
      Assert.assertTrue(circularList.get(java.lang.Math.min(i, CAPACITY - 1)).equals(i));
    }
  }

}
