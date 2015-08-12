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

import org.junit.Assert;
import org.junit.Test;

public class ParsingUtilsTest {

  @Test
  public void splitOnOnWithEmptyArrayShouldProduceTwoEmptyArrays() throws Exception {
    String[] emptyArray = {};
    Assert.assertArrayEquals(emptyArray, ParsingUtils.splitOnOn(emptyArray).before);
    Assert.assertArrayEquals(emptyArray, ParsingUtils.splitOnOn(emptyArray).after);
  }

  @Test
  public void splitOnOnWithOnArrayShouldProduceTwoEmptyArrays() throws Exception {
    String[] emptyArray = {};
    String[] onArray = {"on"};
    Assert.assertArrayEquals(emptyArray, ParsingUtils.splitOnOn(onArray).before);
    Assert.assertArrayEquals(emptyArray, ParsingUtils.splitOnOn(onArray).after);
  }

  @Test
  public void splitOnOnWithOnOnArrayShouldProduceEmptyBeforeAndOnOnAfter() throws Exception {
    String[] emptyArray = {};
    String[] onArray = {"on"};
    String[] onOnArray = {"on", "on"};
    Assert.assertArrayEquals(emptyArray, ParsingUtils.splitOnOn(onOnArray).before);
    Assert.assertArrayEquals(onArray, ParsingUtils.splitOnOn(onOnArray).after);
  }

  @Test
  public void splitOnOnWithThreeWordsSeparatedByOnShouldSplitOnOn() throws Exception {
    String[] fooArray = {"foo"};
    String[] barArray = {"bar"};
    String[] onOnArray = {"foo", "on", "bar"};
    Assert.assertArrayEquals(fooArray, ParsingUtils.splitOnOn(onOnArray).before);
    Assert.assertArrayEquals(barArray, ParsingUtils.splitOnOn(onOnArray).after);
  }

}