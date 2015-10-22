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

package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

  @Test
  public void testToString() throws Exception {
    StopWatch stopWatch = new StopWatch();
    Assert.assertTrue(stopWatch.toString(TimeUnit.NANOSECONDS).endsWith("ns"));
    Assert.assertTrue(stopWatch.toString(TimeUnit.MICROSECONDS).endsWith("μs"));
    Assert.assertTrue(stopWatch.toString(TimeUnit.MILLISECONDS).endsWith("ms"));
    Assert.assertTrue(stopWatch.toString(TimeUnit.SECONDS).endsWith("s"));
    Assert.assertTrue(stopWatch.toString().endsWith("ms"));
  }

}