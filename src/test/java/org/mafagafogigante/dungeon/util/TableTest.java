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

package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class TableTest {

  @Test
  public void insertRowShouldWorkWithTheCorrectAmountOfArguments() throws Exception {
    Table table = new Table("A", "B");
    try {
      for (int i = 0; i < 100; i++) {
        table.insertRow("1", "2");
      }
    } catch (Exception unexpected) {
      Assert.fail();
    }
  }

  @Test
  public void constructorShouldThrowAnExceptionIfThereAreNoArguments() throws Exception {
    try {
      new Table();
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void constructorShouldThrowAnExceptionIfThereAreTooManyArguments() throws Exception {
    try {
      new Table("A", "B", "C", "D", "E", "F", "G");
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void insertRowShouldThrowAnExceptionWithTooFewArguments() throws Exception {
    Table table = new Table("A", "B");
    try {
      table.insertRow("1");
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void insertRowShouldThrowAnExceptionWithTooManyArguments() throws Exception {
    Table table = new Table("A", "B");
    try {
      table.insertRow("1", "2", "3");
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

}
