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

package org.mafagafogigante.dungeon.io;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitTest {

  @Test
  public void splitOnOnWithEmptyListShouldProduceTwoEmptyLists() throws Exception {
    Assert.assertEquals(Collections.<String>emptyList(), Split.splitOnOn(Collections.<String>emptyList()).getBefore());
    Assert.assertEquals(Collections.<String>emptyList(), Split.splitOnOn(Collections.<String>emptyList()).getAfter());
  }

  @Test
  public void splitOnOnWithOnListShouldProduceTwoEmptyLists() throws Exception {
    List<String> emptyList = Collections.emptyList();
    List<String> onList = Collections.singletonList("on");
    Assert.assertEquals(emptyList, Split.splitOnOn(onList).getBefore());
    Assert.assertEquals(emptyList, Split.splitOnOn(onList).getAfter());
  }

  @Test
  public void splitOnOnWithOnOnListShouldProduceEmptyBeforeAndOnOnAfter() throws Exception {
    List<String> emptyList = Collections.emptyList();
    List<String> onList = Collections.singletonList("on");
    List<String> onOnList = Arrays.asList("on", "on");
    Assert.assertEquals(emptyList, Split.splitOnOn(onOnList).getBefore());
    Assert.assertEquals(onList, Split.splitOnOn(onOnList).getAfter());
  }

  @Test
  public void splitOnOnWithThreeWordsSeparatedByOnShouldSplitOnOn() throws Exception {
    List<String> fooList = Collections.singletonList("foo");
    List<String> barList = Collections.singletonList("bar");
    List<String> sourceList = Arrays.asList("foo", "on", "bar");
    Assert.assertEquals(fooList, Split.splitOnOn(sourceList).getBefore());
    Assert.assertEquals(barList, Split.splitOnOn(sourceList).getAfter());
  }

}