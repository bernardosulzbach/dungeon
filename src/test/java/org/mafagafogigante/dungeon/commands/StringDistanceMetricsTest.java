package org.mafagafogigante.dungeon.commands;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class StringDistanceMetricsTest {

  private static final int SMALL_INPUT_MINIMUM_SIZE = 4;
  private static final int SMALL_INPUT_MAXIMUM_SIZE = 256;
  private static final int STRESS_INPUT_MINIMUM_SIZE = 256;
  private static final int STRESS_INPUT_MAXIMUM_SIZE = 65536;

  /**
   * Should use a new Random object to preserve test independence.
   */
  private static final Random random = new Random();

  private static char randomCharacter() {
    return (char) ((int) 'a' + random.nextInt('z' - 'a' + 1));
  }

  private static String makeRandomString(int size) {
    StringBuilder buffer = new StringBuilder(size);
    for (int i = 0; i < size; i++) {
      buffer.append(randomCharacter());
    }
    return buffer.toString();
  }

  private static int countCharacters(String string, char replacement) {
    int count = 0;
    for (int i = 0; i < string.length(); i++) {
      if (string.charAt(i) == replacement) {
        count++;
      }
    }
    return count;
  }

  private static String replaceLetterByDash(String string) {
    for (char c = 'a'; c <= 'z'; c++) {
      int characterCount = countCharacters(string, c);
      if (characterCount != 0) {
        return string.replace(c, '-');
      }
    }
    throw new IllegalArgumentException("string has no characters from a to z.");
  }

  @Test
  public void levenshteinDistanceShouldWorkForSmallInputs() throws Exception {
    for (int inputSize = SMALL_INPUT_MINIMUM_SIZE; inputSize <= SMALL_INPUT_MAXIMUM_SIZE; inputSize *= 2) {
      String input = makeRandomString(inputSize);
      String shuffled = replaceLetterByDash(input);
      int expectedDistance = countCharacters(shuffled, '-');
      int distance = StringDistanceMetrics.levenshteinDistance(input, shuffled);
      Assert.assertEquals(expectedDistance, distance);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void levenshteinDistanceShouldNotRunOutOfMemory() throws Exception {
    for (int inputSize = STRESS_INPUT_MINIMUM_SIZE; inputSize <= STRESS_INPUT_MAXIMUM_SIZE; inputSize *= 2) {
      String input = makeRandomString(inputSize);
      String shuffled = replaceLetterByDash(input);
      int expectedDistance = countCharacters(shuffled, '-');
      // Either this works and we must get a correct result, or this produces an IllegalArgumentException.
      int distance = StringDistanceMetrics.levenshteinDistance(input, shuffled);
      Assert.assertEquals(expectedDistance, distance);
    }
  }

}
