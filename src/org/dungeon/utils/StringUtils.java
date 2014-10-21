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
     * @param fill the filling character.
     * @return a centered string.
     */
    public static String centerString(String string, char fill) {
        return centerString(string, Constants.WIDTH, fill);
    }

    /**
     * Centers a string.
     *
     * @param string the string to be centered.
     * @param width the width of the resulting string.
     * @param fill the filling character.
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

//    /**
//     * Wraps a string of text to the given number of columns.
//     */
//    public static String wrap(String string, int columns) {
//        if (string == null) {
//            return null;
//        }
//        if (columns < 1) {
//            columns = 1;
//        }
//        final StringBuilder stringBuilder = new StringBuilder(string);
//        boolean inAWord = false;
//        int lastLineBreak = 0;
//        int lastWordStart = 0;
//        for (int i = 0; i < string.length(); i++) {
//            // TODO: improve performance / logic.
//            //       read the wrap method of the Apache Commons Library and compare it to this one.
//            // Updates lastLineBreak.
//            if (stringBuilder.charAt(i) == '\n') {
//                lastLineBreak = i;
//                continue;
//            }
//            // This if-then-else statement is used to update lastWordStart.
//            if (inAWord) {
//                if (!Character.isAlphabetic(stringBuilder.charAt(i))) {
//                    inAWord = false;
//                }
//            } else {
//                if (Character.isAlphabetic(stringBuilder.charAt(i))) {
//                    inAWord = true;
//                    lastWordStart = i;
//                }
//            }
//            // This if-then statement verifies if a line wrapping is required.
//            if (i - lastLineBreak > columns) {
//                stringBuilder.insert(lastWordStart, '\n');
//                lastLineBreak = lastWordStart;
//            }
//        }
//        return stringBuilder.toString();
//    }

    /**
     * Split a string of text into an array of words.
     */
    public static String[] split(String string) {
        return string.split("\\s+");
    }

    /**
     * Tests if a string only contains whitespaces and returns true if it does.
     */
    public static boolean isBlankString(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isWhitespace(c)) {
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

}
