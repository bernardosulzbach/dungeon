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

package org.dungeon.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class TableTest {

  private static Table table;

  @Before
  public void createTable() {
    // Always create a new table before running a test.
    table = new Table("A", "B", "C");
    // Insert 100 integers into A.
    for (int i = 0; i < 100; i++) {
      table.insertRow(String.valueOf(i));
    }
  }

  @Test
  public void testInsertRow() throws Exception {
    assertTrue(table.getDimensions().equals(new Dimensions(100, 3)));
    Random random = new Random();
    // Insert 200 rows in the table.
    for (int i = 0; i < 100; i++) {
      if (random.nextBoolean()) {
        table.insertRow(String.valueOf(i), String.valueOf(random.nextInt()));
      } else {
        table.insertRow(String.valueOf(i), String.valueOf(random.nextInt()), "");
      }
      // Test if nulls are handled properly.
      table.insertRow(null, null, null);
    }
    assertTrue(table.getDimensions().equals(new Dimensions(300, 3)));
  }

  @Test
  public void testContains() throws Exception {
    // Check for the 100 values inserted by createTable().
    for (int i = 0; i < 100; i++) {
      assertTrue(table.contains(String.valueOf(i)));
    }
    // There should be 200 empty Strings, 100 in B and 100 in C.
    assertTrue(table.contains(""));
    // There should not be any null.
    assertFalse(table.contains(null));
  }

  @Test
  public void testGetDimensions() throws Exception {
    Dimensions expectedDimensions = new Dimensions(100, 3);
    assertTrue(table.getDimensions().equals(expectedDimensions));
  }

}
