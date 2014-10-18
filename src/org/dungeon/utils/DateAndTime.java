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

import org.dungeon.io.IO;
import org.dungeon.io.WriteStyle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {

    // DateFormats for time and date printing.
    private static final DateFormat TIME = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Print the current time according to the final SimpleDateFormat TIME.
     */
    public static void printTime() {
        IO.writeString(TIME.format(new Date()));
    }

    /**
     * Print the current date according to the final SimpleDateFormat DATE.
     */
    public static void printDate() {
        IO.writeString(DATE.format(new Date()));
    }

}
