package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.date.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Enumerated type of the parts of the day.
 */
public class PartOfDayTest {

  @Test
  public void testGetCorrespondingConstant() throws Exception {
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 0, 0, 0)), PartOfDay.MIDNIGHT);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 0, 59, 0)), PartOfDay.MIDNIGHT);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 1, 0, 0)), PartOfDay.NIGHT);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 4, 59, 0)), PartOfDay.NIGHT);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 5, 0, 0)), PartOfDay.DAWN);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 6, 59, 0)), PartOfDay.DAWN);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 7, 0, 0)), PartOfDay.MORNING);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 10, 59, 0)), PartOfDay.MORNING);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 11, 0, 0)), PartOfDay.NOON);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 12, 59, 0)), PartOfDay.NOON);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 13, 0, 0)), PartOfDay.AFTERNOON);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 16, 59, 0)), PartOfDay.AFTERNOON);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 17, 0, 0)), PartOfDay.DUSK);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 18, 59, 0)), PartOfDay.DUSK);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 19, 0, 0)), PartOfDay.EVENING);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 22, 59, 0)), PartOfDay.EVENING);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 23, 0, 0)), PartOfDay.MIDNIGHT);
    Assertions.assertEquals(PartOfDay.getCorrespondingConstant(new Date(1970, 1, 1, 23, 59, 0)), PartOfDay.MIDNIGHT);
  }

  @Test
  public void testGetSecondsToNext() throws Exception {
    Assertions.assertEquals(3600, PartOfDay.getSecondsToNext(new Date(1970, 1, 1, 22, 0, 0), PartOfDay.MIDNIGHT));
    Assertions.assertEquals(3000, PartOfDay.getSecondsToNext(new Date(1970, 1, 1, 22, 10, 0), PartOfDay.MIDNIGHT));
    Assertions.assertEquals(1200, PartOfDay.getSecondsToNext(new Date(1970, 1, 1, 22, 40, 0), PartOfDay.MIDNIGHT));
    Assertions.assertEquals(86400, PartOfDay.getSecondsToNext(new Date(1970, 1, 1, 23, 0, 0), PartOfDay.MIDNIGHT));
    Assertions.assertEquals(85800, PartOfDay.getSecondsToNext(new Date(1970, 1, 1, 23, 10, 0), PartOfDay.MIDNIGHT));
    Assertions.assertEquals(84000, PartOfDay.getSecondsToNext(new Date(1970, 1, 1, 23, 40, 0), PartOfDay.MIDNIGHT));
    Assertions.assertEquals(82800, PartOfDay.getSecondsToNext(new Date(1970, 1, 1, 0, 0, 0), PartOfDay.MIDNIGHT));
  }

}
