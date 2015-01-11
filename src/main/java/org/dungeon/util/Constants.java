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

package org.dungeon.util;

import org.dungeon.game.ID;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Constants class is a general-purpose constant storing class.
 *
 * @author Bernardo Sulzbach
 */
public class Constants {

  public static final int COLS = 100;
  public static final String FILE_FOUND = "A saved campaign was found.";
  public static final String ACHIEVEMENT_UNLOCKED = "Achievement Unlocked!";

  public static final String SUICIDE_ATTEMPT_1 = "You cannot attempt suicide.";
  public static final String SUICIDE_ATTEMPT_2 = "You cannot target yourself.";

  // The string used to alert the player about invalid input.
  public static final String INVALID_INPUT = "Invalid input.";

  // Selection entry default format.
  // Conforming to this format increases the uniformity of the output of the game, making it more readable.
  public static final String COMMAND_HELP_FORMAT = "%-20s %s";
  public static final String LIST_ENTRY_FORMAT = "%-15s%s";

  // Inventory strings.
  public static final String INVENTORY_FULL = "Inventory is full.";

  // Item not found messages.
  public static final String NOT_EQUIPPING_A_WEAPON = "You are not equipping a weapon.";
  public static final String CANT_SEE_ANYTHING = "It's too dark, you can't see anything.";
  public static final String INVALID_COMMAND = "'%s' is not a command.";
  public static final String SUGGEST_COMMANDS = "See 'commands' for a list of commands.";

  // DateFormats for time and date printing.
  public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
  public static final Color FORE_COLOR_NORMAL = Color.LIGHT_GRAY;
  public static final Color FORE_COLOR_DARKER = Color.GRAY;
  public static final Color HEALTH_BAR_COLOR = Color.GREEN;

  // How many characters the bar name should occupy.
  public static final int BAR_NAME_LENGTH = 16;

  // IDs
  public static final ID HERO_ID = new ID("HERO");
  public static final ID UNARMED_ID = new ID("");

  public static final String NAME = "Dungeon";

}
