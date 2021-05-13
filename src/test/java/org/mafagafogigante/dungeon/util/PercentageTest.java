package org.mafagafogigante.dungeon.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PercentageTest {

  @Test
  public void testCompareTo() throws Exception {
    Assertions.assertTrue(new Percentage(0.1).compareTo(new Percentage(0.2)) < 0);
    Assertions.assertTrue(new Percentage(0.2).compareTo(new Percentage(0.1)) > 0);
    Assertions.assertTrue(new Percentage(0.0).equals(new Percentage(0.0)));
    Assertions.assertTrue(new Percentage(1.0).equals(new Percentage(1.0)));
    Assertions.assertTrue(new Percentage(0.0).compareTo(new Percentage(1e-6)) < 0);
    Assertions.assertTrue(new Percentage(1e-6).compareTo(new Percentage(0.0)) > 0);
  }

  @Test
  public void testBiggerThanOrEqualTo() throws Exception {
    Assertions.assertTrue(new Percentage(1.0).biggerThanOrEqualTo(new Percentage(0.0)));
    Assertions.assertTrue(new Percentage(1.0).biggerThanOrEqualTo(new Percentage(0.9999)));
    Assertions.assertTrue(new Percentage(0.0001).biggerThanOrEqualTo(new Percentage(0.0)));
    Assertions.assertFalse(new Percentage(0.0).biggerThanOrEqualTo(new Percentage(1.0)));
    Assertions.assertFalse(new Percentage(0.9999).biggerThanOrEqualTo(new Percentage(1.0)));
    Assertions.assertFalse(new Percentage(0.0).biggerThanOrEqualTo(new Percentage(0.0001)));
  }

  @Test
  public void testToString() throws Exception {
    Assertions.assertEquals("0.00%", new Percentage(0.0).toString());
    Assertions.assertEquals("5.50%", new Percentage(0.055).toString());
    Assertions.assertEquals("100.00%", new Percentage(1.0).toString());
  }

  @Test
  public void testFromString() throws Exception {
    Percentage[] objects = {new Percentage(0.0), new Percentage(0.2468), new Percentage(1.0)};
    for (Percentage percentage : objects) {
      Assertions.assertEquals(percentage, Percentage.fromString(percentage.toString()));
    }
  }

  @Test
  public void testIsValidPercentageString() throws Exception {
    Assertions.assertTrue(Percentage.isValidPercentageString("0.0%"));
    Assertions.assertTrue(Percentage.isValidPercentageString("100.0%"));
    Assertions.assertTrue(Percentage.isValidPercentageString("12.3456789%"));
    // Sixty decimal places.
    Assertions.assertTrue(
            Percentage.isValidPercentageString("0.000000000000000000000000000000000000000000000000000000000000%"));
    Assertions.assertTrue(
            Percentage.isValidPercentageString("0.000000000000000000000000000000000000000000000000000000000001%"));
    Assertions.assertFalse(Percentage.isValidPercentageString("110.00%"));
    Assertions.assertFalse(Percentage.isValidPercentageString("100.001%"));
    Assertions.assertFalse(Percentage.isValidPercentageString("100.0001%"));
    Assertions.assertFalse(Percentage.isValidPercentageString("100.00001%")); // Enough precision.
  }

}
