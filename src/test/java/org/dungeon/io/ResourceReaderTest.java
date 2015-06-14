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

package org.dungeon.io;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class ResourceReaderTest {

  @Test
  public void testToArray() throws Exception {
    assertArrayEquals(new String[]{""}, ResourceReader.toArray(""));
    assertArrayEquals(new String[]{""}, ResourceReader.toArray("[]"));
    assertArrayEquals(new String[]{"Foo"}, ResourceReader.toArray("Foo"));
    assertArrayEquals(new String[]{"Foo"}, ResourceReader.toArray("[Foo]"));
    assertArrayEquals(new String[]{"A", "B", "C"}, ResourceReader.toArray("[A | B | C]"));
    assertArrayEquals(new String[]{"First", "Second"}, ResourceReader.toArray("[ First | Second ]"));
    assertArrayEquals(new String[]{"A", "[B | C]"}, ResourceReader.toArray("[A | [B | C]]"));
    assertArrayEquals(new String[]{"[A | B | C]"}, ResourceReader.toArray("[[A | B | C]]"));
  }

}