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
