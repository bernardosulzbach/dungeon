package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class ShuffledRangeTest {

  @Test
  public void testShuffle() throws Exception {
    // The special case of a ShuffledRange of a single element.
    ShuffledRange shuffledRange = new ShuffledRange(1, 2);
    int oldLast = shuffledRange.get(shuffledRange.getSize() - 1);
    shuffledRange.shuffle();
    int newFirst = shuffledRange.get(0);
    Assert.assertEquals(oldLast, newFirst);
    // ShuffledRanges of different lengths, checking one of the properties of the shuffle method a few times.
    for (int rangeEnd = 3; rangeEnd <= 5; rangeEnd++) {
      shuffledRange = new ShuffledRange(1, rangeEnd);
      for (int i = 0; i < 100; i++) {
        oldLast = shuffledRange.get(shuffledRange.getSize() - 1);
        shuffledRange.shuffle();
        newFirst = shuffledRange.get(0);
        Assert.assertNotEquals(oldLast, newFirst);
      }
    }
  }

}
