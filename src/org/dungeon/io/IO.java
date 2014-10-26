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

import org.dungeon.core.game.Game;
import org.dungeon.utils.Constants;
import org.dungeon.utils.StringUtils;

import java.awt.*;

/**
 * IO class that encapsulates all Input/Output operations.
 *
 * @author Bernardo Sulzbach - 13/09/2014
 */
public class IO {

    public static void writeString(String string) {
        writeString(string, Color.LIGHT_GRAY);
    }

    /**
     *
     */
    public static void writeString(String string, Color color) {
        writeString(string, color, true);
    }

    /**
     * Outputs a string of text using the given color and inserting a newline character if set to.
     */
    private static void writeString(String string, Color color, boolean endLine) {
        Game.gameWindow.writeToTextPane(StringUtils.clearEnd(string), color, endLine);
    }


    /** Method used to write a line of full width with dots separating two strings. */
    public static void writeFilledLine(String name, String value) {
        int dots = Constants.WIDTH - name.length() - value.length();
        if (dots < 0) {
            throw new IllegalArgumentException("strings are too large.");
        }
        writeString(name, Color.LIGHT_GRAY, false);
        StringBuilder stringBuilder = new StringBuilder();
        for (; dots > 0; dots--) {
            stringBuilder.append('.');
        }
        writeString(stringBuilder.toString(), Color.GRAY, false);
        writeString(value, Color.LIGHT_GRAY);
    }
}
