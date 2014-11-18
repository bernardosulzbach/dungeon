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
import org.dungeon.core.game.Selectable;
import org.dungeon.help.Help;
import org.dungeon.io.IO;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.awt.*;
import java.util.List;

/**
 * General utility class. All utility methods of the game should be placed in this class.
 *
 * @author Bernardo Sulzbach
 */
public class Utils {

    public static String LESS_THAN_A_DAY = "Less than a day";

    /**
     * Prints the full name of the current version of the game.
     */
    public static void printVersion() {
        IO.writeString(Constants.TITLE);
    }

    /**
     * Pads a string with spaces at the end in order to reach a desired length. If the provided string's length is
     * bigger than the desired length, the same string is returned.
     */
    public static String padString(String original, int desiredLength) {
        int requiredSpaces = desiredLength - original.length();
        if (requiredSpaces > 0) {
            StringBuilder stringBuilder = new StringBuilder(desiredLength);
            stringBuilder.append(original);
            for (int i = 0; i < requiredSpaces; i++) {
                stringBuilder.append(' ');
            }
            return stringBuilder.toString();
        } else {
            return original;
        }
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

    /**
     * Parses a list of Selectable candidates trying to find a match based on a query string.
     * <p/>
     * This method is not case-sensitive.
     * <p/>
     * Parts of individual tokens will result in a match if the parts order matches the order of the candidate's name.
     * <p/>
     * (e. g.: "g b r" will match "Giant Black Rat" as "W Watch" will match "Wrist Watch")
     *
     * @param candidates a list of Selectable candidates.
     * @param tokens     the text tokens.
     * @return a SelectionResult object with all the matches found.
     */
    public static <T extends Selectable> SelectionResult<T> selectFromList(List<T> candidates, String[] tokens) {
        SelectionResult<T> selectionResult = new SelectionResult<T>();
        for (T candidate : candidates) {
            if (checkQueryMatch(tokens, split(candidate.getName()))) {
                selectionResult.addMatch(candidate);
            }
        }
        return selectionResult;
    }

    /**
     * Checks if two string arrays match. Not case-sensitive. Only checks for the same starting characters.
     * <p/>
     * "g", "g b", "g r" and "b r" match "Giant Black Rat". "g r b", "b g r", "b r g", "r b g", "r g b" do not.
     *
     * @param query     the query array.
     * @param candidate the candidate array.
     * @return a boolean indicating if the tokens match or not.
     */
    public static boolean checkQueryMatch(String[] query, String[] candidate) {
        if (query.length > candidate.length) {
            return false;
        }
        boolean foundMatch = false;
        int indexOfLastMatchInCandidate = 0;
        for (String queryToken : query) {
            // TODO: reword this. Too messy.
            while (indexOfLastMatchInCandidate < candidate.length && !foundMatch) {
                if (startsWithIgnoreCase(candidate[indexOfLastMatchInCandidate], queryToken)) {
                    foundMatch = true;
                }
                indexOfLastMatchInCandidate++;
            }
            if (!foundMatch) {
                return false;
            } else {
                foundMatch = false;
            }
        }
        return true;
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

    /**
     * Given two dates, this method returns a string of text of a humanized representation of the interval between
     * them.
     *
     * @param start the start time.
     * @param end   the end time.
     * @return a String of text.
     */
    public static String dateDifferenceToString(DateTime start, DateTime end) {
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

    /**
     * Prints a humanized message warning the user that the command used requires arguments.
     */
    public static void printMissingArgumentsMessage() {
        switch (Game.RANDOM.nextInt(3)) {
            case 0:
                IO.writeString("Provide some arguments.", Color.BLUE);
                break;
            case 1:
                IO.writeString("Missing arguments.", Color.BLUE);
                break;
            case 2:
                IO.writeString("This command requires arguments.", Color.BLUE);
                break;
        }
    }

    /**
     * Checks if a string starts with a given string, ignoring case differences.
     *
     * @param a the base string.
     * @param b the prefix.
     * @return true, if the base string starts with the prefix, ignoring case differences.
     */
    public static boolean startsWithIgnoreCase(String a, String b) {
        return a.toLowerCase().startsWith(b.toLowerCase());
    }


    /**
     * Returns all the contents of a string after the first colon.
     *
     * @param string the string of text.
     * @return a String smaller than the one passed as an argument.
     */
    public static String getAfterColon(String string) {
        int colonPosition = string.indexOf(':');
        if (colonPosition == -1) {
            throw new IllegalArgumentException("string does not have a colon.");
        } else {
            return string.substring(colonPosition + 1);
        }
    }

    /**
     * Centers a string.
     *
     * @param string the string to be centered.
     * @param fill   the filling character.
     * @return a centered string.
     */
    public static String centerString(String string, char fill) {
        return centerString(string, Constants.COLS, fill);
    }

    /**
     * Centers a string.
     *
     * @param string the string to be centered.
     * @param width  the width of the resulting string.
     * @param fill   the filling character.
     * @return a centered string.
     */
    private static String centerString(String string, int width, char fill) {
        int length = string.length();
        if (length > width) {
            throw new IllegalArgumentException("String is bigger than the desired width.");
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < (width - length) / 2; i++) {
                builder.append(fill);
            }
            builder.append(string);
            for (int i = 0; i < (width - length) / 2; i++) {
                builder.append(fill);
            }
            if (builder.length() < width) {
                builder.append(fill);
            }
            return builder.toString();
        }
    }

    /**
     * Creates a string of repeated characters.
     */
    public static String makeRepeatedCharacterString(int repetitions, char character) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < repetitions; i++) {
            builder.append(character);
        }
        return builder.toString();
    }

    /**
     * Split a string of text into an array of words.
     */
    public static String[] split(String string) {
        return string.split("\\s+");
    }

    /**
     * Tests if a string only contains whitespaces and returns true if it does.
     */
    public static boolean isNotBlankString(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a string is alphabetic (only contains letters).
     *
     * @param s the string to be tested.
     * @return a boolean indicating if the string only contains letters.
     */
    public static boolean isAlphabetic(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isAlphabetic(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Strips all newlines and spaces at the end of the string.
     */
    public static String clearEnd(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        int length = stringBuilder.length();
        char lastChar;
        while (length > 0) {
            lastChar = stringBuilder.charAt(length - 1);
            if (Character.isSpaceChar(lastChar) || lastChar == '\n') {
                stringBuilder.setLength(stringBuilder.length() - 1);
            } else {
                break;
            }
            length = stringBuilder.length();
        }
        return stringBuilder.toString();
    }

    public static void printAmbiguousSelectionMessage() {
        if (Game.RANDOM.nextBoolean()) {
            IO.writeString("Provided input is ambiguous.");
        } else {
            IO.writeString("More than one entity with this name could be found.");
        }
    }

    /**
     * Prints a message warning the player that a directory creation failed. This method exists to avoid repeated code
     * and improve
     */
    public static void printFailedToCreateDirectoryMessage(String directory) {
        IO.writeString("Failed to create the " + directory + " directory.");
    }

}
