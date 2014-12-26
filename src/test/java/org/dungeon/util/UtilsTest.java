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

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

  @Test
  public void testCheckQueryMatch() throws Exception {
    String[] candidateTokens = Utils.split("Big White Tiger");

    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("b"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("b w"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("b t"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("b w t"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("w"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("w t"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("t"), candidateTokens));

    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("big white tiger"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("big white"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("big tiger"), candidateTokens));
    Assert.assertTrue(Utils.checkQueryMatch(Utils.split("white tiger"), candidateTokens));

    Assert.assertFalse(Utils.checkQueryMatch(Utils.split("t w b"), candidateTokens));
    Assert.assertFalse(Utils.checkQueryMatch(Utils.split("t w"), candidateTokens));
    Assert.assertFalse(Utils.checkQueryMatch(Utils.split("t b"), candidateTokens));
    Assert.assertFalse(Utils.checkQueryMatch(Utils.split("w b"), candidateTokens));
  }

  @Test
  public void testRoll() throws Exception {
    for (int i = 0; i < 100; i++) {
      Assert.assertFalse(Utils.roll(0.0));
      Assert.assertTrue(Utils.roll(1.0));
    }
  }

}