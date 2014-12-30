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

package org.dungeon.help;

/**
 * String constants used by some of the classes on the help package.
 * <p/>
 * Created by Bernardo on 01/10/2014.
 */
class HelpConstants {

  // The correct usage of printHelp.
  public static final String HELP_USAGE = "  Usage: help (command)";

  // The path of the text file to be parsed.
  public static final String COMMAND_TXT_PATH = "commands.txt";
  public static final String ASPECT_TXT_PATH = "aspects.txt";

  // Strings used by the Help class.
  public static final String NO_INFO = "No info available.";
  public static final String NO_ALIASES = "No aliases.";
  public static final String NOT_INITIALIZED = "Help data not initialized.";
}
