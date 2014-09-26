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

/**
 * @author Bernardo Sulzbach
 */
public class StringUtils {

    /**
     * Centers a string.
     *
     * @param string the string to be centered.
     * @return a centered string.
     */
    public static String centerString(String string) {
        return centerString(string, Constants.WIDTH, ' ');
    }

    /**
     * Centers a string.
     *
     * @param string the string to be centered.
     * @param fill   the filling character.
     * @return a centered string.
     */
    public static String centerString(String string, char fill) {
        return centerString(string, Constants.WIDTH, fill);
    }

    /**
     * Centers a string.
     *
     * @param string the string to be centered.
     * @param width  the width of the resulting string.
     * @param fill   the filling character.
     * @return a centered string.
     */
    public static String centerString(String string, int width, char fill) {
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
     * Checks if a string is alphabetic (only contains letters).
     */
    public static boolean isAlphabetic(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isAlphabetic(name.charAt(i))) {
                return false;
            }
        }
        return true;
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
     * Verifies if the first character of a string matches the first character of the first character of another string.
     * Ignores case.
     * Throws IllegalArgumentException.
     */
    public static boolean firstEqualsIgnoreCase(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) {
            throw new IllegalArgumentException("Strings must have at least one character.");
        }
        return Character.toLowerCase(s1.charAt(0)) == Character.toLowerCase(s2.charAt(0));
    }

    /**
     * Split a string of text into an array of words.
     */
    public static String[] split(String string) {
        return string.split("\\s+");
    }
}
