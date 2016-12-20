package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class PercentageTest {

  @Test
  public void testCompareTo() throws Exception {
    Assert.assertTrue(new Percentage(0.1).compareTo(new Percentage(0.2)) < 0);
    Assert.assertTrue(new Percentage(0.2).compareTo(new Percentage(0.1)) > 0);
    Assert.assertTrue(new Percentage(0.0).equals(new Percentage(0.0)));
    Assert.assertTrue(new Percentage(1.0).equals(new Percentage(1.0)));
    Assert.assertTrue(new Percentage(0.0).compareTo(new Percentage(1e-6)) < 0);
    Assert.assertTrue(new Percentage(1e-6).compareTo(new Percentage(0.0)) > 0);
  }

  @Test
  public void testBiggerThanOrEqualTo() throws Exception {
    Assert.assertTrue(new Percentage(1.0).biggerThanOrEqualTo(new Percentage(0.0)));
    Assert.assertTrue(new Percentage(1.0).biggerThanOrEqualTo(new Percentage(0.9999)));
    Assert.assertTrue(new Percentage(0.0001).biggerThanOrEqualTo(new Percentage(0.0)));
    Assert.assertFalse(new Percentage(0.0).biggerThanOrEqualTo(new Percentage(1.0)));
    Assert.assertFalse(new Percentage(0.9999).biggerThanOrEqualTo(new Percentage(1.0)));
    Assert.assertFalse(new Percentage(0.0).biggerThanOrEqualTo(new Percentage(0.0001)));
  }

  @Test
  public void testToString() throws Exception {
    Assert.assertEquals("0.00%", new Percentage(0.0).toString());
    Assert.assertEquals("5.50%", new Percentage(0.055).toString());
    Assert.assertEquals("100.00%", new Percentage(1.0).toString());
  }

  @Test
  public void testFromString() throws Exception {
    Percentage[] objects = {new Percentage(0.0), new Percentage(0.2468), new Percentage(1.0)};
    for (Percentage percentage : objects) {
      Assert.assertEquals(percentage, Percentage.fromString(percentage.toString()));
    }
  }

  @Test
  public void testIsValidPercentageString() throws Exception {
    Assert.assertTrue(Percentage.isValidPercentageString("0.0%"));
    Assert.assertTrue(Percentage.isValidPercentageString("100.0%"));
    Assert.assertTrue(Percentage.isValidPercentageString("12.3456789%"));
    // Sixty decimal places.
    Assert.assertTrue(
        Percentage.isValidPercentageString("0.000000000000000000000000000000000000000000000000000000000000%"));
    Assert.assertTrue(
        Percentage.isValidPercentageString("0.000000000000000000000000000000000000000000000000000000000001%"));
    Assert.assertFalse(Percentage.isValidPercentageString("110.00%"));
    Assert.assertFalse(Percentage.isValidPercentageString("100.001%"));
    Assert.assertFalse(Percentage.isValidPercentageString("100.0001%"));
    Assert.assertFalse(Percentage.isValidPercentageString("100.00001%")); // Enough precision.
  }

}
