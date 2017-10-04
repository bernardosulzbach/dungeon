package org.mafagafogigante.dungeon.date;

import org.junit.Assert;
import org.junit.Test;

public class DateTest {

  /**
   * As there is not a maximum year defined by the Date class, just test stuff up to the year 1000.
   */
  private static final int MAX_YEAR = 1000;

  @Test
  public void testGetDay() throws Exception {
    for (int i = 1; i <= DungeonTimeUnit.MONTH.as(DungeonTimeUnit.DAY); i++) {
      Assert.assertEquals(i, new Date(1, 1, i).getDay());
    }
  }

  @Test
  public void testGetMonth() throws Exception {
    for (int i = 1; i <= DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH); i++) {
      Assert.assertEquals(i, new Date(1, i, 1).getMonth());
    }
  }

  @Test
  public void testGetYear() throws Exception {
    for (int i = 1; i <= MAX_YEAR; i++) {
      Assert.assertEquals(i, new Date(i, 1, 1).getYear());
    }
  }

  @Test
  public void testMinusDays() throws Exception {
    Date date = new Date(1, 1, DungeonTimeUnit.MONTH.as(DungeonTimeUnit.DAY));
    for (long i = DungeonTimeUnit.MONTH.as(DungeonTimeUnit.DAY); i > 0; i--) {
      Assert.assertEquals(i, date.getDay());
      date = date.minus(1, DungeonTimeUnit.DAY);
    }
  }

  @Test
  public void testMinusMonths() throws Exception {
    Date date = new Date(1, DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH), 1);
    for (long i = DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH); i > 0; i--) {
      Assert.assertEquals(i, date.getMonth());
      date = date.minus(1, DungeonTimeUnit.MONTH);
    }
  }

  @Test
  public void testMinusYears() throws Exception {
    Date date = new Date(MAX_YEAR, 1, 1);
    for (long i = MAX_YEAR; i > 0; i--) {
      Assert.assertEquals(i, date.getYear());
      date = date.minus(1, DungeonTimeUnit.YEAR);
    }
  }

  @Test
  public void testPlusDays() throws Exception {
    Date date = new Date(1, 1, 1);
    for (long i = 1; i <= DungeonTimeUnit.MONTH.as(DungeonTimeUnit.DAY); i++) {
      Assert.assertEquals(i, date.getDay());
      date = date.plus(1, DungeonTimeUnit.DAY);
    }
  }

  @Test
  public void testPlusMonths() throws Exception {
    Date date = new Date(1, 1, 1);
    for (long i = 1; i <= DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH); i++) {
      Assert.assertEquals(i, date.getMonth());
      date = date.plus(1, DungeonTimeUnit.MONTH);
    }
  }

  @Test
  public void testPlusYears() throws Exception {
    Date date = new Date(1, 1, 1);
    for (long i = 1; i <= MAX_YEAR; i++) {
      Assert.assertEquals(i, date.getYear());
      date = date.plus(1, DungeonTimeUnit.YEAR);
    }
  }

  @Test
  public void testCompareTo() throws Exception {
    Date a = new Date(1, 1, 1);
    Date b = new Date(1, 1, 1);
    Date c = new Date(1, 1, 2);
    Assert.assertTrue(a.compareTo(b) == 0);
    Assert.assertTrue(b.compareTo(a) == 0);
    Assert.assertTrue(a.compareTo(c) < 0);
    Assert.assertTrue(b.compareTo(c) < 0);
    Assert.assertTrue(c.compareTo(a) > 0);
    Assert.assertTrue(c.compareTo(b) > 0);
  }

}
