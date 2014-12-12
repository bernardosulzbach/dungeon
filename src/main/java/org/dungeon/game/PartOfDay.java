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
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Enumerated type of the parts of the day.
 * <p/>
 * Created by Bernardo Sulzbach on 24/09/2014.
 * <p/>
 * Updated by Bernardo Sulzbach on 21/11/2014: added starting hours.
 */
public enum PartOfDay {

    // Keep this array sorted by the startingHour in ascending order. See getCorrespondingConstant to understand why.
    NIGHT("Night", 0.4, 1),
    DAWN("Dawn", 0.6, 5),
    MORNING("Morning", 0.8, 7),
    NOON("Noon", 1.0, 11),
    AFTERNOON("Afternoon", 0.8, 13),
    DUSK("Dusk", 0.6, 17),
    EVENING("Evening", 0.4, 19),
    MIDNIGHT("Midnight", 0.2, 23);

    private final String stringRepresentation;

    // How bright is the sun at this part of the day?
    // Should be bigger than or equal to 0 and smaller than or equal to 1.
    private double luminosity;

    private int startingHour;

    PartOfDay(String stringRepresentation, double luminosity, int startingHour) {
        this.stringRepresentation = stringRepresentation;
        setLuminosity(luminosity);
        setStartingHour(startingHour);
    }

    /**
     * Returns the PartOfDay constant corresponding to a given time.
     *
     * @param dateTime a DateTime object.
     * @return a PartOfDay constant.
     */
    public static PartOfDay getCorrespondingConstant(DateTime dateTime) {
        int hour = dateTime.getHourOfDay();
        // MIDNIGHT starts at 23, therefore 0 does not satisfy the comparison and the following statement is necessary.
        if (hour == 0) {
            // It is also possible to add 24 to hour if it is zero, making it bigger than 23, but this is simpler.
            return MIDNIGHT;
        }
        PartOfDay[] podArray = values();
        // Note that the array is sorted in ascending startingHour order, therefore we iterate backwards.
        for (int i = podArray.length - 1; i >= 0; i--) {
            if (podArray[i].getStartingHour() <= hour) {
                return podArray[i];
            }
        }
        return null;
    }

    /**
     * @param cur the current time.
     * @param pod a part of the day.
     * @return the number of seconds between the current time and the start of the part of the day.
     */
    public static int getSecondsToNext(DateTime cur, PartOfDay pod) {
        DateTime startOfPod = cur.getHourOfDay() < pod.getStartingHour() ? cur : cur.plusDays(1);
        startOfPod = startOfPod.withHourOfDay(pod.getStartingHour()).withMinuteOfHour(0).withSecondOfMinute(0);
        return new Period(cur, startOfPod, PeriodType.seconds()).getSeconds();
    }

    public double getLuminosity() {
        return luminosity;
    }

    void setLuminosity(double luminosity) {
        if (luminosity < 0.0 || luminosity > 1.0) {
            throw new IllegalArgumentException("luminosity must be nonnegative and not bigger than 1.");
        }
        this.luminosity = luminosity;
    }

    public int getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(int startingHour) {
        if (startingHour < 0 || startingHour > 23) {
            throw new IllegalArgumentException("startingHour must be in the range [0, 23]");
        }
        this.startingHour = startingHour;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }

}
