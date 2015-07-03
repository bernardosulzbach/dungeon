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

package org.dungeon.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dungeon.util.Percentage;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTest {

  @Test
  public void testRollWithDoubles() throws Exception {
    assertFalse(Random.roll(0.0));
    assertTrue(Random.roll(1.0));
  }

  @Test
  public void testRollWithPercentages() throws Exception {
    assertFalse(Random.roll(new Percentage(0.0)));
    assertTrue(Random.roll(new Percentage(1.0)));
  }

  @Test
  public void testSelect() throws Exception {
    try {
      Random.select(Collections.emptyList());
      Assert.fail("expected an exception.");
    } catch (IllegalArgumentException expected) {
      // Dungeon Code Style does not require a comment on exceptions named expected in tests.
    }
    List<Integer> integerList = new ArrayList<Integer>();
    integerList.add(0);
    assertTrue(Random.select(integerList).equals(0));
  }

}
