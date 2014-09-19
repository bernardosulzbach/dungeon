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

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Utility methods related to this project licensing.
 * <p/>
 * Change log
 * Created by Bernardo on 19/09/2014.
 */
public class LicenseUtils {

    // The path to the license text file.
    public static final String LICENSE_PATH = "/org/dungeon/res/LICENSE.txt";

    public static void printLicense() {
        // TODO: stop reading the LICENSE.txt file in every execution.
        try {
            InputStreamReader inputStream = new InputStreamReader(LicenseUtils.class.getResourceAsStream(LICENSE_PATH));
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            StringBuilder builder = new StringBuilder();
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                builder.append(currentLine).append(System.lineSeparator());
            }
            IO.writeString(builder.toString());
        } catch (Exception e) {
            IO.writeString("Failed to read the license file.");
        }
    }

}
