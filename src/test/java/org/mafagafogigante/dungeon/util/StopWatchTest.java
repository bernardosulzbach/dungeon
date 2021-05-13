package org.mafagafogigante.dungeon.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

  @Test
  public void testToString() throws Exception {
    StopWatch stopWatch = new StopWatch();
    Assertions.assertTrue(stopWatch.toString(TimeUnit.NANOSECONDS).endsWith("ns"));
    Assertions.assertTrue(stopWatch.toString(TimeUnit.MICROSECONDS).endsWith("μs"));
    Assertions.assertTrue(stopWatch.toString(TimeUnit.MILLISECONDS).endsWith("ms"));
    Assertions.assertTrue(stopWatch.toString(TimeUnit.SECONDS).endsWith("s"));
    Assertions.assertTrue(stopWatch.toString().endsWith("ms"));
  }

}
