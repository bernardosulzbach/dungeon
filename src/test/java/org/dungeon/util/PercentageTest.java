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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PercentageTest {

  @Test
  public void testCompareTo() throws Exception {
    assertTrue(new Percentage(0.1).compareTo(new Percentage(0.2)) < 0);
    assertTrue(new Percentage(0.2).compareTo(new Percentage(0.1)) > 0);
    assertTrue(new Percentage(0.0).equals(new Percentage(0.0)));
    assertTrue(new Percentage(1.0).equals(new Percentage(1.0)));
    assertTrue(new Percentage(0.0).compareTo(new Percentage(1e-6)) < 0);
    assertTrue(new Percentage(1e-6).compareTo(new Percentage(0.0)) > 0);
  }

  @Test
  public void testBiggerThanOrEqualTo() throws Exception {
    assertTrue(new Percentage(1.0).biggerThanOrEqualTo(new Percentage(0.0)));
    assertTrue(new Percentage(1.0).biggerThanOrEqualTo(new Percentage(0.9999)));
    assertTrue(new Percentage(0.0001).biggerThanOrEqualTo(new Percentage(0.0)));
    assertFalse(new Percentage(0.0).biggerThanOrEqualTo(new Percentage(1.0)));
    assertFalse(new Percentage(0.9999).biggerThanOrEqualTo(new Percentage(1.0)));
    assertFalse(new Percentage(0.0).biggerThanOrEqualTo(new Percentage(0.0001)));
  }

  @Test
  public void testToString() throws Exception {
    assertEquals("0.00%", new Percentage(0.0).toString());
    assertEquals("5.50%", new Percentage(0.055).toString());
    assertEquals("100.00%", new Percentage(1.0).toString());
  }

  @Test
  public void testFromString() throws Exception {
    Percentage[] objects = {new Percentage(0.0), new Percentage(0.2468), new Percentage(1.0)};
    for (Percentage percentage : objects) {
      assertEquals(percentage, Percentage.fromString(percentage.toString()));
    }
  }

  @Test
  public void testIsValidPercentageString() throws Exception {
    assertTrue(Percentage.isValidPercentageString("0.0%"));
    assertTrue(Percentage.isValidPercentageString("100.0%"));
    assertTrue(Percentage.isValidPercentageString("12.3456789%"));
    // Sixty decimal places.
    assertTrue(Percentage.isValidPercentageString("0.000000000000000000000000000000000000000000000000000000000000%"));
    assertTrue(Percentage.isValidPercentageString("0.000000000000000000000000000000000000000000000000000000000001%"));
    assertFalse(Percentage.isValidPercentageString("110.00%"));
    assertFalse(Percentage.isValidPercentageString("100.001%"));
    assertFalse(Percentage.isValidPercentageString("100.0001%"));
    assertFalse(Percentage.isValidPercentageString("100.00001%")); // Enough precision.
  }

}