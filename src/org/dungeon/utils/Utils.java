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
import org.dungeon.io.IO;

/**
 * General utility class.
 *
 * @author Bernardo Sulzbach
 */
public class Utils {

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
     * Checks if a string is a valid name in the game.
     */
    public static boolean isValidName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Names must be at least one character long.");
        } else if (!StringUtils.isAlphabetic(name)) {
            throw new IllegalArgumentException("Names must contain only letters.");
        } else {
            return true;
        }
    }

    /**
     * Prints a message reporting the usage of an invalid command.
     */
    public static void printInvalidCommandMessage(String command) {
        IO.writeString(command + " is not a valid command.\nSee 'commands' for a list of valid commands.");
    }

    public static void printCredits() {
        IO.writeString("This game was made by Bernardo Sulzbach and Gabriel Bohmer.\n");
    }

    /**
     * Simulates a random roll.
     *
     * @param chance the probability of a true result. Must be nonnegative and smaller than 1.
     */
    public static boolean roll(double chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("chance must be nonnegative and smaller than 1.");
        }
        return chance > Game.RANDOM.nextDouble();
    }

}
