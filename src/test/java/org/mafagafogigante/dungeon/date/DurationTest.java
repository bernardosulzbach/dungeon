package org.mafagafogigante.dungeon.date;

import org.junit.Assert;
import org.junit.Test;

public class DurationTest {

  @Test
  public void testToString() throws Exception {
    Date start = new Date(2014, DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH), DungeonTimeUnit.MONTH.as(
        DungeonTimeUnit.DAY), 0, 0, 0);
    Date end = new Date(2014, DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH), DungeonTimeUnit.MONTH.as(
        DungeonTimeUnit.DAY), 0, 0, 0);

    end = end.plus(1, DungeonTimeUnit.DAY);
    Assert.assertEquals("1 day", new Duration(start, end).toString());
    end = end.plus(1, DungeonTimeUnit.DAY);
    Assert.assertEquals("2 days", new Duration(start, end).toString());
    end = end.minus(2, DungeonTimeUnit.DAY);

    end = end.plus(1, DungeonTimeUnit.MONTH);
    Assert.assertEquals("1 month", new Duration(start, end).toString());
    end = end.plus(1, DungeonTimeUnit.MONTH);
    Assert.assertEquals("2 months", new Duration(start, end).toString());
    end = end.plus(1, DungeonTimeUnit.DAY);
    Assert.assertEquals("2 months and 1 day", new Duration(start, end).toString());
    end = end.plus(1, DungeonTimeUnit.DAY);
    Assert.assertEquals("2 months and 2 days", new Duration(start, end).toString());
    end = end.minus(2, DungeonTimeUnit.DAY);
    end = end.minus(2, DungeonTimeUnit.MONTH);

    end = end.plus(1, DungeonTimeUnit.YEAR);
    Assert.assertEquals("1 year", new Duration(start, end).toString());
    end = end.plus(1, DungeonTimeUnit.YEAR);
    Assert.assertEquals("2 years", new Duration(start, end).toString());
    end = end.plus(1, DungeonTimeUnit.DAY);
    Assert.assertEquals("2 years and 1 day", new Duration(start, end).toString());
    end = end.minus(1, DungeonTimeUnit.DAY);
    end = end.plus(1, DungeonTimeUnit.MONTH);
    Assert.assertEquals("2 years and 1 month", new Duration(start, end).toString());
    end = end.plus(1, DungeonTimeUnit.DAY);
    Assert.assertEquals("2 years, 1 month, and 1 day", new Duration(start, end).toString());
  }

}
