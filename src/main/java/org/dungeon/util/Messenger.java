/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

import org.dungeon.io.IO;

import java.awt.Color;

/**
 * Messenger helper class that defines several static methods to print uniform warning messages.
 * <p/>
 * Created by Bernardo on 12/02/2015.
 */
public class Messenger {

  /**
   * Prints a message reporting the usage of an invalid command.
   */
  public static void printInvalidCommandMessage(String command) {
    IO.writeString("'" + command + "' is not a recognized command.", Color.RED);
    IO.writeString("See 'commands' for a complete list of commands.", Color.ORANGE);
  }

  public static void printInvalidNumberFormatOrValue() {
    IO.writeString("Invalid number format or value.");
  }

  /**
   * Prints a warning that a command requires arguments.
   */
  public static void printMissingArgumentsMessage() {
    IO.writeString("This command requires arguments.");
  }

  public static void printAmbiguousSelectionMessage() {
    IO.writeString("Provided input is ambiguous.");
  }

  /**
   * Prints a warning that a directory creation failed.
   */
  public static void printFailedToCreateDirectoryMessage(String directory) {
    IO.writeString("Failed to create the '" + directory + "' directory.");
  }

}
