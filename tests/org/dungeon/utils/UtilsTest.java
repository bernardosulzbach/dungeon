package org.dungeon.utils;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class UtilsTest extends TestCase {

    @Test
    public void testRoll() throws Exception {
        assertEquals(Utils.roll(0.0), false);
        assertEquals(Utils.roll(1.0), true);
    }

    @Test
    public void testDateDifferenceToString() throws Exception {
        Calendar start = new GregorianCalendar(2014, 10, 20);
        long startInMillis = start.getTimeInMillis();
        Calendar end = new GregorianCalendar(2014, 10, 20);

        assertEquals(Utils.LESS_THAN_A_DAY, Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));

        end.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals("1 day", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals("2 days", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, -2);

        end.add(Calendar.MONTH, 1);
        assertEquals("1 month", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.MONTH, 1);
        assertEquals("2 months", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals("2 months and 1 day", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals("2 months and 2 days", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, -2);
        end.add(Calendar.MONTH, -2);

        end.add(Calendar.YEAR, 1);
        assertEquals("1 year", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.YEAR, 1);
        assertEquals("2 years", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals("2 years and 1 day", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, -1);
        end.add(Calendar.MONTH, 1);
        assertEquals("2 years and 1 month", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
        end.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals("2 years, 1 month and 1 day", Utils.dateDifferenceToString(startInMillis, end.getTimeInMillis()));
    }

}