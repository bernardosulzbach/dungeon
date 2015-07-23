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

import org.junit.Assert;
import org.junit.Test;

public class IdTest {

  @Test
  public void testConstructor() throws Exception {
    try {
      new Id(null);
      Assert.fail("expected an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
    try {
      new Id("");
      Assert.fail("expected an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
    try {
      new Id(" ");
      Assert.fail("expected an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
    new Id("403_ACCESS_FORBIDDEN");
    new Id("404_NOT_FOUND");
    new Id("VALID_ID");
    new Id("YET_ANOTHER_VALID_ID");
    new Id("ID_WITH_NUMBER_255");
  }

}