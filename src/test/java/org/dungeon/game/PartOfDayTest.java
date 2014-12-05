/*
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dungeon.game;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Enumerated type of the parts of the day.
 * <p/>
 * Created by Bernardo Sulzbach on 24/09/2014.
 * <p/>
 * Updated by Bernardo Sulzbach on 21/11/2014: added startingHour.
 */
public class PartOfDayTest {

    @Test
    public void testGetCorrespondingConstant() throws Exception {
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 0, 0)), PartOfDay.MIDNIGHT);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 0, 59)), PartOfDay.MIDNIGHT);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 1, 0)), PartOfDay.NIGHT);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 4, 59)), PartOfDay.NIGHT);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 5, 0)), PartOfDay.DAWN);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 6, 59)), PartOfDay.DAWN);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 7, 0)), PartOfDay.MORNING);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 10, 59)), PartOfDay.MORNING);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 11, 0)), PartOfDay.NOON);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 12, 59)), PartOfDay.NOON);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 13, 0)), PartOfDay.AFTERNOON);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 16, 59)), PartOfDay.AFTERNOON);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 17, 0)), PartOfDay.DUSK);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 18, 59)), PartOfDay.DUSK);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 19, 0)), PartOfDay.EVENING);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 22, 59)), PartOfDay.EVENING);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 23, 0)), PartOfDay.MIDNIGHT);
        Assert.assertEquals(PartOfDay.getCorrespondingConstant(new DateTime(1970, 1, 1, 23, 59)), PartOfDay.MIDNIGHT);
    }

    @Test
    public void testGetSecondsToNext() throws Exception {
        Assert.assertEquals(3600, PartOfDay.getSecondsToNext(new DateTime(1970, 1, 1, 22, 0), PartOfDay.MIDNIGHT));
        Assert.assertEquals(3000, PartOfDay.getSecondsToNext(new DateTime(1970, 1, 1, 22, 10), PartOfDay.MIDNIGHT));
        Assert.assertEquals(1200, PartOfDay.getSecondsToNext(new DateTime(1970, 1, 1, 22, 40), PartOfDay.MIDNIGHT));
        Assert.assertEquals(86400, PartOfDay.getSecondsToNext(new DateTime(1970, 1, 1, 23, 0), PartOfDay.MIDNIGHT));
        Assert.assertEquals(85800, PartOfDay.getSecondsToNext(new DateTime(1970, 1, 1, 23, 10), PartOfDay.MIDNIGHT));
        Assert.assertEquals(84000, PartOfDay.getSecondsToNext(new DateTime(1970, 1, 1, 23, 40), PartOfDay.MIDNIGHT));
        Assert.assertEquals(82800, PartOfDay.getSecondsToNext(new DateTime(1970, 1, 1, 0, 0), PartOfDay.MIDNIGHT));
    }

}
