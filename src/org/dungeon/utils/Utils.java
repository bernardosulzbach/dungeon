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

import org.dungeon.core.game.Game;
import org.dungeon.help.Help;
import org.dungeon.io.IO;
import org.joda.time.Period;

import java.awt.*;

/**
 * General utility class.
 *
 * @author Bernardo Sulzbach
 */
public class Utils {

    public static String LESS_THAN_A_DAY = "Less than a day";

    /**
     * Prints the startup heading.
     */
    public static void printHeading() {
        IO.writeString(Constants.HEADING);
    }

    /**
     * Prints the full name of the current version of the game.
     */
    public static void printVersion() {
        IO.writeString(Constants.FULLNAME);
    }

    /**
     * Prints a message reporting the usage of an invalid command.
     */
    public static void printInvalidCommandMessage(String command) {
        IO.writeString(String.format(Constants.INVALID_COMMAND, command), Color.RED);
        if (Help.isInitialized()) {
            IO.writeString(Constants.SUGGEST_COMMANDS, Color.ORANGE);
        }
    }

    public static void printCredits() {
        IO.writeString("This game was made by Bernardo Sulzbach and Gabriel Bohmer.");
    }

    /**
     * Simulates a random roll.
     *
     * @param chance the probability of a true result. Must be nonnegative and smaller than or equal to 1.
     * @return a boolean indicating if the roll was successful or not.
     */
    public static boolean roll(double chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("chance must be nonnegative and smaller than 1.");
        }
        return chance > Game.RANDOM.nextDouble();
    }

    public static String dateDifferenceToString(long start, long end) {
        Period period = new Period(start, end);
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();
        StringBuilder builder = new StringBuilder();
        if (years != 0) {
            if (years == 1) {
                builder.append(years).append(" year");
            } else {
                builder.append(years).append(" years");
            }
        }
        if (months != 0) {
            if (builder.length() != 0) {
                if (days == 0) {
                    builder.append(" and ");
                } else {
                    builder.append(", ");
                }
            }
            if (months == 1) {
                builder.append(months).append(" month");
            } else {
                builder.append(months).append(" months");
            }
        }
        if (days != 0) {
            if (builder.length() != 0) {
                builder.append(" and ");
            }
            if (days == 1) {
                builder.append(days).append(" day");
            } else {
                builder.append(days).append(" days");
            }
        }
        if (builder.length() == 0) {
            builder.append(LESS_THAN_A_DAY);
        }
        return builder.toString();
    }

}
