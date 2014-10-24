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

    private static final String TITLE = "Dungeon";

    /** Basic version information. (19/10/2014) */
    private static final String VERSION = "v0.0.3";
    private static final String CODENAME = "Bowman";

    // The name used as the window title. Contains the game's name, version and its version's codename.
    public static final String FULLNAME = String.format("%s %s (%s)", TITLE, VERSION, CODENAME);

    public static final int WIDTH = 100;
    public static final String LINE_1 = StringUtils.makeRepeatedCharacterString(WIDTH, '-');
    private static final String LINE_2 = StringUtils.makeRepeatedCharacterString(WIDTH, '=');
    public static final String HEADING = LINE_2 + '\n' + StringUtils.centerString(TITLE, '-') + '\n' + LINE_2;

    // Loader strings.
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
    public static final String SUICIDE_ATTEMPT_1 = "You cannot attempt suicide.";
    public static final String SUICIDE_ATTEMPT_2 = "You cannot target yourself.";

    /**
     * The string used to alert the player about invalid input.
     */
    public static final String INVALID_INPUT = "Invalid input.";

    // Description strings.
    public static final String NO_CREATURES = "You do not see any creatures here.";
    public static final String NO_ITEMS = "You do not see any items here.";

    // Selection entry default format.
    // Conforming to this format increases the uniformity of the output of the game, making it more readable.
    public static final String COMMAND_HELP_FORMAT = "%-20s %s";
    public static final String SELECTION_ENTRY_FORMAT = "%-10s %-15s";

    public static final String EMPTY_COMMAND_HISTORY = "The command history is empty.";

    // Inventory strings.
    public static final String INVENTORY_FULL = "Inventory is full.";

    // Item not found messages.
    public static final String ITEM_NOT_FOUND_IN_INVENTORY = "Item not found in inventory.";
    public static final String ITEM_NOT_FOUND_IN_LOCATION = "Item not found in the current location.";

    public static final String NOT_EQUIPPING_A_WEAPON = "You are not equipping a weapon.";

    public static final String CANT_SEE_ANYTHING = "It's too dark, you can't see anything.";

    public static final String HERO_ID = "HERO";
    public static final String INVALID_COMMAND = "'%s' is not a command.";
    public static final String SUGGEST_COMMANDS = "See 'commands' for a list of commands.";
}
