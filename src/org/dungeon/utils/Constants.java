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
 * Constants class is a general-purpose constant storing class.
 *
 * @author Bernardo Sulzbach
 */
public class Constants {

    public static final String TITLE = "Dungeon";

    public static final int WIDTH = 79;

    /**
     * String used to improve output readability.
     */
    public static final String MARGIN = "  ";

    public static final String SAVE_PATH = "savefiles/";
    public static final String SAVE_NAME = "campaign";
    public static final String SAVE_EXTENSION = ".dungeon";

    public static final String FILE_FOUND = "A saved campaign was found.";

    public static final String SAVE_ERROR = "Could not save the game.";
    public static final String SAVE_SUCCESS = "Successfully saved the game.";
    public static final String SAVE_CONFIRM = "Do you want to save the game?";

    public static final String LOAD_ERROR = "Could not load the saved campaign.";
    public static final String LOAD_SUCCESS = "Successfully loaded the saved campaign.";
    public static final String LOAD_CONFIRM = "Do you want to load the game?";

    public static final String ACHIEVEMENT_UNLOCKED = "Achievement Unlocked!";
    public static final String LEVEL_UP = "Level up!";

    // Two 79-character long strings used to improve readability.
    public static final String LINE_1 = StringUtils.makeRepeatedCharacterString(WIDTH, '-');
    public static final String LINE_2 = StringUtils.makeRepeatedCharacterString(WIDTH, '=');

    public static final String HEADING = LINE_2 + '\n' + StringUtils.centerString(TITLE, WIDTH, '-') + '\n' + LINE_2;
    public static final String SUICIDE_ATTEMPT = "Are you trying to kill yourself?";
    /**
     * The string used to alert the player about invalid input.
     */
    public static final String INVALID_INPUT = "Invalid input.";
    public static final String NOT_YET_SUPPORTED = "Not yet supported.";
    /**
     * Movement strings.
     */
    public static final String WALK_BLOCKED = "The path is blocked.";
    public static final String NO_CREATURES = "You do not see any creatures here.";
    public static final String NO_ITEMS = "You do not see any items here.";
}
