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

import java.util.List;

import org.dungeon.core.game.Selectable;
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
     * Checks if a string is a valid name in the game.
     *
     * @param name
     * @return
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
     * Method that let the player select a Selectable object from a List.
     */
    public static <T extends Selectable> T selectFromList(List<T> list) {
        StringBuilder builder = new StringBuilder("0. Abort\n");
        int index = 1;
        for (Selectable aSelectable : list) {
            builder.append(index).append(". ").append(aSelectable.toSelectionEntry()).append('\n');
            index++;
        }
        IO.writeString(builder.toString());
        int choice = -1;
        while (true) {
            try {
                choice = Integer.parseInt(IO.readString());
            } catch (NumberFormatException exception) {
                IO.writeString(Constants.INVALID_INPUT);
                continue;
            }
            if (choice < 0 || choice > list.size()) {
                IO.writeString(Constants.INVALID_INPUT);
            } else {
                break;
            }
        }
        if (choice == 0) {
            return null;
        }
        return list.get(choice - 1);
    }

}
