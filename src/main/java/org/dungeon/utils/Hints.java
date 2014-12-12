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
 * Class Hints that holds all the hints available from inside the game.
 * <p/>
 * Created by Bernardo on 20/09/2014.
 */
public class Hints {

    // TODO: extract this to a resource file.
    public static final String[] hintsArray = {
            "Attacks against critters deal 100% bonus damage.",
            "Equipped items are colored purple in your inventory.",
            "Every combatant movement is considered to be a battle turn.",
            "If the creature has a weapon, it will be used to perform the attack. " +
                    "Otherwise, the creature will attack with its bare hands.",
            "The command 'achievements' will also show the total number of achievements.",

            "There is a 'poem' command that prints a poem to the screen.",
            "Use 'config bold' to make the text bold.\n" +
                    "This can be done to improve readability or just to make the game look nicer."};

}
