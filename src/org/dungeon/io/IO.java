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
package org.dungeon.io;

import org.dungeon.utils.Constants;

import java.util.Scanner;

/**
 * IO class that encapsulates all Input/Output operations.
 *
 * @author Bernardo Sulzbach - 13/09/2014
 */
public class IO {

    /**
     * The Scanner used for all the IO operations.
     */
    public static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Outputs a string of text, stripping unnecessary spaces at the end and formatting it according to a WriteStyle.
     */
    public static void writeString(String string, WriteStyle style) {
        switch (style) {
            case COMMAND:
                string = insertBeforeLines(string, Constants.MARGIN + Constants.MARGIN);
                string = string.replaceAll("\n", "\n\n");
                break;
            case MARGIN:
                string = insertBeforeLines(string, Constants.MARGIN);
                break;
            case WARNING:
                System.err.println(Constants.WARNING + ": " + string);
                return;
        }
        // Remove extra newlines at the end.
        while (string.charAt(string.length() - 1) == '\n' || Character.isSpaceChar(string.charAt(string.length() - 1))) {
            string = string.substring(0, string.length() - 1);
        }
        System.out.println(string);
    }

    public static String insertBeforeLines(String text, String word) {
        if (text.isEmpty()) {
            throw new IllegalArgumentException("text should be a non-empty String.");
        }
        if (word.isEmpty()) {
            throw new IllegalArgumentException("word should be a non-empty String.");
        }
        StringBuilder builder = new StringBuilder(text);
        builder.insert(0, word);
        int index = builder.indexOf("\n");
        while (index != -1) {
            builder.insert(index + 1, word);
            index = builder.indexOf("\n", index + word.length() + 1);
        }
        return builder.toString();
    }

    /**
     * Read a line of input from the user.
     */
    public static String readString() {
        String line;
        do {
            System.out.print("> ");
            line = SCANNER.nextLine().trim();
        } while (line.equals(""));
        return line;
    }

}
