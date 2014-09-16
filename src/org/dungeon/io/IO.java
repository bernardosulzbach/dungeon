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
     * Outputs a string to the console, stripping unnecessary newlines at the
     * end.
     *
     * @param string
     *            the string to be printed.
     */
    public static void writeString(String string) {
        while (string.endsWith("\n")) {
            string = string.substring(0, string.length() - 1);
        }
        System.out.println(string);
    }

    /**
     * Read a line of input from the user.
     *
     * @return
     */
    public static String readString() {
        String line;
        do {
            System.out.print("> ");
            line = SCANNER.nextLine().trim();
        } while (line.equals(""));
        return line;
    }

    /**
     * Read a line of input from the user and returns an array with the words in
     * that line.
     *
     * @return a String array.
     */
    public static String[] readWords() {
        return readString().split("\\s+");
    }

}
