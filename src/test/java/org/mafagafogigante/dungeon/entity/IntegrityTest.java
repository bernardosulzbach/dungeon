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

package org.mafagafogigante.dungeon.entity;

import org.junit.Assert;
import org.junit.Test;

public class IntegrityTest {

  @Test
  public void testIsMaximum() throws Exception {
    Assert.assertTrue(new Integrity(2, 2).isMaximum());
    Assert.assertFalse(new Integrity(2, 1).isMaximum());
    Assert.assertFalse(new Integrity(2, 0).isMaximum());
  }

  @Test
  public void testIsZero() throws Exception {
    Assert.assertTrue(new Integrity(2, 0).isZero());
    Assert.assertFalse(new Integrity(2, 1).isZero());
    Assert.assertFalse(new Integrity(2, 2).isZero());
  }

  @Test
  public void incrementByShouldWorkWithPositiveValues() throws Exception {
    Integrity integrity = new Integrity(1, 0);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.incrementBy(1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.incrementBy(1);
    Assert.assertEquals(1, integrity.getCurrent());
  }

  @Test
  public void incrementByShouldWorkWithNegativeValues() throws Exception {
    Integrity integrity = new Integrity(1, 1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.incrementBy(-1);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.incrementBy(-1);
    Assert.assertEquals(0, integrity.getCurrent());
  }

  @Test
  public void decrementByShouldWorkWithPositiveValues() throws Exception {
    Integrity integrity = new Integrity(1, 1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.decrementBy(1);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.decrementBy(1);
    Assert.assertEquals(0, integrity.getCurrent());
  }

  @Test
  public void decrementByShouldWorkWithNegativeValues() throws Exception {
    Integrity integrity = new Integrity(1, 0);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.decrementBy(-1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.decrementBy(-1);
    Assert.assertEquals(1, integrity.getCurrent());
  }

}