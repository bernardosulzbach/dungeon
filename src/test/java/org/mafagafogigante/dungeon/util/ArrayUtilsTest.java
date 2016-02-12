package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class ArrayUtilsTest {

  @Test
  public void findFirstOccurrenceShouldReturnTheIndexIfTheElementIsFound() throws Exception {
    String[] tokens = {"a", "b", "c", "d", "e"};
    for (int i = 0; i < tokens.length; i++) {
      Assert.assertEquals(i, ArrayUtils.findFirstOccurrence(tokens, tokens[i]));
    }
  }

  @Test
  public void findFirstOccurrenceShouldReturnTheLengthIfTheElementIsNotFound() throws Exception {
    String[] tokens = {"a", "b", "c", "d", "e"};
    Assert.assertEquals(tokens.length, ArrayUtils.findFirstOccurrence(tokens, "z"));
  }

}