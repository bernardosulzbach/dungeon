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

import java.awt.Color;
import org.dungeon.core.game.Game;
import org.dungeon.utils.StringUtils;

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
     * Outputs a string of text, stripping unnecessary spaces at the end and formatting it according to a WriteStyle.
     */
    public static void writeString(String string, Color color) {
        Game.gameWindow.writeToTextPane(StringUtils.clearEnd(string), color);
    }
}
