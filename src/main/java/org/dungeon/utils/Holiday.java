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
package org.dungeon.utils;

import org.joda.time.DateTime;

/**
 * Holiday class that stores holidays strings and defines a method to retrieve theses strings with a DateTime object.
 * <p/>
 * Created by Bernardo Sulzbach on 11/11/14.
 */
public abstract class Holiday {

    private static final String NEW_YEAR = "New Year";
    private static final String CHRISTMAS = "Christmas";
    private static final String SAINT_PATRICK = "Saint Patrick's Day";
    private static final String INDEPENDENCE_DAY = "Independence Day";
    private static final String VETERANS_DAY = "Veterans Day";
    private static final String HALLOWEEN = "Halloween";

    /**
     * Retrieves the holiday corresponding to a hero's mind on a given date (obtained by getting the date of the world
     * where the hero is).
     *
     * @param date a DateTime object with the requested date.
     * @return a String with the holiday description if there is a holiday, <code>null</code> otherwise.
     */
    public static String getHoliday(DateTime date) {
        int month = date.getMonthOfYear();
        int day = date.getDayOfMonth();
        switch (month) {
            case 1:
                switch (day) {
                    case 1:
                        return NEW_YEAR;
                }
                break;
            case 3:
                switch (day) {
                    case 17:
                        return SAINT_PATRICK;
                }
                break;
            case 7:
                switch (day) {
                    case 4:
                        return INDEPENDENCE_DAY;
                }
                break;
            case 10:
                switch (day) {
                    case 31:
                        return HALLOWEEN;
                }
                break;
            case 11:
                switch (day) {
                    case 11:
                        return VETERANS_DAY;
                }
                break;
            case 12:
                switch (day) {
                    case 25:
                        return CHRISTMAS;
                }
                break;
        }
        return null;
    }

}